package com.justdavis.karl.misc.datasources;

import java.sql.Driver;

import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * An {@link IDataSourceCoordinates} implementation models the data/settings
 * needed to construct a {@link DataSource} instance. Each
 * {@link IDataSourceCoordinates} implementation will be paired with a
 * corresponding {@link IDataSourceConnector} implementation, and typically,
 * each pair corresponds to a given JDBC {@link DataSource}/{@link Driver}
 * implementation. For example, there might be a
 * <code>PostgreSqlConnector</code> and <code>PostgreSqlCoordinates</code> pair
 * that together provide functionality for working with the PostgreSQL database
 * server's JDBC implementation.
 * </p>
 * <p>
 * Each {@link IDataSourceCoordinates} instance must comply with the following
 * additional requirements:
 * </p>
 * <ul>
 * <li>Instances must be immutable (and thus thread-safe).</li>
 * <li>Instances must be marshallable and unmarshallable via JAX-B. They must be
 * marked as an {@link XmlRootElement} (though they may also be used as non-root
 * elements in XML documents, as well).</li>
 * <li>Implementations must provide the data/settings necessary for the paired
 * {@link IDataSourceConnector} implementation to construct a {@link DataSource}
 * instance (via
 * {@link IDataSourceConnector#createDataSource(IDataSourceCoordinates)}).</li>
 * </ul>
 * 
 * @see IDataSourceConnector
 */
public abstract class IDataSourceCoordinates {
	/*
	 * The name here is a bit misleading, but this type <em>should</em> be an
	 * interface, it just can't, because JAX-B's @XmlElementRef doesn't work on
	 * interface properties, but does on abstract classes. Silliness!
	 */

	/*
	 * Don't actually have any required functionality here right now. Probably
	 * will later.
	 */
}
