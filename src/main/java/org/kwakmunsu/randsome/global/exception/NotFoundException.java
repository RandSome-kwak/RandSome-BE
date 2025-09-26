package org.kwakmunsu.randsome.global.exception;

import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

public class NotFoundException extends RootException {

    public NotFoundException(ErrorStatus status) {
        super(status);
    }

    public NotFoundException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}