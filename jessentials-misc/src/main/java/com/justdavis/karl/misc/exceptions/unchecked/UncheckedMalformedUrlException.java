package com.justdavis.karl.misc.exceptions.unchecked;

import java.net.MalformedURLException;

/**
 * Wraps {@link MalformedURLException}s in an unchecked exception.
 */
public final class UncheckedMalformedUrlException extends RuntimeException {
	private static final long serialVersionUID = 647026626695906557L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedMalformedUrlException(MalformedURLException e) {
		super(e);
	}
}
