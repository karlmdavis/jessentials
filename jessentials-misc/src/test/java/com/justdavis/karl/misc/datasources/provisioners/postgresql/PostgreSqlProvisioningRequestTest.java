package com.justdavis.karl.misc.datasources.provisioners.postgresql;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link PostgreSqlProvisioningRequest}.
 */
public final class PostgreSqlProvisioningRequestTest {
	/**
	 * Ensures that
	 * {@link PostgreSqlProvisioningRequest#requestForRandomDatabase(String)}
	 * works as expected.
	 */
	@Test
	public void randomRequest() {
		PostgreSqlProvisioningRequest request1 = PostgreSqlProvisioningRequest.requestForRandomDatabase("foo");
		PostgreSqlProvisioningRequest request2 = PostgreSqlProvisioningRequest.requestForRandomDatabase("foo");

		Assert.assertTrue(request1.getDatabaseName().startsWith("foo_"));
		Assert.assertNotEquals(request1.getDatabaseName(), request2.getDatabaseName());
	}
}
