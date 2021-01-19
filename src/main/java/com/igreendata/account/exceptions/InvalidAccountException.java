package com.igreendata.account.exceptions;

/**
 *
 * @author Joby Job
 *
 */
public class InvalidAccountException extends IGreenDataAccountException {

	public InvalidAccountException(String message) {
		super(message);
	}

	public InvalidAccountException(String message, Throwable t) {
		super(message, t);
	}
}
