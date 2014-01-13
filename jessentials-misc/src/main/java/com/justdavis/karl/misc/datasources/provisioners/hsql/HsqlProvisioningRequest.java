package com.justdavis.karl.misc.datasources.provisioners.hsql;

import com.justdavis.karl.misc.datasources.provisioners.IProvisioningRequest;

/**
 * <p>
 * The {@link IProvisioningRequest} implementation for {@link HsqlProvisioner}.
 * </p>
 */
public final class HsqlProvisioningRequest implements IProvisioningRequest {
	private final String databaseName;

	/**
	 * Constructs a new {@link HsqlProvisioningRequest} instance.
	 * 
	 * @param databaseName
	 *            the value to use for {@link #getDatabaseName()}
	 */
	public HsqlProvisioningRequest(String databaseName) {
		if (databaseName == null)
			throw new IllegalArgumentException();
		if (databaseName.isEmpty())
			throw new IllegalArgumentException();

		this.databaseName = databaseName;
	}

	/**
	 * @return the name of the in-memory database that will be created
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HsqlProvisioningRequest [databaseName=" + databaseName + "]";
	}
}
