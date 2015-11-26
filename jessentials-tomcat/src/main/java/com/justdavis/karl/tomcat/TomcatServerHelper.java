package com.justdavis.karl.tomcat;

import java.nio.file.Path;

/**
 * <p>
 * A utility class for creating and interacting with Tomcat server instances.
 * </p>
 * <p>
 * This class is mostly a wrapper around
 * <a href="https://codehaus-cargo.github.io/cargo/Javadocs.html">Cargo's Java
 * API</a>.
 * </p>
 */
public final class TomcatServerHelper {
	/**
	 * @param installPathBase
	 *            the {@link Path} for the directory to install to (the actual
	 *            installation will land in a subdirectory)
	 * @return a new {@link LocallyInstalledTomcatServer} instance
	 */
	public LocallyInstalledTomcatServer createLocallyInstalledServer(Path installPathBase) {
		return new LocallyInstalledTomcatServer(installPathBase);
	}
}
