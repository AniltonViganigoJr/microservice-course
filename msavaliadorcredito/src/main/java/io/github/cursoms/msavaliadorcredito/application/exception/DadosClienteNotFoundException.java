package io.github.cursoms.msavaliadorcredito.application.exception;

public class DadosClienteNotFoundException extends RuntimeException{
    public DadosClienteNotFoundException() {
        super("Dados do cliente não encontrado para o CPF informado.");
    }
}
