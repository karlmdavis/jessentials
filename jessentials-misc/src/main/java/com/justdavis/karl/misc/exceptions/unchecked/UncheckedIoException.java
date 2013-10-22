package com.justdavis.karl.misc.exceptions.unchecked;

import java.io.IOException;

/**
 * Wraps {@link IOException}s in an unchecked exception.
 */
public final class UncheckedIoException extends RuntimeException {
	private static final long serialVersionUID = -7495391180527726679L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedIoException(IOException e) {
		super(e);
	}
}
