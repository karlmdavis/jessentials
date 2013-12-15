package com.justdavis.karl.misc.datasources.hsql;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;
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
}
