package com.justdavis.karl.misc.datasources.postgresql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.postgresql.Driver;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.stereotype.Component;

import com.justdavis.karl.misc.datasources.IDataSourceConnector;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedSqlException;

/**
 * This {@link IDataSourceConnector} implementation converts
 * {@link PostgreSqlCoordinates} instance into JDBC {@link DataSource}s, for <a
 * href="http://www.postgresql.org/">PostgreSQL</a> databases.
 * 
 * @see PostgreSqlCoordinates
 */
@Component
public final class PostgreSqlConnector implements
		IDataSourceConnector<PostgreSqlCoordinates> {
	/**
	 * @see com.justdavis.karl.misc.datasources.IDataSourceConnector#getCoordinatesType()
	 */
	@Override
	public Class<PostgreSqlCoordinates> getCoordinatesType() {
		return PostgreSqlCoordinates.class;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.IDataSourceConnector#createDataSource(com.justdavis.karl.misc.datasources.IDataSourceCoordinates)
	 */
	@Override
	public DataSource createDataSource(PostgreSqlCoordinates coords)
			throws UncheckedSqlException {
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		try {
			dataSource.setUrl(coords.getUrl());
			dataSource.setServerName(null);
			if (coords.getUser() != null)
				dataSource.setUser(coords.getUser());
			if (coords.getPassword() != null)
				dataSource.setPassword(coords.getPassword());
		} catch (SQLException e) {
			throw new UncheckedSqlException(e);
		}
		return dataSource;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.IDataSourceConnector#convertToJpaProperties(com.justdavis.karl.misc.datasources.IDataSourceCoordinates)
	 */
	@Override
	public Map<String, Object> convertToJpaProperties(
			PostgreSqlCoordinates coords) {
		Map<String, Object> jpaCoords = new HashMap<String, Object>();
		jpaCoords.put(JPA_JDBC_DRIVER, Driver.class.getName());
		jpaCoords.put(JPA_JDBC_URL, coords.getUrl());
		if (coords.getUser() != null)
			jpaCoords.put(JPA_JDBC_USER, coords.getUser());
		if (coords.getPassword() != null)
			jpaCoords.put(JPA_JDBC_PASSWORD, coords.getPassword());
		return jpaCoords;
	}
}
