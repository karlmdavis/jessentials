package com.justdavis.karl.misc.datasources.provisioners.hsql;

import java.util.Random;

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
	 * Constructs a new {@link HsqlProvisioningRequest} instance for a database
	 * with a semi-random name.
	 * 
	 * @param databaseNamePrefix
	 *            the prefix value to use for {@link #getDatabaseName()}
	 */
	public static HsqlProvisioningRequest requestForRandomDatabase(String databaseNamePrefix) {
		if (databaseNamePrefix == null)
			throw new IllegalArgumentException();
		if (databaseNamePrefix.isEmpty())
			throw new IllegalArgumentException();

		int randomSuffix = new Random().nextInt(1000000);
		return new HsqlProvisioningRequest(databaseNamePrefix + "_" + randomSuffix);
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
