package io.github.cursoms.msavaliadorcredito.application.exception;

public class ErroSolicitacaoCartaoException extends RuntimeException{
    public ErroSolicitacaoCartaoException(String message) {
        super(message);
    }
}