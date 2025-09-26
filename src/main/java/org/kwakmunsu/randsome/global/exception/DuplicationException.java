package org.kwakmunsu.randsome.global.exception;

import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

public class DuplicationException extends RootException {

    public DuplicationException(ErrorStatus status) {
        super(status);
    }

    public DuplicationException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}