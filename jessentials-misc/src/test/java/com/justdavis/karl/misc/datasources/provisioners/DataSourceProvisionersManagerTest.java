package com.justdavis.karl.misc.datasources.provisioners;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.karl.misc.datasources.provisioners.DataSourceProvisionersManager.ProvisioningResult;
import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioner;
import com.justdavis.karl.misc.datasources.provisioners.hsql.HsqlProvisioningRequest;

/**
 * Unit tests for {@link DataSourceProvisionersManager}.
 */
public final class DataSourceProvisionersManagerTest {
	/**
	 * Tests {@link DataSourceProvisionersManager#}.
	 */
	@Test
	public void findTarget() {
		// Create the DataSourceProvisionersManager instance to test.
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
		HsqlProvisioningRequest hsqlRequest = new HsqlProvisioningRequest("foo");
		ProvisioningResult provisioningResult = null;
		try {
			provisioningResult = provisionersManager.provision(targetsProvider,
					hsqlRequest);
			Assert.assertNotNull(provisioningResult);
		} finally {
			if (provisioningResult != null)
				provisionersManager.delete(provisioningResult);
		}
	}
}
