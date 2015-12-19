package com.justdavis.karl.misc.datasources.provisioners.hsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.karl.misc.datasources.hsql.HsqlConnector;
import com.justdavis.karl.misc.datasources.hsql.HsqlCoordinates;

/**
 * Unit tests for {@link HsqlProvisioner}.
 */
public final class HsqlProvisionerTest {
	/**
	 * Verifies that
	 * {@link HsqlProvisioner#provision(HsqlProvisioningTarget, HsqlProvisioningRequest)}
	 * works as expected.
	 * 
	 * @throws SQLException
	 *             (would indicate a problem with the code under test)
	 */
	@Test
	public void provision() throws SQLException {
		// Create and run a provisioning request.
		HsqlProvisioner provisioner = new HsqlProvisioner();
		HsqlProvisioningTarget target = new HsqlProvisioningTarget();
		HsqlProvisioningRequest request = new HsqlProvisioningRequest("foo");
		HsqlCoordinates coords = provisioner.provision(target, request);
		Assert.assertNotNull(coords);

		// Create a DataSource
		HsqlConnector connector = new HsqlConnector();
		DataSource hsqlDataSource = connector.createDataSource(coords);
		Assert.assertNotNull(hsqlDataSource);

		/*
		 * Create and test a Connection. The query here is taken from
		 * http://stackoverflow.com/a/3670000/1851299.
		 */
		Connection hsqlConnection = null;
		try {
			hsqlConnection = hsqlDataSource.getConnection();
			Assert.assertNotNull(hsqlConnection);
			PreparedStatement statement = hsqlConnection
					.prepareStatement("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS");
			ResultSet resultSet = statement.executeQuery();
			Assert.assertTrue(resultSet.next());
			Assert.assertEquals(1, resultSet.getInt(1));
		} finally {
			if (hsqlConnection != null)
				hsqlConnection.close();
		}
	}

	/**
	 * Verifies that
	 * {@link HsqlProvisioner#delete(HsqlProvisioningTarget, HsqlProvisioningRequest)}
	 * works as expected.
	 * 
	 * @throws SQLException
	 *             (would indicate a problem with the code under test)
	 */
	@Test
	public void delete() throws SQLException {
		// Create and run a provisioning request.
		HsqlProvisioner provisioner = new HsqlProvisioner();
		HsqlProvisioningTarget target = new HsqlProvisioningTarget();
		HsqlProvisioningRequest request = new HsqlProvisioningRequest("foo");
		HsqlCoordinates coords = provisioner.provision(target, request);
		HsqlConnector connector = new HsqlConnector();
		DataSource hsqlDataSource = connector.createDataSource(coords);

		/*
		 * Create a table in the database, just so there's something there that
		 * we can verify has been removed later.
		 */
		Connection hsqlConnection = null;
		try {
			hsqlConnection = hsqlDataSource.getConnection();
			Assert.assertNotNull(hsqlConnection);
			PreparedStatement statement = hsqlConnection
					.prepareStatement("CREATE TABLE foo ( id int NOT NULL, PRIMARY KEY (id) );");
			statement.execute();
		} finally {
			if (hsqlConnection != null)
				hsqlConnection.close();
		}

		// Delete the provisioned database.
		provisioner.delete(target, request);

		/*
		 * HSQL's in-memory databases are created automatically at
		 * connection-time, so we can't just verify that attempting to connect
		 * to the database fails. Instead, we'll verify that the table created
		 * earlier is gone.
		 */
		try {
			hsqlConnection = hsqlDataSource.getConnection();
			Assert.assertNotNull(hsqlConnection);
			PreparedStatement statement = hsqlConnection.prepareStatement(
					"SELECT TABLE_NAME" + " FROM INFORMATION_SCHEMA.TABLES" + " WHERE TABLE_NAME = 'foo';");
			ResultSet tablesResult = statement.executeQuery();
			Assert.assertFalse(tablesResult.next());
		} finally {
			if (hsqlConnection != null)
				hsqlConnection.close();
		}
	}
}
