package io.github.cursoms.msavaliadorcredito.application.exception;

import lombok.Getter;

public class ErroComunicacaoMicroserviceException extends  RuntimeException {

    @Getter
    private Integer statusCode;

    public ErroComunicacaoMicroserviceException(String msg, Integer statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }
}