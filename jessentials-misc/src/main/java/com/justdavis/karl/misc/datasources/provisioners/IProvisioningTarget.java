package com.justdavis.karl.misc.datasources.provisioners;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * <p>
 * {@link IProvisioningTarget} implementations model the options for an
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
 * <p>
 * {@link IProvisioningTarget} implementations must be
 * marshallable/unmarshallable as JAX-B {@link XmlElementRef}s.
 * </p>
 * 
 * @see IDataSourceProvisioner
 * @see IProvisioningRequest
 */
public abstract class IProvisioningTarget {
	/*
	 * The name here is a bit misleading, but this type <em>should</em> be an
	 * interface, it just can't, because JAX-B's @XmlElementRef doesn't work on
	 * interface properties, but does on abstract classes. Silliness!
	 */

	/*
	 * Don't actually have any required functionality here right now. Really
	 * more of a marker interface.
	 */
}
