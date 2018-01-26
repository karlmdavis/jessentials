package com.justdavis.karl.misc.datasources.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.justdavis.karl.misc.datasources.DataSourceConnectorsManager;
import com.justdavis.karl.misc.datasources.IDataSourceCoordinates;
import com.justdavis.karl.misc.datasources.hsql.HsqlConnector;
import com.justdavis.karl.misc.datasources.postgresql.PostgreSqlConnector;
import com.justdavis.karl.misc.datasources.provisioners.DataSourceProvisionersManager;
import com.justdavis.karl.misc.datasources.provisioners.DataSourceProvisionersManager.ProvisioningResult;
import com.justdavis.karl.misc.datasources.provisioners.IProvisioningRequest;
import com.justdavis.karl.misc.datasources.provisioners.IProvisioningTargetsProvider;
import com.justdavis.karl.misc.datasources.provisioners.XmlProvisioningTargetsProvider;
import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioner;
import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioningRequest;
import com.justdavis.karl.misc.datasources.provisioners.postgresql.PostgreSqlProvisioner;
import com.justdavis.karl.misc.datasources.provisioners.postgresql.PostgreSqlProvisioningRequest;

/**
 * <p>
 * Integration tests for {@link LiquibaseSchemaManager}.
 * </p>
 */
@RunWith(Parameterized.class)
public final class LiquibaseSchemaManagerIT {
	/**
	 * @return the test run parameters to pass to
	 *         {@link #LiquibaseSchemaManagerIT(IProvisioningRequest)}, where
	 *         each top-level element in the returned {@link Collection}
	 *         represents a test run
	 */
	@Parameterized.Parameters(name = "{index}: IProvisioningRequest={0}")
	public static Collection<Object[]> createTestParameters() {
		Collection<Object[]> testParameters = new LinkedList<Object[]>();

		IProvisioningRequest hsqlRequest = new HsqlProvisioningRequest("integrationtest");
		testParameters.add(new Object[] { hsqlRequest });

		IProvisioningRequest postgreSqlRequest = new PostgreSqlProvisioningRequest("integrationtest");
		testParameters.add(new Object[] { postgreSqlRequest });

		return testParameters;
	}

	private final IProvisioningRequest provisioningRequest;

	/**
	 * Constructs a new {@link LiquibaseSchemaManagerIT} instance. The test
	 * runner will generate the parameters to pass to this from the
	 * {@link #createTestParameters()} method.
	 * 
	 * @param provisioningRequest
	 *            the {@link IProvisioningRequest} parameter for the test run
	 */
	public LiquibaseSchemaManagerIT(IProvisioningRequest provisioningRequest) {
		this.provisioningRequest = provisioningRequest;
	}

	/**
	 * Tests
	 * {@link LiquibaseSchemaManager#createOrUpgradeSchema(IDataSourceCoordinates)}
	 * .
	 * 
	 * @throws SQLException
	 *             (might indicate a test failure)
	 */
	@Test
	public void createOrUpgradeSchema() throws SQLException {
		// Provision the DB to run against.
		@SuppressWarnings("unchecked")
		DataSourceProvisionersManager provisionersManager = new DataSourceProvisionersManager(new HsqlProvisioner(),
				new PostgreSqlProvisioner());
		IProvisioningTargetsProvider targetsProvider = new XmlProvisioningTargetsProvider(provisionersManager,
				Thread.currentThread().getContextClassLoader().getResource("datasource-provisioning-targets.xml"));
		ProvisioningResult provisioningResult = provisionersManager.provision(targetsProvider, provisioningRequest);

		try {
			// Create the LiquibaseSchemaManager.
			DataSourceConnectorsManager connectorsManager = new DataSourceConnectorsManager(new HsqlConnector(),
					new PostgreSqlConnector());
			LiquibaseSchemaManager schemaManager = new LiquibaseSchemaManager(connectorsManager,
					"sample-xml/liquibase-change-log-1.xml");

			// Run the schema manager.
			schemaManager.createOrUpgradeSchema(provisioningResult.getCoords());

			/*
			 * That should have created a "Test" table with an "id" column.
			 * Verify that.
			 */
			Connection connection = null;
			try {
				connection = connectorsManager.createDataSource(provisioningResult.getCoords()).getConnection();
				ResultSet columns = connection.getMetaData().getColumns(null, null, "Test", "id");
				Assert.assertTrue(columns.next());
				Assert.assertEquals("id", columns.getString("COLUMN_NAME"));
				Assert.assertEquals(Types.INTEGER, columns.getInt("DATA_TYPE"));
				Assert.assertFalse(columns.next());
				columns.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		} finally {
			provisionersManager.delete(provisioningResult);
		}
	}

	/**
	 * Tests {@link LiquibaseSchemaManager#wipeSchema(IDataSourceCoordinates)}.
	 * 
	 * @throws SQLException
	 *             (might indicate a test failure)
	 */
	@Test
	public void wipeSchema() throws SQLException {
		// Provision the DB to run against.
		@SuppressWarnings("unchecked")
		DataSourceProvisionersManager provisionersManager = new DataSourceProvisionersManager(new HsqlProvisioner(),
				new PostgreSqlProvisioner());
		IProvisioningTargetsProvider targetsProvider = new XmlProvisioningTargetsProvider(provisionersManager,
				Thread.currentThread().getContextClassLoader().getResource("datasource-provisioning-targets.xml"));
		ProvisioningResult provisioningResult = provisionersManager.provision(targetsProvider, provisioningRequest);

		try {
			// Create the LiquibaseSchemaManager.
			DataSourceConnectorsManager connectorsManager = new DataSourceConnectorsManager(new HsqlConnector(),
					new PostgreSqlConnector());
			LiquibaseSchemaManager schemaManager = new LiquibaseSchemaManager(connectorsManager,
					"sample-xml/liquibase-change-log-1.xml");

			// Run the schema manager: create and then wipe the schema.
			schemaManager.createOrUpgradeSchema(provisioningResult.getCoords());
			schemaManager.wipeSchema(provisioningResult.getCoords());

			/*
			 * When the schema was created, a "Test" table would have been
			 * created. Make sure it's not there.
			 */
			Connection connection = null;
			try {
				connection = connectorsManager.createDataSource(provisioningResult.getCoords()).getConnection();
				ResultSet tables = connection.getMetaData().getTables(null, null, "Test", null);
				Assert.assertFalse(tables.next());
				tables.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		} finally {
			provisionersManager.delete(provisioningResult);
		}
	}
}
