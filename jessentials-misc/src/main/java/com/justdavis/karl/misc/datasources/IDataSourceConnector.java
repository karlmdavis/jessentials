package com.justdavis.karl.misc.datasources;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.justdavis.karl.misc.exceptions.unchecked.UncheckedSqlException;

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
}
