package com.justdavis.karl.tomcat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.deployable.Deployable;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.container.installer.Installer;
import org.codehaus.cargo.container.installer.ZipURLInstaller;
import org.codehaus.cargo.container.property.ServletPropertySet;
import org.codehaus.cargo.container.tomcat.Tomcat7xInstalledLocalContainer;
import org.codehaus.cargo.container.tomcat.Tomcat7xStandaloneLocalConfiguration;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.justdavis.karl.misc.exceptions.unchecked.UncheckedIoException;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedMalformedUrlException;

/**
 * An {@link ITomcatServer} implementation for
 * {@link Tomcat7xInstalledLocalContainer}s.
 */
public final class LocallyInstalledTomcatServer implements ITomcatServer {
	private static final String TOMCAT_INSTALLER_URL = "http://www.us.apache.org/dist/tomcat/tomcat-7/v7.0.65/bin/apache-tomcat-7.0.65.tar.gz";

	private static final Logger LOGGER = LoggerFactory.getLogger(LocallyInstalledTomcatServer.class);

	private final Path installPath;
	private final Tomcat7xInstalledLocalContainer container;

	/**
	 * Constructs a new {@link LocallyInstalledTomcatServer} instance.
	 * 
	 * @param installPathBase
	 *            the {@link Path} for the directory to install to (the actual
	 *            installation will land in a subdirectory)
	 */
	public LocallyInstalledTomcatServer(Path installPathBase) {
		this.installPath = install(installPathBase);

		Tomcat7xStandaloneLocalConfiguration configuration = (Tomcat7xStandaloneLocalConfiguration) new DefaultConfigurationFactory()
				.createConfiguration("tomcat7x", ContainerType.INSTALLED, ConfigurationType.STANDALONE);
		configuration.setProperty(ServletPropertySet.PORT, "" + selectAvailablePort());
		Tomcat7xInstalledLocalContainer container = (Tomcat7xInstalledLocalContainer) new DefaultContainerFactory()
				.createContainer("tomcat7x", ContainerType.INSTALLED, configuration);
		container.setHome(installPath.toAbsolutePath().toString());
		this.container = container;
	}

	/**
	 * @return a random, available port number
	 */
	private static int selectAvailablePort() {
		try (ServerSocket serverSocket = new ServerSocket(0);) {
			return serverSocket.getLocalPort();
		} catch (IOException e) {
			throw new UncheckedIoException(e);
		}
	}

	/**
	 * @see com.justdavis.karl.tomcat.ITomcatServer#getUrlWithPath(java.lang.String)
	 */
	@Override
	public URL getUrlWithPath(String path) {
		try {
			return new URL(String.format("http://localhost:%d/%s", getHttpPort(), path));
		} catch (MalformedURLException e) {
			throw new UncheckedMalformedUrlException(e);
		}
	}

	/**
	 * @param warPattern
	 *            a regex for the file name of the WAR to determine the context
	 *            root of
	 * @return the context root for the WAR matching the specified
	 *         {@link Pattern}
	 */
	public String getContextRoot(Pattern warPattern) {
		// TODO Is this method needed?
		List<Deployable> deployedStuff = this.container.getConfiguration().getDeployables();
		for (Deployable deployedThing : deployedStuff) {
			if (warPattern.matcher(deployedThing.getFile()).matches()) {
				if (!(deployedThing instanceof WAR))
					LOGGER.warn("Found non-WAR match: {}", deployedThing.getFile());

				WAR deployedWar = (WAR) deployedThing;
				return deployedWar.getContext();
			}
		}

		throw new IllegalArgumentException("No matching WAR found.");
	}

	/**
	 * @return the HTTP port that Tomcat is running on
	 */
	private int getHttpPort() {
		return Integer.parseInt(this.container.getConfiguration().getPropertyValue(ServletPropertySet.PORT));
	}

	/**
	 * @see com.justdavis.karl.tomcat.ITomcatServer#addWar(java.lang.String,
	 *      java.nio.file.Path, java.nio.file.PathMatcher)
	 */
	@Override
	public ITomcatServer addWar(String contextRoot, Path warPath, final PathMatcher warPattern) {
		Objects.requireNonNull(warPattern);

		final List<Path> matchingFiles = new ArrayList<>();
		try {
			Files.walkFileTree(warPath, new SimpleFileVisitor<Path>() {
				/**
				 * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object,
				 *      java.nio.file.attribute.BasicFileAttributes)
				 */
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Objects.requireNonNull(file);
					if (warPattern.matches(file))
						matchingFiles.add(file);

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new UncheckedIoException(e);
		}

		if (matchingFiles.isEmpty())
			throw new IllegalArgumentException("No matching WAR found: " + warPattern);
		if (matchingFiles.size() > 1)
			throw new IllegalArgumentException("Too many matching WARs found: " + matchingFiles);

		WAR warDeployable = new WAR(matchingFiles.get(0).toAbsolutePath().toString());
		warDeployable.setContext(contextRoot);
		this.container.getConfiguration().addDeployable(warDeployable);

		return this;
	}

	/**
	 * @see com.justdavis.karl.tomcat.ITomcatServer#setJavaSystemProperty(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ITomcatServer setJavaSystemProperty(String key, String value) {
		if (this.container.getSystemProperties().containsKey(key))
			LOGGER.warn("Clobberring existing Java system property: {}={}", key, value);
		this.container.getSystemProperties().put(key, value);

		return this;
	}

	/**
	 * @see com.justdavis.karl.tomcat.ITomcatServer#start()
	 */
	@Override
	public ITomcatServer start() {
		container.start();

		return this;
	}

	/**
	 * Installs Tomcat into the specified directory.
	 * 
	 * @param installPathBase
	 *            the {@link Path} for the directory to install to (the actual
	 *            installation will land in a subdirectory)
	 * @return the {@link Path} of the resulting Tomcat installation
	 */
	private static Path install(Path installPathBase) {
		/*
		 * Download from the internets, save the installer into a temp
		 * directory, and install it to the specified path. The Cargo code here
		 * is kind enough to check if the same version has already been
		 * installed. If so, it won't do so twice.
		 */
		URL installerUrl;
		try {
			installerUrl = new URL(TOMCAT_INSTALLER_URL);
		} catch (MalformedURLException e) {
			throw new UncheckedMalformedUrlException(e);
		}
		String downloadLocation = null;
		Installer installer = new ZipURLInstaller(installerUrl, downloadLocation,
				installPathBase.toAbsolutePath().toString());
		installer.install();

		/*
		 * The installer lays things down in a weird way: the actual install
		 * will land in
		 * '<installPathBase>/apache-tomcat-<version>/apache-tomcat-<version>/'
		 * (note the double nesting). The single-nesting might be useful, so
		 * we'll leave it, but we should fix up the double.
		 */
		int beginIndex = TOMCAT_INSTALLER_URL.lastIndexOf('/') + 1;
		int endIndex = TOMCAT_INSTALLER_URL.indexOf(".tar.gz");
		String installName = TOMCAT_INSTALLER_URL.substring(beginIndex, endIndex);
		Path firstSubdir = installPathBase.resolve(installName);

		/*
		 * We only need to do the shuffle if it hasn't already been done (i.e.
		 * if the install is cached from a previous call to this method).
		 */
		if (Files.exists(firstSubdir.resolve("bin")))
			return firstSubdir;

		try {
			// Shuffle things around to undo the second-level of nesting.
			Path firstSubdirAlt = installPathBase.resolve(installName + ".tmp");
			Files.move(firstSubdir, firstSubdirAlt);
			Path secondSubdir = firstSubdirAlt.resolve(installName);
			Files.move(secondSubdir, firstSubdir);

			// Also, be sure to move the ".cargo" file created by the installer.
			Files.move(firstSubdirAlt.resolve(".cargo"), firstSubdir.resolve(".cargo"));
			Files.deleteIfExists(firstSubdirAlt);
		} catch (IOException e) {
			throw new UncheckedIoException(e);
		}

		return firstSubdir;
	}

	/**
	 * @see com.justdavis.karl.tomcat.ITomcatServer#release()
	 */
	@Override
	public void release() {
		container.stop();
	}
}
