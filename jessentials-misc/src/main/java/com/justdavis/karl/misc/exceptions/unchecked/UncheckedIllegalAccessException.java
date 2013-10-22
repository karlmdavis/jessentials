package com.justdavis.karl.misc.exceptions.unchecked;

/**
 * Wraps {@link IllegalAccessException}s in an unchecked exception.
 */
public final class UncheckedIllegalAccessException extends RuntimeException {
	private static final long serialVersionUID = -1877741863798605019L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedIllegalAccessException(IllegalAccessException e) {
		super(e);
	}
}
