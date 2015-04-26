package com.justdavis.karl.misc.datasources.hsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import liquibase.database.Database;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.jvm.HsqlConnection;

import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.stereotype.Component;

import com.justdavis.karl.misc.datasources.IDataSourceConnector;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedSqlException;

/**
 * This {@link IDataSourceConnector} implementation converts
 * {@link HsqlCoordinates} instance into JDBC {@link DataSource}s, for <a
 * href="http://hsqldb.org/">HSQL</a> databases.
 * 
 * @see HsqlCoordinates
 */
@Component
public final class HsqlConnector implements
		IDataSourceConnector<HsqlCoordinates> {
	/**
	 * @see com.justdavis.karl.misc.datasources.IDataSourceConnector#getCoordinatesType()
	 */
	@Override
	public Class<HsqlCoordinates> getCoordinatesType() {
		return HsqlCoordinates.class;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.IDataSourceConnector#createDataSource(com.justdavis.karl.misc.datasources.IDataSourceCoordinates)
	 */
	@Override
	public DataSource createDataSource(HsqlCoordinates coords)
			throws UncheckedSqlException {
		JDBCDataSource dataSource = new JDBCDataSource();
		dataSource.setUrl(coords.getUrl());
		return dataSource;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.IDataSourceConnector#convertToJpaProperties(com.justdavis.karl.misc.datasources.IDataSourceCoordinates)
	 */
	@Override
	public Map<String, Object> convertToJpaProperties(HsqlCoordinates coords) {
		Map<String, Object> jpaCoords = new HashMap<String, Object>();
		jpaCoords.put(JPA_JDBC_DRIVER, JDBCDriver.class.getName());
		jpaCoords.put(JPA_JDBC_URL, coords.getUrl());
		return jpaCoords;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.IDataSourceConnector#convertToLiquibaseConnection(com.justdavis.karl.misc.datasources.IDataSourceCoordinates)
	 */
	@Override
	public Database convertToLiquibaseConnection(HsqlCoordinates coords) {
		DataSource jdbcDataSource = createDataSource(coords);
		Connection jdbcConnection;
		try {
			jdbcConnection = jdbcDataSource.getConnection();
		} catch (SQLException e) {
			throw new UncheckedSqlException(e);
		}

		HsqlDatabase liquibaseDb = new HsqlDatabase();
		HsqlConnection liquibaseConnection = new HsqlConnection(jdbcConnection);
		liquibaseDb.setConnection(liquibaseConnection);

		return liquibaseDb;
	}

	/**
	 * Note: Just leaving this here for future debugging. Very useful when you
	 * want to peek at an in-memory HSQL database.
	 * 
	 * @param coords
	 *            the {@link HsqlCoordinates} of the database to launch the
	 *            manager GUI for
	 */
	public void startHsqlManagerGui(HsqlCoordinates coords) {
		org.hsqldb.util.DatabaseManagerSwing.main(new String[] { "--url",
				coords.getUrl(), "--noexit" });
	}
}
