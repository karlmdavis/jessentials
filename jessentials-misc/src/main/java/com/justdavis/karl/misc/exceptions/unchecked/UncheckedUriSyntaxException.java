package com.justdavis.karl.misc.exceptions.unchecked;

import java.net.URISyntaxException;

/**
 * Wraps {@link URISyntaxException}s in an unchecked exception.
 */
public final class UncheckedUriSyntaxException extends RuntimeException {
	private static final long serialVersionUID = 6858431853600868382L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedUriSyntaxException(URISyntaxException e) {
		super(e);
	}
}
