package com.gonnichiwa.exception;

public class CEmailSigninFailedException extends RuntimeException {
	public CEmailSigninFailedException() {
		super();
	}

	public CEmailSigninFailedException(String msg) {
		super(msg);
	}

	public CEmailSigninFailedException(String msg, Throwable t) {
		super(msg, t);
	}
}
