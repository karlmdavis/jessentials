package com.justdavis.jessentials.exceptions;

/**
 * Wraps {@link InstantiationException}s in an unchecked exception.
 */
public final class UncheckedInstantiationException extends RuntimeException {
	private static final long serialVersionUID = 5442299705664676285L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedInstantiationException(InstantiationException e) {
		super(e);
	}
}
