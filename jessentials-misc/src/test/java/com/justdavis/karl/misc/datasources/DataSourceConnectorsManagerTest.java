package com.justdavis.karl.misc.datasources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Assert;
import org.junit.Test;

import com.justdavis.karl.misc.datasources.hsql.HsqlConnector;
import com.justdavis.karl.misc.datasources.hsql.HsqlCoordinates;

/**
 * Unit tests for {@link DataSourceConnectorsManager}.
 */
public final class DataSourceConnectorsManagerTest {
	/**
	 * Ensures that {@link DataSourceConnectorsManager#getCoordinatesTypes()}
	 * works as expected.
	 */
	@Test
	public void getCoordinatesTypes() {
		DataSourceConnectorsManager connectorsManager = new DataSourceConnectorsManager(
				new HashSet<IDataSourceConnector<? extends IDataSourceCoordinates>>(
						Arrays.asList(new HsqlConnector())));

		Assert.assertEquals(1, connectorsManager.getCoordinatesTypes().size());
		Assert.assertEquals(HsqlCoordinates.class, connectorsManager
				.getCoordinatesTypes().iterator().next());
	}

	/**
	 * Ensures that
	 * {@link DataSourceConnectorsManager#createDataSource(IDataSourceCoordinates)}
	 * works as expected.
	 */
	@Test
	public void createDataSource() {
		DataSourceConnectorsManager connectorsManager = new DataSourceConnectorsManager(
				new HashSet<IDataSourceConnector<? extends IDataSourceCoordinates>>(
						Arrays.asList(new HsqlConnector())));

		DataSource dataSource = connectorsManager
				.createDataSource(new HsqlCoordinates(
						"jdbc:hsqldb:mem:foo;shutdown=true"));
		Assert.assertTrue(dataSource instanceof JDBCDataSource);
	}

	/**
	 * Ensures that
	 * {@link DataSourceConnectorsManager#convertToJpaProperties(IDataSourceCoordinates)}
	 * works as expected.
	 */
	@Test
	public void convertToJpaProperties() {
		DataSourceConnectorsManager connectorsManager = new DataSourceConnectorsManager(
				new HashSet<IDataSourceConnector<? extends IDataSourceCoordinates>>(
						Arrays.asList(new HsqlConnector())));

		Map<String, Object> jpaProperties = connectorsManager
				.convertToJpaProperties(new HsqlCoordinates(
						"jdbc:hsqldb:mem:foo;shutdown=true"));
		Assert.assertNotNull(jpaProperties);
	}
}
