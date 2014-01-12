package com.justdavis.karl.misc.datasources.postgresql;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.postgresql.Driver;

import com.justdavis.karl.misc.datasources.IDataSourceConnector;

/**
 * Unit tests for {@link PostgreSqlConnector}.
 */
public final class PostgreSqlConnectorTest {
	/**
	 * Tests {@link PostgreSqlConnector#createDataSource(PostgreSqlCoordinates)}
	 * .
	 * 
	 * @throws SQLException
	 *             (this error indicates the test failed)
	 */
	@Test
	public void createDataSource() throws SQLException {
		PostgreSqlCoordinates coords = new PostgreSqlCoordinates(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true");
		PostgreSqlConnector connector = new PostgreSqlConnector();

		DataSource dataSource = connector.createDataSource(coords);
		Assert.assertNotNull(dataSource);

		/*
		 * As this is just a unit test, there's no guarantee that a PostgreSQL
		 * server is available, so we can't actually test the connection. A test
		 * of this functionality is covered by
		 * com.justdavis.karl.misc.datasources
		 * .provisioners.postgresql.PostgreSqlProvisionerIT.provision().
		 */
	}

	/**
	 * Tests
	 * {@link PostgreSqlConnector#convertToJpaProperties(PostgreSqlCoordinates)}
	 * .
	 */
	@Test
	public void convertToJpaProperties() {
		PostgreSqlCoordinates coords = new PostgreSqlCoordinates(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true");

		Map<String, Object> jpaCoords = new PostgreSqlConnector()
				.convertToJpaProperties(coords);
		Assert.assertNotNull(jpaCoords);
		Assert.assertEquals(2, jpaCoords.size());
		Assert.assertEquals(Driver.class.getName(),
				jpaCoords.get(IDataSourceConnector.JPA_JDBC_DRIVER));
		Assert.assertEquals(coords.getUrl(),
				jpaCoords.get(IDataSourceConnector.JPA_JDBC_URL));
	}
}
