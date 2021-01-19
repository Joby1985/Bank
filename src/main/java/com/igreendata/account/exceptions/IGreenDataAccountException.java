package com.igreendata.account.exceptions;

/**
 *
 * @author Joby Job
 *
 */
@SuppressWarnings("serial")
public class IGreenDataAccountException extends RuntimeException {

	public IGreenDataAccountException(String message) {
		super(message);
	}

	public IGreenDataAccountException(String message, Throwable t) {
		super(message, t);
	}
}
