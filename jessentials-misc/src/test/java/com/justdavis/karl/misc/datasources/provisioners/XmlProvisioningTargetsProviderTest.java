package com.justdavis.karl.misc.datasources.provisioners;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioner;
import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioningTarget;

/**
 * Unit tests for {@link XmlProvisioningTargetsProvider}.
 */
public class XmlProvisioningTargetsProviderTest {
	/**
	 * Tests {@link XmlProvisioningTargetsProvider#findTarget(Class)}.
	 */
	@Test
	public void findTarget() {
		// Create the XmlProvisioningTargetsProvider instance to test.
		@SuppressWarnings("unchecked")
		DataSourceProvisionersManager provisionersManager = new DataSourceProvisionersManager(
				new HsqlProvisioner());
		URL targetsDocUrl = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource("sample-xml/datasource-provisioning-targets-1.xml");
		XmlProvisioningTargetsProvider targetsProvider = new XmlProvisioningTargetsProvider(
				provisionersManager, targetsDocUrl);

		// Verify that it works as expected.
		HsqlProvisioningTarget hsqlTarget = targetsProvider
				.findTarget(HsqlProvisioningTarget.class);
		Assert.assertNotNull(hsqlTarget);
	}
}
