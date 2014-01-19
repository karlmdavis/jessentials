package com.justdavis.karl.misc.datasources.schema;

import com.justdavis.karl.misc.datasources.IDataSourceCoordinates;

/**
 * Implementations of this interface are responsible for populating/upgrading
 * the data source schema. This application doesn't ship <code>.sql</code>
 * scripts as part of its release; it will use this interface to automatically
 * create the schema needed in the database it's running against.
 */
public interface IDataSourceSchemaManager {
	/**
	 * Creates or upgrades the schema of the specified data source to the one
	 * required by the application's DAOs.
	 * 
	 * @param coords
	 *            the {@link IDataSourceCoordinates} to the data source that the
	 *            schema should be populated in
	 */
	void createOrUpgradeSchema(IDataSourceCoordinates coords);
}
