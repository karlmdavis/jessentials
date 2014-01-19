package com.justdavis.karl.misc.datasources;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.justdavis.karl.misc.SpringConfigForJEMisc;

/**
 * Integration tests for {@link SpringConfigForJEMisc}.
 */
public final class SpringConfigForJEMiscIT {
	/**
	 * Tests {@link SpringConfigForJEMisc}, to ensure it can be used to
	 * configure a Spring context.
	 */
	@Test
	public void springContextCreation() {
		AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext(
				SpringConfigForJEMisc.class);
		Assert.assertNotNull(springContext
				.getBean(DataSourceConnectorsManager.class));
		springContext.close();
	}
}
