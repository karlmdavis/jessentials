package com.justdavis.karl.misc.datasources.provisioners.postgresql;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.karl.misc.datasources.postgresql.PostgreSqlConnector;
import com.justdavis.karl.misc.datasources.postgresql.PostgreSqlCoordinates;
import com.justdavis.karl.misc.datasources.provisioners.DataSourceProvisionersManager;
import com.justdavis.karl.misc.datasources.provisioners.IProvisioningTargetsProvider;
import com.justdavis.karl.misc.datasources.provisioners.XmlProvisioningTargetsProvider;

/**
 * Integration tests for {@link PostgreSqlProvisioner}.
 */
public final class PostgreSqlProvisionerIT {
	/**
	 * Verifies that
	 * {@link PostgreSqlProvisioner#provision(PostgreSqlProvisioningTarget, PostgreSqlProvisioningRequest)}
	 * works as expected.
	 * 
	 * @throws SQLException
	 *             (would indicate a problem with the code under test)
	 */
	@Test
	public void provision() throws SQLException {
		// Find the available provisioning target.
		@SuppressWarnings("unchecked")
		DataSourceProvisionersManager provisionersManager = new DataSourceProvisionersManager(
				new PostgreSqlProvisioner());
		URL availableTargetsUrl = Thread.currentThread()
				.getContextClassLoader()
				.getResource("datasource-provisioning-targets.xml");
		IProvisioningTargetsProvider targetsProvider = new XmlProvisioningTargetsProvider(
				provisionersManager, availableTargetsUrl);
		PostgreSqlProvisioningTarget target = targetsProvider
				.findTarget(PostgreSqlProvisioningTarget.class);

		// Create and run a provisioning request.
		PostgreSqlProvisioner provisioner = new PostgreSqlProvisioner();
		PostgreSqlProvisioningRequest request = new PostgreSqlProvisioningRequest(
				"integrationtest");
		try {
			PostgreSqlCoordinates provisionedCoords = provisioner.provision(
					target, request);
			Assert.assertNotNull(provisionedCoords);

			// Create a DataSource
			PostgreSqlConnector connector = new PostgreSqlConnector();
			DataSource postgreSqlDataSource = connector
					.createDataSource(provisionedCoords);
			Assert.assertNotNull(postgreSqlDataSource);

			/*
			 * Create and test a Connection. The query here is taken from
			 * http://stackoverflow.com/a/3670000/1851299.
			 */
			Connection hsqlConnection = null;
			try {
				hsqlConnection = postgreSqlDataSource.getConnection();
				Assert.assertNotNull(hsqlConnection);
				PreparedStatement statement = hsqlConnection
						.prepareStatement("SELECT 1");
				ResultSet resultSet = statement.executeQuery();
				Assert.assertTrue(resultSet.next());
				Assert.assertEquals(1, resultSet.getInt(1));
			} finally {
				if (hsqlConnection != null)
					hsqlConnection.close();
			}
		} finally {
			provisioner.delete(target, request);
		}
	}

	/**
	 * Verifies that
	 * {@link PostgreSqlProvisioner#delete(PostgreSqlProvisioningTarget, PostgreSqlProvisioningRequest)}
	 * works as expected.
	 * 
	 * @throws SQLException
	 *             (would indicate a problem with the code under test)
	 */
	@Test
	public void delete() throws SQLException {
		// Find the available provisioning target.
		@SuppressWarnings("unchecked")
		DataSourceProvisionersManager provisionersManager = new DataSourceProvisionersManager(
				new PostgreSqlProvisioner());
		URL availableTargetsUrl = Thread.currentThread()
				.getContextClassLoader()
				.getResource("datasource-provisioning-targets.xml");
		IProvisioningTargetsProvider targetsProvider = new XmlProvisioningTargetsProvider(
				provisionersManager, availableTargetsUrl);
		PostgreSqlProvisioningTarget target = targetsProvider
				.findTarget(PostgreSqlProvisioningTarget.class);

		// Create and run a provisioning request.
		PostgreSqlProvisioner provisioner = new PostgreSqlProvisioner();
		PostgreSqlProvisioningRequest request = new PostgreSqlProvisioningRequest(
				"IntegrationTest");
		provisioner.provision(target, request);

		// Delete the provisioned database.
		provisioner.delete(target, request);

		/*
		 * Run a query on the server to see if the database still exists.
		 */
		PostgreSqlConnector connector = new PostgreSqlConnector();
		DataSource postgreSqlDataSource = connector.createDataSource(target
				.getServerCoords());
		Connection postgreSqlConnection = null;
		try {
			postgreSqlConnection = postgreSqlDataSource.getConnection();
			Assert.assertNotNull(postgreSqlConnection);
			PreparedStatement statement = postgreSqlConnection
					.prepareStatement("SELECT datname" + " FROM pg_database"
							+ " WHERE datname = 'IntegrationTest';");
			ResultSet dbsResult = statement.executeQuery();
			Assert.assertFalse(dbsResult.next());
		} finally {
			if (postgreSqlConnection != null)
				postgreSqlConnection.close();
		}
	}
}
