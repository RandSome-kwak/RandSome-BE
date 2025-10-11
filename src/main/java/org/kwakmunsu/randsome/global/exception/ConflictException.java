package org.kwakmunsu.randsome.global.exception;

import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

public class ConflictException extends RootException {

    public ConflictException(ErrorStatus status) {
        super(status);
    }

    public ConflictException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}