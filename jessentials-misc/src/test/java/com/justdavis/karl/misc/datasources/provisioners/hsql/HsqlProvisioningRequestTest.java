package com.justdavis.karl.misc.datasources.provisioners.hsql;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link HsqlProvisioningRequest}.
 */
public final class HsqlProvisioningRequestTest {
	/**
	 * Ensures that
	 * {@link HsqlProvisioningRequest#requestForRandomDatabase(String)} works as
	 * expected.
	 */
	@Test
	public void randomRequest() {
		HsqlProvisioningRequest request1 = HsqlProvisioningRequest
				.requestForRandomDatabase("foo");
		HsqlProvisioningRequest request2 = HsqlProvisioningRequest
				.requestForRandomDatabase("foo");

		Assert.assertTrue(request1.getDatabaseName().startsWith("foo_"));
		Assert.assertNotEquals(request1.getDatabaseName(),
				request2.getDatabaseName());
	}
}
