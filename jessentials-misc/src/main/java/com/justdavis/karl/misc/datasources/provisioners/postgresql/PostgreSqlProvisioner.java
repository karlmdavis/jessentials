package com.justdavis.karl.misc.datasources.provisioners.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.justdavis.karl.misc.datasources.postgresql.PostgreSqlConnector;
import com.justdavis.karl.misc.datasources.postgresql.PostgreSqlCoordinates;
import com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedSqlException;

/**
 * <p>
 * An {@link IDataSourceProvisioner} implementation for
 * {@link PostgreSqlConnector} data sources.
 * </p>
 * <p>
 * <strong>Please note:</strong> Only in-memory, local-JVM-only databases are
 * currently supported.
 * </p>
 */
@Component
public final class PostgreSqlProvisioner
		implements
		IDataSourceProvisioner<PostgreSqlCoordinates, PostgreSqlProvisioningTarget, PostgreSqlProvisioningRequest> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PostgreSqlProvisioner.class);

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#getTargetType()
	 */
	@Override
	public Class<PostgreSqlProvisioningTarget> getTargetType() {
		return PostgreSqlProvisioningTarget.class;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#getRequestType()
	 */
	@Override
	public Class<PostgreSqlProvisioningRequest> getRequestType() {
		return PostgreSqlProvisioningRequest.class;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#provision(com.justdavis.karl.misc.datasources.provisioners.IProvisioningTarget,
	 *      com.justdavis.karl.misc.datasources.provisioners.IProvisioningRequest)
	 */
	@Override
	public PostgreSqlCoordinates provision(PostgreSqlProvisioningTarget target,
			PostgreSqlProvisioningRequest request) {
		if (target == null)
			throw new IllegalArgumentException();
		if (request == null)
			throw new IllegalArgumentException();

		/*
		 * Connect to the postgres database (from the target) and run the CREATE
		 * DATABASE command there.
		 */
		PostgreSqlConnector connector = new PostgreSqlConnector();
		DataSource dataSource = connector.createDataSource(target
				.getServerCoords());
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(String
					.format("CREATE DATABASE %s;", request.getDatabaseName()));
			statement.execute();
		} catch (SQLException e) {
			throw new UncheckedSqlException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new UncheckedSqlException(e);
				}
			}
		}

		PostgreSqlCoordinates coords = new PostgreSqlCoordinates(
				target.getServerCoords(), request.getDatabaseName());
		LOGGER.info("Provisioned PostgreSQL database: {}", coords);
		return coords;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#delete(com.justdavis.karl.misc.datasources.provisioners.IProvisioningTarget,
	 *      com.justdavis.karl.misc.datasources.provisioners.IProvisioningRequest)
	 */
	@Override
	public void delete(PostgreSqlProvisioningTarget target,
			PostgreSqlProvisioningRequest request) {
		if (target == null)
			throw new IllegalArgumentException();
		if (request == null)
			throw new IllegalArgumentException();

		/*
		 * Connect to the postgres database (from the target) and run the DROP
		 * DATABASE command there.
		 */
		PostgreSqlConnector connector = new PostgreSqlConnector();
		DataSource dataSource = connector.createDataSource(target
				.getServerCoords());
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(String
					.format("DROP DATABASE %s;", request.getDatabaseName()));
			statement.execute();

			LOGGER.info("Deleted PostgreSQL database: {}",
					request.getDatabaseName());
		} catch (SQLException e) {
			throw new UncheckedSqlException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new UncheckedSqlException(e);
				}
			}
		}
	}
}
