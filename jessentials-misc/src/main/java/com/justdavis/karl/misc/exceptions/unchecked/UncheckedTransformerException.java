package com.justdavis.karl.misc.exceptions.unchecked;

import javax.xml.transform.TransformerException;

/**
 * Wraps {@link TransformerException}s in an unchecked exception.
 */
public final class UncheckedTransformerException extends RuntimeException {
	private static final long serialVersionUID = 7604228517056636770L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedTransformerException(TransformerException e) {
		super(e);
	}
}
