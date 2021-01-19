package com.igreendata.account.exceptions;

@SuppressWarnings("serial")
public class AccountTypeCannotChangeException extends IGreenDataAccountException {

    public AccountTypeCannotChangeException(String message) {
        super(message);
    }

    public AccountTypeCannotChangeException(String message, Throwable t) {
        super(message, t);
    }
}
