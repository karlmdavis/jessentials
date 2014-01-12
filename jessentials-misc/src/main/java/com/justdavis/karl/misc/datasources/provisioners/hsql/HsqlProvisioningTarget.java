package com.justdavis.karl.misc.datasources.provisioners.hsql;

import javax.xml.bind.annotation.XmlRootElement;

import com.justdavis.karl.misc.datasources.provisioners.IProvisioningTarget;

/**
 * <p>
 * The {@link IProvisioningTarget} implementation for {@link HsqlProvisioner}.
 * </p>
 * <p>
 * Please note that there's really nothing here, as currently only in-memory
 * HSQL databases are supported, and no target options are needed for those.
 * </p>
 */
@XmlRootElement
public final class HsqlProvisioningTarget extends IProvisioningTarget {

}
