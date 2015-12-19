package com.justdavis.karl.misc.datasources.provisioners;

/**
 * This unchecked exception can be thrown by
 * {@link IProvisioningTargetsProvider#findTarget(Class)} and indicates that a
 * matching {@link IProvisioningTarget} instance could not be found.
 * 
 * @see IProvisioningTargetsProvider
 */
public final class UnmatchedProvisioningTargetException extends RuntimeException {
	private static final long serialVersionUID = 3957673788388685256L;

	/**
	 * Constructs a new {@link UnmatchedProvisioningTargetException} instance.
	 * 
	 * @param targetType
	 *            the {@link IProvisioningTarget} implementation type that an
	 *            instance of could not be found
	 */
	public UnmatchedProvisioningTargetException(Class<? extends IProvisioningTarget> targetType) {
		super(String.format("Unable to find %s of type '%s'.", IProvisioningTarget.class.getSimpleName(), targetType));
	}
}
