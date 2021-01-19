package com.igreendata.account.exceptions;

/**
 *
 * @author Joby Job
 *
 */
@SuppressWarnings("serial")
public class InvalidCurrencyException extends IGreenDataAccountException {

	public InvalidCurrencyException(String message) {
		super(message);
	}

	public InvalidCurrencyException(String message, Throwable t) {
		super(message, t);
	}
}
