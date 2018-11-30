package org.marcosoft.lib;

public class ValidatorException extends Exception {
	private static final long serialVersionUID = 4176521034293762881L;

	public ValidatorException() {
	}

	public ValidatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidatorException(String message) {
		super(message);
	}

	public ValidatorException(Throwable cause) {
		super(cause);
	}
}
