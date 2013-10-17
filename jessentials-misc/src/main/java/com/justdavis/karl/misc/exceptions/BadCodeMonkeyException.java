package com.justdavis.karl.misc.exceptions;

/**
 * <p>
 * <strong>Bad code monkey! No Doritos&reg; for you!</strong>
 * </p>
 * <p>
 * This exception should be used to indicate a programmer error: throw it when a
 * condition has been encountered that should not have been encountered unless
 * the author of that code (i.e. you, the Code Monkey) made a mistake somewhere.
 * </p>
 */
public final class BadCodeMonkeyException extends RuntimeException {
	private static final long serialVersionUID = 6764860303725144657L;

	/**
	 * Constructs a new {@link BadCodeMonkeyException}.
	 */
	public BadCodeMonkeyException() {
		super();
	}

	/**
	 * Constructs a new {@link BadCodeMonkeyException}.
	 * 
	 * @param message
	 *            the value to use for {@link #getMessage()}
	 * @param cause
	 *            the value to use for {@link #getCause()}
	 */
	public BadCodeMonkeyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new {@link BadCodeMonkeyException}.
	 * 
	 * @param message
	 *            the value to use for {@link #getMessage()}
	 */
	public BadCodeMonkeyException(String message) {
		super(message);
	}

	/**
	 * Constructs a new {@link BadCodeMonkeyException}.
	 * 
	 * @param cause
	 *            the value to use for {@link #getCause()}
	 */
	public BadCodeMonkeyException(Throwable cause) {
		super(cause);
	}
}
