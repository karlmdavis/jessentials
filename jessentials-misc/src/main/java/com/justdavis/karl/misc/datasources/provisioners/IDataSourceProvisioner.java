package com.justdavis.karl.misc.datasources.provisioners;

import com.justdavis.karl.misc.datasources.IDataSourceCoordinates;

/**
 * Implementations of this interface are responsible for provisioning (creating,
 * loading, etc.) data source repositories (e.g. databases). Different
 * implementations will support doing this in different ways: one implementation
 * might restore database backups from files, while another might create and
 * populate databases via a <code>.sql</code> script.
 * 
 * @param <C>
 *            the {@link IDataSourceCoordinates} implementation that this
 *            {@link IDataSourceProvisioner} implementation works with: the one
 *            for the database platform it creates databases for
 * @param <T>
 *            the {@link IProvisioningTarget} implementation that specifies
 *            (some of) the provisioning options (generally, the target server)
 * @param <R>
 *            the {@link IProvisioningRequest} implementation that specifies
 *            (the rest of) the provisioning options (generally, the ones that
 *            are the same regardless of what machine the code is run from)
 */
public interface IDataSourceProvisioner<C extends IDataSourceCoordinates, T extends IProvisioningTarget, R extends IProvisioningRequest> {
	/**
	 * @return the {@link IProvisioningTarget} implementation that specifies
	 *         (some of) the provisioning options (generally, the target server)
	 */
	Class<T> getTargetType();

	/**
	 * @return the {@link IProvisioningRequest} implementation that specifies
	 *         (the rest of) the provisioning options (generally, the ones that
	 *         are the same regardless of what machine the code is run from)
	 */
	Class<R> getRequestType();

	/**
	 * Provisions a data source repository that can be connected to via the
	 * returned {@link IDataSourceCoordinates}.
	 * 
	 * @param target
	 *            the {@link IProvisioningTarget} that specifies (some of) the
	 *            provisioning options (generally, the target server)
	 * @param request
	 *            the {@link IProvisioningRequest} that specifies (the rest of)
	 *            the provisioning options (generally, the ones that are the
	 *            same regardless of what machine the code is run from)
	 * @return the {@link IDataSourceCoordinates} for the newly-provisioned data
	 *         source repository
	 */
	C provision(T target, R request);

	/**
	 * <p>
	 * Deletes the specified data source repository. Intended for use in
	 * cleaning up databases that are created during automated tests.
	 * </p>
	 * <p>
	 * <strong>WARNING:</strong> This will delete data; the data will be gone;
	 * no backups will be made (automatically); kiss your database goodbye.
	 * </p>
	 * 
	 * @param target
	 *            the {@link IProvisioningTarget} that was passed to
	 *            {@link #provision(IProvisioningTarget, IProvisioningRequest)}
	 *            for the data source repository to be removed
	 * @param request
	 *            the {@link IProvisioningRequest} that was passed to
	 *            {@link #provision(IProvisioningTarget, IProvisioningRequest)}
	 *            for the data source repository to be removed
	 */
	void delete(T target, R request);
}
