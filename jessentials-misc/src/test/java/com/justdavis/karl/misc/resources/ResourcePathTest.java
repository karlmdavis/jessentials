package com.justdavis.karl.misc.resources;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link ResourcePath}.
 */
public final class ResourcePathTest {
	/**
	 * Tests {@link ResourcePath} against a class and a sibling resource.
	 */
	@Test
	public void simpleUsage() {
		ResourcePath path = new ResourcePath(ResourcePathTest.class, "foo.txt");
		Assert.assertEquals("com/justdavis/karl/misc/resources/foo.txt",
				path.getPath());
	}

	/**
	 * Tests {@link ResourcePath} against a class and a resource in a package
	 * one level deeper than that class.
	 */
	@Test
	public void deeperResource() {
		ResourcePath path = new ResourcePath(ResourcePathTest.class,
				"bar/foo.txt");
		Assert.assertEquals("com/justdavis/karl/misc/resources/bar/foo.txt",
				path.getPath());
	}

	/**
	 * Tests {@link ResourcePath} against an anonymous class and a sibling
	 * resource.
	 */
	@Test
	public void anonymousClass() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// Won't be called; do nothing.
			}
		};

		ResourcePath path = new ResourcePath(runnable.getClass(), "foo.txt");
		Assert.assertEquals("com/justdavis/karl/misc/resources/foo.txt",
				path.getPath());
	}
}
