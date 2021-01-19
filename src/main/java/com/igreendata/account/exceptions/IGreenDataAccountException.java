package com.igreendata.account.exceptions;

@SuppressWarnings("serial")
public class IGreenDataAccountException extends RuntimeException {

    public IGreenDataAccountException(String message) {
        super(message);
    }

    public IGreenDataAccountException(String message, Throwable t) {
        super(message, t);
    }
}
