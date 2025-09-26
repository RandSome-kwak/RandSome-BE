package org.kwakmunsu.randsome.global.exception;


import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

public class BadRequestException extends RootException {

    public BadRequestException(ErrorStatus status) {
        super(status);
    }

    public BadRequestException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}