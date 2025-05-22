package io.github.cursoms.msavaliadorcredito.application;

import feign.FeignException;
import io.github.cursoms.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import io.github.cursoms.msavaliadorcredito.application.exception.ErroComunicacaoMicroserviceException;
import io.github.cursoms.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import io.github.cursoms.msavaliadorcredito.domain.model.*;
import io.github.cursoms.msavaliadorcredito.infra.clients.CartaoResourceClient;
import io.github.cursoms.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.cursoms.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartaoResourceClient cartaoResourceClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException,
            ErroComunicacaoMicroserviceException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> dadosCartaoResponseEntity = cartaoResourceClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponseEntity.getBody())
                    .cartoes(dadosCartaoResponseEntity.getBody())
                    .build();

        }catch (FeignException.FeignClientException e){
            int statusCode = e.status();
            if (HttpStatus.NOT_FOUND.value() == statusCode){
                throw  new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(e.getMessage(), statusCode);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao (String cpf, Long renda) throws DadosClienteNotFoundException,
            ErroComunicacaoMicroserviceException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartaoResourceClient.getCartoesRendaAteh(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoeAprovados =  cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado cartaoAprovado = new CartaoAprovado();
                cartaoAprovado.setCartao(cartao.getNome());
                cartaoAprovado.setBandeira(cartao.getBandeira());
                cartaoAprovado.setLimiteAprovado(limiteAprovado);

                String nomeCartao = cartao.getNome().toUpperCase();
                boolean ehCartaoBalck = nomeCartao.contains("BLACK");

                if (!ehCartaoBalck || renda >= 20000) {
                    return cartaoAprovado;
                }
                return null;

            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoeAprovados);

        }catch (FeignException.FeignClientException e){
            int statusCode = e.status();
            if (HttpStatus.NOT_FOUND.value() == statusCode){
                throw  new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(e.getMessage(), statusCode);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}