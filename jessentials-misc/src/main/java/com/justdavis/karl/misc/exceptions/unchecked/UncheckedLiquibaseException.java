package com.justdavis.karl.misc.exceptions.unchecked;

import liquibase.exception.LiquibaseException;

/**
 * Wraps {@link LiquibaseException}s in an unchecked exception.
 */
public final class UncheckedLiquibaseException extends RuntimeException {
	private static final long serialVersionUID = -6541092897798187064L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedLiquibaseException(LiquibaseException e) {
		super(e);
	}
}
