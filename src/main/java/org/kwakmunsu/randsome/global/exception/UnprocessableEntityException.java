package org.kwakmunsu.randsome.global.exception;

import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

public class UnprocessableEntityException extends RootException {

    public UnprocessableEntityException(ErrorStatus status) {
        super(status);
    }

    public UnprocessableEntityException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}