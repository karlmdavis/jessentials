package com.justdavis.karl.tomcat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * Integration tests for {@link LocallyInstalledTomcatServer}.
 */
public final class LocallyInstalledTomcatServerIT {
	/**
	 * Uses {@link LocallyInstalledTomcatServer} to launch
	 * <code>src/test/resources/tomcat-sample-7.0.war</code>.
	 * 
	 * @throws IOException
	 *             Any {@link IOException}s that occur indicate that the test
	 *             has failed.
	 */
	@Test
	public void launchSample() throws IOException {
		TomcatServerHelper serverHelper = new TomcatServerHelper();
		ITomcatServer server = null;
		try {
			// Configure and start the server.
			server = serverHelper.createLocallyInstalledServer(Paths.get(".", "target", "tomcat"))
					.addWar("sample", Paths.get("."),
							FileSystems.getDefault().getPathMatcher("glob:./src/test/resources/tomcat-sample-7.0.war"))
					.start();
			Assert.assertNotNull(server);
			Assert.assertTrue(server instanceof LocallyInstalledTomcatServer);

			// Verify that the sample is running, as expected.
			URL urlForSample = server.getUrlWithPath("/sample");
			HttpURLConnection connectionToSample = (HttpURLConnection) urlForSample.openConnection();
			Assert.assertEquals(200, connectionToSample.getResponseCode());
		} finally {
			if (server != null)
				server.release();
		}
	}
}
