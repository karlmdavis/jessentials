package com.justdavis.karl.misc.exceptions.unchecked;

import java.sql.SQLException;

/**
 * Wraps {@link SQLException}s in an unchecked exception.
 */
public final class UncheckedSqlException extends RuntimeException {
	private static final long serialVersionUID = -2919304841974396159L;

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            the checked exception to wrap
	 */
	public UncheckedSqlException(SQLException e) {
		super(e);
	}
}
