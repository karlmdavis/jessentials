package com.justdavis.karl.misc.datasources.provisioners.postgresql;

import com.justdavis.karl.misc.datasources.provisioners.IProvisioningRequest;

/**
 * <p>
 * The {@link IProvisioningRequest} implementation for
 * {@link PostgreSqlProvisioner}.
 * </p>
 * <p>
 * The options here are basically those available via PostgreSQL's <a href=
 * "http://www.postgresql.org/docs/9.1/static/sql-createdatabase.html">CREATE
 * DATABASE</a> command. However, not all of the options there are (yet)
 * supported here.
 * </p>
 */
public final class PostgreSqlProvisioningRequest implements
		IProvisioningRequest {
	private final String databaseName;

	/**
	 * Constructs a new {@link PostgreSqlProvisioningRequest} instance.
	 * 
	 * @param databaseName
	 *            the value to use for {@link #getDatabaseName()}
	 */
	public PostgreSqlProvisioningRequest(String databaseName) {
		if (databaseName == null)
			throw new IllegalArgumentException();
		if (databaseName.isEmpty())
			throw new IllegalArgumentException();

		this.databaseName = databaseName;
	}

	/**
	 * @return the name of the database that will be created
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PostgreSqlProvisioningRequest [databaseName=" + databaseName
				+ "]";
	}
}
