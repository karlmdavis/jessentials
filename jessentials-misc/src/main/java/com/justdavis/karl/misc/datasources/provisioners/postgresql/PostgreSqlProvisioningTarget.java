package com.justdavis.karl.misc.datasources.provisioners.postgresql;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.justdavis.karl.misc.datasources.postgresql.PostgreSqlCoordinates;
import com.justdavis.karl.misc.datasources.provisioners.IProvisioningTarget;

/**
 * <p>
 * The {@link IProvisioningTarget} implementation for
 * {@link PostgreSqlProvisioner}.
 * </p>
 */
@XmlRootElement
public final class PostgreSqlProvisioningTarget extends IProvisioningTarget {
	@XmlElement
	private final PostgreSqlCoordinates serverCoords;

	/**
	 * This no-arg default constructor is required by JAX-B.
	 */
	@SuppressWarnings("unused")
	private PostgreSqlProvisioningTarget() {
		this.serverCoords = null;
	}

	/**
	 * Constructs a new {@link PostgreSqlProvisioningTarget} instance.
	 * 
	 * @param serverCoords
	 *            the value to use for {@link #getServerCoords()}
	 */
	public PostgreSqlProvisioningTarget(PostgreSqlCoordinates serverCoords) {
		if (serverCoords == null)
			throw new IllegalArgumentException();

		this.serverCoords = serverCoords;
	}

	/**
	 * @return Returns the {@link PostgreSqlCoordinates} for a database
	 *         (typically the " <code>postgres</code>" database) on the
	 *         PostgreSQL server to provision a new database on. This connection
	 *         must be for a user with the privileges necessary to create and
	 *         drop databases.
	 */
	public PostgreSqlCoordinates getServerCoords() {
		return serverCoords;
	}
}
