package org.kwakmunsu.randsome.global.exception;

import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

public class UnAuthenticationException extends RootException {

    public UnAuthenticationException(ErrorStatus status) {
        super(status);
    }

    public UnAuthenticationException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}