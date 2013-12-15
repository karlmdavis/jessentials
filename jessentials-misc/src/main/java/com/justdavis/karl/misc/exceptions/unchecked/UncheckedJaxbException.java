package com.justdavis.karl.misc.exceptions.unchecked;

import javax.xml.bind.JAXBException;

/**
 * Wraps {@link JAXBException}s in an unchecked exception.
 */
public final class UncheckedJaxbException extends RuntimeException {
	private static final long serialVersionUID = 5504143463029453810L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedJaxbException(JAXBException e) {
		super(e);
	}
}
