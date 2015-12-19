package com.justdavis.karl.misc.datasources.postgresql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.postgresql.ds.PGSimpleDataSource;

import com.justdavis.karl.misc.datasources.IDataSourceCoordinates;

/**
 * Represents the data/settings required by {@link PostgreSqlConnector} to
 * construct <a href="http://www.postgresql.org/">PostgreSQL</a>
 * {@link PGSimpleDataSource}s.
 * 
 * @see PostgreSqlConnector
 */
@XmlRootElement
public final class PostgreSqlCoordinates extends IDataSourceCoordinates {
	/*
	 * Something to consider: would it be more or less user friendly to break up
	 * the URL parameter here into its components, e.g. protocol, host, port,
	 * database, etc.? See
	 * org.postgresql.ds.common.BaseDataSource.setUrl(String) for a list of all
	 * the properties URL gets decomposed into.
	 */

	/**
	 * A regular expression that can be used to parse PostgreSQL JDBC URLs, as
	 * described in
	 * <a href="http://jdbc.postgresql.org/documentation/head/connect.html" >
	 * Connecting to the Database</a>. The second capture group should have the
	 * database name.
	 */
	private static final Pattern JDBC_URL_REGEX = Pattern.compile("^jdbc:postgresql:(//.+/)?([^?]+)(\\?.*)?");

	@XmlElement
	private final String url;

	@XmlElement
	@XmlJavaTypeAdapter(FilterableStringAdapter.class)
	private final String user;

	@XmlElement
	@XmlJavaTypeAdapter(FilterableStringAdapter.class)
	private final String password;

	/**
	 * This no-arg default constructor is required by JAX-B.
	 */
	@SuppressWarnings("unused")
	private PostgreSqlCoordinates() {
		this.url = null;
		this.user = null;
		this.password = null;
	}

	/**
	 * Constructs a new {@link PostgreSqlCoordinates} instance.
	 * 
	 * @param url
	 *            the value to use for {@link #getUrl()}
	 * @param user
	 *            the value to use for {@link #getUser()}
	 * @param password
	 *            the value to use for {@link #getPassword()}
	 */
	public PostgreSqlCoordinates(String url, String user, String password) {
		if (url == null)
			throw new IllegalArgumentException();
		if (url.isEmpty())
			throw new IllegalArgumentException();

		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * Constructs a new {@link PostgreSqlCoordinates} instance.
	 * 
	 * @param url
	 *            the value to use for {@link #getUrl()}
	 */
	public PostgreSqlCoordinates(String url) {
		this(url, null, null);
	}

	/**
	 * This transform constructor creates a new {@link PostgreSqlCoordinates}
	 * instance based on the specified instance, but with a different database
	 * name.
	 * 
	 * @param originalCoords
	 *            the {@link PostgreSqlCoordinates} instance to base the new
	 *            transformed instance on
	 * @param databaseName
	 *            the name of the new database name
	 */
	public PostgreSqlCoordinates(PostgreSqlCoordinates originalCoords, String databaseName) {
		String originalUrl = originalCoords.getUrl();

		Matcher urlMatcher = JDBC_URL_REGEX.matcher(originalUrl);
		if (!urlMatcher.matches())
			throw new IllegalArgumentException();

		int endOfHead = urlMatcher.start(2);
		int startOfTail = urlMatcher.end(2);

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(originalUrl.substring(0, endOfHead));
		urlBuilder.append(databaseName);
		urlBuilder.append(originalUrl.substring(startOfTail));

		this.url = urlBuilder.toString();
		this.user = originalCoords.user;
		this.password = originalCoords.password;
	}

	/**
	 * @return the value that will be passed to
	 *         {@link PGSimpleDataSource#setUrl(String)}, e.g. "
	 *         <code>jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true</code>
	 *         "
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the user to connect as, or <code>null</code> if no user should be
	 *         provided
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the password to connect with, or <code>null</code> if no password
	 *         should be provided
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PostgreSqlCoordinates [url=" + url + ", user=" + user + ", password=***]";
	}

	/**
	 * This JAX-B {@link XmlAdapter} should be used for any XML elements whose
	 * text contents might be supplied as Maven properties. During unmarshalling
	 * (converting from XML to Java objects), if the contents of the elements
	 * "look like" an unfiltered Maven property, e.g.
	 * <code>&lt;someElement&gt;${someMavenProperty}&lt;/someElement&gt;</code>,
	 * the value being read in will be replaced with <code>null</code> instead,
	 * to prevent bogus data from being read in.
	 */
	private static final class FilterableStringAdapter extends XmlAdapter<String, String> {
		private static final Pattern MAVEN_PROP_VALUE_REGEX = Pattern.compile("^\\$\\{.*\\}$");

		/**
		 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
		 */
		@Override
		public String unmarshal(String v) throws Exception {
			if (MAVEN_PROP_VALUE_REGEX.matcher(v).matches())
				return null;

			return v;
		}

		/**
		 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
		 */
		@Override
		public String marshal(String v) throws Exception {
			return v;
		}
	}
}
