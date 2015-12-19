package com.justdavis.karl.misc.datasources.provisioners.hsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.justdavis.karl.misc.datasources.hsql.HsqlConnector;
import com.justdavis.karl.misc.datasources.hsql.HsqlCoordinates;
import com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedSqlException;

/**
 * <p>
 * An {@link IDataSourceProvisioner} implementation for {@link HsqlConnector}
 * data sources.
 * </p>
 * <p>
 * <strong>Please note:</strong> Only in-memory, local-JVM-only databases are
 * currently supported.
 * </p>
 */
@Component
public final class HsqlProvisioner
		implements IDataSourceProvisioner<HsqlCoordinates, HsqlProvisioningTarget, HsqlProvisioningRequest> {
	private static final Logger LOGGER = LoggerFactory.getLogger(HsqlProvisioner.class);

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#getTargetType()
	 */
	@Override
	public Class<HsqlProvisioningTarget> getTargetType() {
		return HsqlProvisioningTarget.class;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#getRequestType()
	 */
	@Override
	public Class<HsqlProvisioningRequest> getRequestType() {
		return HsqlProvisioningRequest.class;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#provision(com.justdavis.karl.misc.datasources.provisioners.IProvisioningTarget,
	 *      com.justdavis.karl.misc.datasources.provisioners.IProvisioningRequest)
	 */
	@Override
	public HsqlCoordinates provision(HsqlProvisioningTarget target, HsqlProvisioningRequest request) {
		if (request == null)
			throw new IllegalArgumentException();

		/*
		 * All we have to do here is create a DataSource for the desired DB and
		 * connect to it once (to verify it's working).
		 */
		HsqlCoordinates coords = new HsqlCoordinates(String.format("jdbc:hsqldb:mem:%s", request.getDatabaseName()));

		HsqlConnector connector = new HsqlConnector();
		DataSource dataSource = connector.createDataSource(coords);

		/*
		 * Create and test a Connection. The query here is taken from
		 * http://stackoverflow.com/a/3670000/1851299.
		 */
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS");
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			int returnedInt = resultSet.getInt(1);
			if (returnedInt != 1)
				throw new IllegalStateException("Database connection not working correctly.");
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

		LOGGER.info("Provisioned HSQL database: {}", coords);
		return coords;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner#delete(com.justdavis.karl.misc.datasources.provisioners.IProvisioningTarget,
	 *      com.justdavis.karl.misc.datasources.provisioners.IProvisioningRequest)
	 */
	@Override
	public void delete(HsqlProvisioningTarget target, HsqlProvisioningRequest request) {
		if (request == null)
			throw new IllegalArgumentException();

		HsqlCoordinates coords = new HsqlCoordinates("jdbc:hsqldb:mem:" + request.getDatabaseName());
		HsqlConnector connector = new HsqlConnector();
		DataSource dataSource = connector.createDataSource(coords);

		/*
		 * Create a Connection, use it to execute the HSQL "SHUTDOWN" command.
		 */
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement("SHUTDOWN");
			statement.execute();

			LOGGER.info("Deleted HSQL database: {}", coords);
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
