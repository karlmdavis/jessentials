package com.justdavis.karl.misc.datasources.hsql;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hsqldb.jdbc.JDBCDataSource;

import com.justdavis.karl.misc.datasources.IDataSourceCoordinates;

/**
 * Represents the data/settings required by {@link HsqlConnector} to construct
 * <a href="http://hsqldb.org/">HSQL</a> {@link JDBCDataSource}s.
 * 
 * @see HsqlConnector
 */
@XmlRootElement
public final class HsqlCoordinates extends IDataSourceCoordinates {
	/*
	 * Something to consider: would it be more or less user friendly to break up
	 * the URL parameter here into its components, e.g. protocol, host, port,
	 * database, etc.? See
	 * http://hsqldb.org/doc/2.0/guide/dbproperties-chapt.html for details on
	 * all of the available HSQL connection properties.
	 */

	@XmlElement
	private final String url;

	/**
	 * This no-arg default constructor is required by JAX-B.
	 */
	@SuppressWarnings("unused")
	private HsqlCoordinates() {
		this.url = null;
	}

	/**
	 * Constructs a new {@link HsqlCoordinates} instance.
	 * 
	 * @param url
	 *            the value to use for {@link #getUrl()}
	 */
	public HsqlCoordinates(String url) {
		this.url = url;
	}

	/**
	 * @return the value that will be passed to
	 *         {@link JDBCDataSource#setUrl(String)}, e.g. "
	 *         <code>jdbc:hsqldb:mem:foo</code>"
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HsqlCoordinates [url=" + url + "]";
	}
}
