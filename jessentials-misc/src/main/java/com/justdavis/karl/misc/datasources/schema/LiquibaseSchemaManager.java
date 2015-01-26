package com.justdavis.karl.misc.datasources.schema;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.justdavis.karl.misc.datasources.DataSourceConnectorsManager;
import com.justdavis.karl.misc.datasources.IDataSourceCoordinates;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedLiquibaseException;

/**
 * The default {@link IDataSourceSchemaManager} implementation. Uses the open
 * source <a href="http://www.liquibase.org/">Liquibase</a> library to do the
 * heavy lifting.
 */
public final class LiquibaseSchemaManager implements IDataSourceSchemaManager {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LiquibaseSchemaManager.class);

	private final DataSourceConnectorsManager connectorsManager;
	private final String liquibaseChangeLogPath;

	/**
	 * Constructs a new {@link LiquibaseSchemaManager} instance.
	 * 
	 * @param connectorsManager
	 *            the {@link DataSourceConnectorsManager} to use
	 * @param liquibaseChangeLogPath
	 *            the path to the Liquibase XML change log, which may be either
	 *            a classpath entry or filesystem path (both will be tried, in
	 *            that order)
	 */
	public LiquibaseSchemaManager(
			DataSourceConnectorsManager connectorsManager,
			String liquibaseChangeLogPath) {
		this.connectorsManager = connectorsManager;
		this.liquibaseChangeLogPath = liquibaseChangeLogPath;
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.schema.IDataSourceSchemaManager#createOrUpgradeSchema(com.justdavis.karl.misc.datasources.IDataSourceCoordinates)
	 */
	@Override
	public void createOrUpgradeSchema(IDataSourceCoordinates coords) {
		LOGGER.info("Liquibase schema create/upgrade: running...");
		ResourceAccessor resourceAccessor = new CompositeResourceAccessor(
				new ClassLoaderResourceAccessor(Thread.currentThread()
						.getContextClassLoader()),
				new FileSystemResourceAccessor("."));

		Database database = connectorsManager
				.convertToLiquibaseConnection(coords);
		try {
			Liquibase liquibase = new Liquibase(liquibaseChangeLogPath,
					resourceAccessor, database);
			liquibase.update(new Contexts());
		} catch (LiquibaseException e) {
			throw new UncheckedLiquibaseException(e);
		} finally {
			/*
			 * Every Liquibase 'Database' instance has an open JDBC 'Connection'
			 * instance that was created in the
			 * 'convertToLiquibaseConnection(...)' call above. Need to ensure
			 * that's cleaned up.
			 */
			try {
				database.getConnection().close();
				LOGGER.info("Database connection released.");
			} catch (DatabaseException e) {
				throw new UncheckedLiquibaseException(e);
			}
		}
		LOGGER.info("Liquibase schema create/upgrade: complete.");
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.schema.IDataSourceSchemaManager#wipeSchema(com.justdavis.karl.misc.datasources.IDataSourceCoordinates)
	 */
	@Override
	public void wipeSchema(IDataSourceCoordinates coords) {
		LOGGER.info("Liquibase schema wipe: running...");
		ResourceAccessor resourceAccessor = new CompositeResourceAccessor(
				new ClassLoaderResourceAccessor(Thread.currentThread()
						.getContextClassLoader()),
				new FileSystemResourceAccessor("."));

		Database database = connectorsManager
				.convertToLiquibaseConnection(coords);
		try {
			Liquibase liquibase = new Liquibase(liquibaseChangeLogPath,
					resourceAccessor, database);
			liquibase.dropAll();
		} catch (LiquibaseException e) {
			throw new UncheckedLiquibaseException(e);
		} finally {
			/*
			 * Every Liquibase 'Database' instance has an open JDBC 'Connection'
			 * instance that was created in the
			 * 'convertToLiquibaseConnection(...)' call above. Need to ensure
			 * that's cleaned up.
			 */
			try {
				database.getConnection().close();
				LOGGER.info("Database connection released.");
			} catch (DatabaseException e) {
				throw new UncheckedLiquibaseException(e);
			}
		}
		LOGGER.info("Liquibase schema wipe: complete.");
	}
}
