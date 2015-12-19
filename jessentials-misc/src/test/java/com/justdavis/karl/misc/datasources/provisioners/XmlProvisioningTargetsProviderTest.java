package com.justdavis.karl.misc.datasources.provisioners;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioner;
import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioningTarget;
import com.justdavis.karl.misc.datasources.provisioners.postgresql.PostgreSqlProvisioner;
import com.justdavis.karl.misc.datasources.provisioners.postgresql.PostgreSqlProvisioningTarget;

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
		DataSourceProvisionersManager provisionersManager = new DataSourceProvisionersManager(new HsqlProvisioner());
		URL targetsDocUrl = Thread.currentThread().getContextClassLoader()
				.getResource("sample-xml/datasource-provisioning-targets-1.xml");
		XmlProvisioningTargetsProvider targetsProvider = new XmlProvisioningTargetsProvider(provisionersManager,
				targetsDocUrl);

		// Verify that it works as expected.
		HsqlProvisioningTarget target = targetsProvider.findTarget(HsqlProvisioningTarget.class);
		Assert.assertNotNull(target);
	}

	/**
	 * Tests {@link XmlProvisioningTargetsProvider#findTarget(Class)} when run
	 * against a targets XML document that has properties that weren't set, and
	 * thus weren't filtered.
	 */
	@Test
	public void findTargetWithUnfilteredProperties() {
		// Create the XmlProvisioningTargetsProvider instance to test.
		@SuppressWarnings("unchecked")
		DataSourceProvisionersManager provisionersManager = new DataSourceProvisionersManager(
				new PostgreSqlProvisioner());
		URL targetsDocUrl = Thread.currentThread().getContextClassLoader()
				.getResource("sample-xml/datasource-provisioning-targets-2.xml");
		XmlProvisioningTargetsProvider targetsProvider = new XmlProvisioningTargetsProvider(provisionersManager,
				targetsDocUrl);

		// Verify that it works as expected.
		PostgreSqlProvisioningTarget target = targetsProvider.findTarget(PostgreSqlProvisioningTarget.class);
		Assert.assertNotNull(target);
		Assert.assertNotNull(target.getServerCoords().getUrl());
		Assert.assertNull(target.getServerCoords().getUser());
		Assert.assertNull(target.getServerCoords().getPassword());
	}
}
