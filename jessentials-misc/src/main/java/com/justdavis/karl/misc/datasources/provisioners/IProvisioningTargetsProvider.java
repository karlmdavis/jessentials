package com.justdavis.karl.misc.datasources.provisioners;

/**
 * Implementations of this interface are responsible for providing the available
 * {@link IProvisioningTarget}s available.
 * 
 * @see XmlProvisioningTargetsProvider
 */
public interface IProvisioningTargetsProvider {
	/**
	 * @param targetType
	 *            the {@link IProvisioningTarget} implementation type to find an
	 *            instance of
	 * @return returns an {@link IProvisioningTarget} of the specified type
	 * @throws UnmatchedProvisioningTargetException
	 *             An {@link UnmatchedProvisioningTargetException} will be
	 *             thrown if a matching {@link IProvisioningTarget} instance
	 *             could not be found.
	 */
	<T extends IProvisioningTarget> T findTarget(Class<T> targetType)
			throws UnmatchedProvisioningTargetException;
}
