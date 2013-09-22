package com.justdavis.jessentials.exceptions;

/**
 * Wraps {@link NoSuchMethodException}s in an unchecked exception.
 */
public final class UncheckedNoSuchMethodException extends RuntimeException {
	private static final long serialVersionUID = -5427033094014512377L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedNoSuchMethodException(NoSuchMethodException e) {
		super(e);
	}
}
