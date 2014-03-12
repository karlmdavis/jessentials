package com.justdavis.karl.misc.jetty;

import org.junit.Test;

/**
 * Unit tests for {@link EmbeddedServer}.
 */
public final class EmbeddedServerTest {
	/**
	 * Ensures that {@link EmbeddedServer#startServer()} and
	 * {@link EmbeddedServer#stopServer()} work as expected.
	 */
	@Test
	public void startAndStop() {
		/*
		 * FIXME Can't test this, because Jetty requires Java 7, but this
		 * project requires Java 6. Won't be able to run these tests until
		 * that's changed.
		 */
		// EmbeddedServer server = new EmbeddedServer(0, false, null);
		// server.startServer();
		// server.stopServer();
	}
}
