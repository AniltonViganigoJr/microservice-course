package io.github.cursoms.msavaliadorcredito.application;

import io.github.cursoms.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.cursoms.msavaliadorcredito.infra.clients.CartaoResourceClient;
import io.github.cursoms.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.cursoms.msavaliadorcredito.domain.model.DadosCliente;
import io.github.cursoms.msavaliadorcredito.domain.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    public final ClienteResourceClient clienteResourceClient;
    public final CartaoResourceClient cartaoResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){

        ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteResourceClient.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> dadosCartaoResponseEntity = cartaoResourceClient.getCartoesByCliente(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponseEntity.getBody())
                .cartoes(dadosCartaoResponseEntity.getBody())
                .build();
    }
}