package com.justdavis.karl.misc.datasources.provisioners;

/**
 * <p>
 * {@link IProvisioningRequest} implementations model the options for an
 * {@link IDataSourceProvisioner} operation. Each {@link IDataSourceProvisioner}
 * implementation splits these options between a paired
 * {@link IProvisioningRequest} and {@link IProvisioningTarget} implementation:
 * </p>
 * <ul>
 * <li>{@link IProvisioningRequest} models the options that don't change if the
 * request is run from a different machine, e.g. what to name the resulting
 * database.</li>
 * <li>{@link IProvisioningTarget} models the options that might change if the
 * request is run from a different machine, e.g. what database <em>server</em>
 * to run the request on.</li>
 * </ul>
 * 
 * @see IDataSourceProvisioner
 * @see IProvisioningTarget
 */
public interface IProvisioningRequest {

}
