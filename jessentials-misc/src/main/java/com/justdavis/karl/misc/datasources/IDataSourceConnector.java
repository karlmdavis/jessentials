package com.justdavis.karl.misc.datasources;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.justdavis.karl.misc.exceptions.unchecked.UncheckedSqlException;

import liquibase.database.Database;

/**
 * <p>
 * Provides functionality that's missing "out of the box" for JDBC
 * {@link DataSource} implementations. Generally speaking, there will be a 1:1
 * relationship between {@link DataSource} implementations and
 * {@link IDataSourceConnector} implementations.
 * </p>
 * <p>
 * Each {@link IDataSourceConnector} implementation is paired with an
 * {@link IDataSourceCoordinates} implementation that models the data/settings
 * needed to construct a {@link DataSource} instance.
 * </p>
 * <p>
 * So what's the extra functionality provided by this that's otherwise missing?
 * </p>
 * <ol>
 * <li>Unlike {@link DataSource}s, {@link IDataSourceCoordinates}
 * implementations must support JAX-B.</li>
 * <li>Unlike {@link DataSource}s, {@link IDataSourceCoordinates} instances must
 * be immutable (and thus thread-safe).</li>
 * </ol>
 * 
 * @param <C>
 *            the {@link IDataSourceCoordinates} implementation that this
 *            {@link IDataSourceConnector} implementation is paired with
 */
public interface IDataSourceConnector<C extends IDataSourceCoordinates> {
	public static final String JPA_JDBC_DRIVER = "javax.persistence.jdbc.driver";
	public static final String JPA_JDBC_URL = "javax.persistence.jdbc.url";
	public static final String JPA_JDBC_USER = "javax.persistence.jdbc.user";
	public static final String JPA_JDBC_PASSWORD = "javax.persistence.jdbc.password";

	/**
	 * @return the {@link IDataSourceCoordinates} type that this
	 *         {@link IDataSourceConnector} is paired with
	 */
	Class<C> getCoordinatesType();

	/*
	 * I'm not entirely sure yet how this API should work with connection
	 * pooling. If possible, though, I'd like for that to be handled elsewhere:
	 * implementations of this interface would always return new, unpooled
	 * DataSource instances.
	 */

	/**
	 * @param coords
	 *            an {@link IDataSourceCoordinates} instance that provides the
	 *            data/settings needed to create a {@link DataSource} connection
	 * @return a new JDBC {@link DataSource} instance for the data store
	 *         represented by the specified {@link IDataSourceCoordinates}
	 *         instance
	 * @throws UncheckedSqlException
	 *             An {@link UncheckedSqlException} may be thrown if any
	 *             {@link SQLException}s or similar errors are encountered while
	 *             creating the {@link DataSource} instance.
	 */
	DataSource createDataSource(C coords) throws UncheckedSqlException;

	/**
	 * <p>
	 * This method allows {@link IDataSourceCoordinates} instances to also be
	 * used with JPA, by converting the coordinates into a properties
	 * {@link Map}. This {@link Map} will contain the following JPA properties,
	 * or a subset of them:
	 * </p>
	 * <ul>
	 * <li>{@value #JPA_JDBC_DRIVER}</li>
	 * <li>{@value #JPA_JDBC_URL}</li>
	 * <li>{@value #JPA_JDBC_USER}</li>
	 * <li>{@value #JPA_JDBC_PASSWORD}</li>
	 * </ul>
	 * 
	 * @param coords
	 *            an {@link IDataSourceCoordinates} instance that provides the
	 *            data/settings needed to identify a database that JPA can use
	 * @return a {@link Map} of the database-specific properties that can be
	 *         passed to JPA when creating
	 *         <code>javax.persistence.EntityManagerFactory</code>s (or other
	 *         related operations)
	 */
	Map<String, Object> convertToJpaProperties(C coords);

	/**
	 * <p>
	 * This method allows {@link IDataSourceCoordinates} instances to also be
	 * used with <a href="http://www.liquibase.org/">Liquibase</a>, by
	 * converting the coordinates into a (Liquibase-specific) {@link Database}
	 * instance.
	 * </p>
	 * <p>
	 * This method will open a JDBC {@link Connection} to the specified data
	 * source repository. It, however, will not be able to close that
	 * {@link Connection} when it's no longer needed; the code calling this
	 * method assumes ownership of it and must properly clean things up after
	 * the fact.
	 * </p>
	 * 
	 * @param coords
	 *            an {@link IDataSourceCoordinates} instance that provides the
	 *            data/settings needed to identify a database that Liquibase can
	 *            use
	 * @return a (Liquibase-specific) {@link Database} instance
	 */
	Database convertToLiquibaseConnection(C coords);
}
