package io.github.cursoms.msavaliadorcredito.application;

import io.github.cursoms.msavaliadorcredito.ClienteResourceClient;
import io.github.cursoms.msavaliadorcredito.domain.model.DadosCliente;
import io.github.cursoms.msavaliadorcredito.domain.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    public final ClienteResourceClient clienteResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){
        // Obter Dados Cliente: msclientes
        ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteResourceClient.dadosCliente(cpf);
        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponseEntity.getBody())
                .build();
    }

}