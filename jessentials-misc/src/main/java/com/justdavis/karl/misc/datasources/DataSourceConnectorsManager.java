package com.justdavis.karl.misc.datasources;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.justdavis.karl.misc.exceptions.BadCodeMonkeyException;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedSqlException;

import liquibase.database.Database;

/**
 * A helper class for working with an application's {@link IDataSourceConnector}
 * implementations.
 */
@Component
public final class DataSourceConnectorsManager {
	private final Set<IDataSourceConnector<? extends IDataSourceCoordinates>> dataSourceConnectors;

	/**
	 * Constructs a new {@link DataSourceConnectorsManager} instance.
	 * 
	 * @param dataSourceConnectors
	 *            the {@link Set} of {@link IDataSourceConnector}s available to
	 *            the application
	 */
	@Inject
	public DataSourceConnectorsManager(
			Set<IDataSourceConnector<? extends IDataSourceCoordinates>> dataSourceConnectors) {
		/*
		 * Sanity check: if no IDataSourceConnector implementations are
		 * registered, there's a problem.
		 */
		if (dataSourceConnectors == null || dataSourceConnectors.isEmpty())
			throw new BadCodeMonkeyException(String.format(
					"No %s implementations were provided."
							+ " This is likely a problem with the DI/Spring configuration.",
					IDataSourceConnector.class.getSimpleName()));

		this.dataSourceConnectors = dataSourceConnectors;
	}

	/**
	 * Constructs a new {@link DataSourceConnectorsManager} instance.
	 * 
	 * @param dataSourceConnectors
	 *            the {@link IDataSourceConnector}s available to the application
	 */
	@SafeVarargs
	public DataSourceConnectorsManager(IDataSourceConnector<? extends IDataSourceCoordinates>... dataSourceConnectors) {
		this(new HashSet<IDataSourceConnector<? extends IDataSourceCoordinates>>(Arrays.asList(dataSourceConnectors)));
	}

	/**
	 * @return all of the {@link IDataSourceCoordinates} types associated with
	 *         the available {@link IDataSourceConnector}s
	 */
	public Set<Class<? extends IDataSourceCoordinates>> getCoordinatesTypes() {
		Set<Class<? extends IDataSourceCoordinates>> coordTypes = new HashSet<Class<? extends IDataSourceCoordinates>>();
		for (IDataSourceConnector<? extends IDataSourceCoordinates> connector : dataSourceConnectors)
			coordTypes.add(connector.getCoordinatesType());

		return coordTypes;
	}

	/**
	 * This method will select the {@link IDataSourceConnector} implementation
	 * that supports the specified {@link IDataSourceCoordinates} instance and
	 * return the results of
	 * {@link IDataSourceConnector#createDataSource(IDataSourceCoordinates)} for
	 * it.
	 * 
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
	 * @throws IllegalArgumentException
	 *             An {@link IllegalArgumentException} will be thrown if no
	 *             matching {@link IDataSourceConnector} can be found for the
	 *             specified {@link IDataSourceCoordinates}.
	 */
	@SuppressWarnings("unchecked")
	public DataSource createDataSource(IDataSourceCoordinates coords)
			throws UncheckedSqlException, IllegalArgumentException {
		@SuppressWarnings("rawtypes")
		IDataSourceConnector matchingConnector = findMatchingConnector(coords);
		return matchingConnector.createDataSource(coords);
	}

	/**
	 * This method will select the {@link IDataSourceConnector} implementation
	 * that supports the specified {@link IDataSourceCoordinates} instance and
	 * return the results of
	 * {@link IDataSourceConnector#convertToJpaProperties(IDataSourceCoordinates)}
	 * for it.
	 * 
	 * @param coords
	 *            an {@link IDataSourceCoordinates} instance that provides the
	 *            data/settings needed to create the JPA properties
	 * @return a {@link Map} of the database-specific properties that can be
	 *         passed to JPA when creating
	 *         {@link javax.persistence.EntityManagerFactory}s (or other related
	 *         operations)
	 * @throws IllegalArgumentException
	 *             An {@link IllegalArgumentException} will be thrown if no
	 *             matching {@link IDataSourceConnector} can be found for the
	 *             specified {@link IDataSourceCoordinates}.
	 * @see javax.persistence.Persistence#createEntityManagerFactory(String,
	 *      Map)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> convertToJpaProperties(IDataSourceCoordinates coords) throws IllegalArgumentException {
		@SuppressWarnings("rawtypes")
		IDataSourceConnector matchingConnector = findMatchingConnector(coords);
		return matchingConnector.convertToJpaProperties(coords);
	}

	/**
	 * This method will select the {@link IDataSourceConnector} implementation
	 * that supports the specified {@link IDataSourceCoordinates} instance and
	 * return the results of
	 * {@link IDataSourceConnector#convertToLiquibaseConnection(IDataSourceCoordinates)}
	 * for it.
	 * 
	 * @return a (Liquibase-specific) {@link Database} instance
	 * @throws IllegalArgumentException
	 *             An {@link IllegalArgumentException} will be thrown if no
	 *             matching {@link IDataSourceConnector} can be found for the
	 *             specified {@link IDataSourceCoordinates}.
	 */
	@SuppressWarnings("unchecked")
	public Database convertToLiquibaseConnection(IDataSourceCoordinates coords) {
		@SuppressWarnings("rawtypes")
		IDataSourceConnector matchingConnector = findMatchingConnector(coords);
		return matchingConnector.convertToLiquibaseConnection(coords);
	}

	/**
	 * @param coords
	 *            the {@link IDataSourceCoordinates} to find a matching
	 *            {@link IDataSourceConnector} for
	 * @return the {@link IDataSourceConnector} that supports the specified
	 *         {@link IDataSourceCoordinates} instance
	 * @throws IllegalArgumentException
	 *             an {@link IllegalArgumentException} will be thrown if no
	 *             matching {@link IDataSourceConnector} can be found for the
	 *             specified {@link IDataSourceCoordinates}.
	 */
	@SuppressWarnings("rawtypes")
	private IDataSourceConnector findMatchingConnector(IDataSourceCoordinates coords) throws IllegalArgumentException {
		// Sanity check: null coords?
		if (coords == null)
			throw new IllegalArgumentException();

		// Find the IDataSourceConnector that supports the coords.
		IDataSourceConnector matchingConnector = null;
		for (IDataSourceConnector<? extends IDataSourceCoordinates> connector : dataSourceConnectors)
			if (connector.getCoordinatesType().isAssignableFrom(coords.getClass()))
				matchingConnector = connector;

		// Did we find a match?
		if (matchingConnector == null)
			throw new IllegalArgumentException(String.format("No matching %s found for the coordinates of type '%s'.",
					IDataSourceConnector.class.getSimpleName(), coords.getClass()));
		return matchingConnector;
	}
}
