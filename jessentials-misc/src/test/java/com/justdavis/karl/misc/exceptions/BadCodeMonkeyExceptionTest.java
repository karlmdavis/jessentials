package com.justdavis.karl.misc.exceptions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link BadCodeMonkeyException}.
 */
public final class BadCodeMonkeyExceptionTest {
	/**
	 * Tests {@link BadCodeMonkeyException#BadCodeMonkeyException()}.
	 */
	@Test
	public void construct_default() {
		BadCodeMonkeyException exception = new BadCodeMonkeyException();
		Assert.assertNull(exception.getMessage());
		Assert.assertNull(exception.getCause());
	}

	/**
	 * Tests {@link BadCodeMonkeyException#BadCodeMonkeyException(String)}.
	 */
	@Test
	public void construct_message() {
		BadCodeMonkeyException exception = new BadCodeMonkeyException("bob");
		Assert.assertEquals("bob", exception.getMessage());
		Assert.assertNull(exception.getCause());
	}

	/**
	 * Tests {@link BadCodeMonkeyException#BadCodeMonkeyException(Throwable)}.
	 */
	@Test
	public void construct_cause() {
		IllegalArgumentException cause = new IllegalArgumentException();
		BadCodeMonkeyException exception = new BadCodeMonkeyException(cause);
		Assert.assertNotNull(exception.getMessage());
		Assert.assertSame(cause, exception.getCause());
	}

	/**
	 * Tests
	 * {@link BadCodeMonkeyException#BadCodeMonkeyException(String, Throwable)}.
	 */
	@Test
	public void construct_messageAndCause() {
		IllegalArgumentException cause = new IllegalArgumentException();
		BadCodeMonkeyException exception = new BadCodeMonkeyException("bob", cause);
		Assert.assertEquals("bob", exception.getMessage());
		Assert.assertSame(cause, exception.getCause());
	}
}
