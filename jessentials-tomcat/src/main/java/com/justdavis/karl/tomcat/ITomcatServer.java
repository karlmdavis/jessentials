package com.justdavis.karl.tomcat;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

/**
 * Implementations of this interface can be used to interact with Tomcat
 * servers.
 */
public interface ITomcatServer {
	/**
	 * Adds the specified WAR to this Tomcat server.
	 * 
	 * @param contextRoot
	 *            the context root to host the specified WAR at (which will be
	 *            overridden if the WAR contains a <code>context.xml</code>
	 *            file)
	 * @param warPath
	 *            the {@link Path} to search for the WAR within
	 * @param warPattern
	 *            a {@link PathMatcher} to locate the WAR file to be deployed in
	 *            this {@link ITomcatServer}
	 * @return this {@link ITomcatServer} instance, for call-chaining purposes
	 */
	ITomcatServer addWar(String contextRoot, Path warPath, PathMatcher warPattern);

	/**
	 * Sets the specified Java system property on this Tomcat server. Will have
	 * no effect if called after {@link #start()}.
	 * 
	 * @param key
	 *            the key of the property to be set
	 * @param value
	 *            the value to set the property to
	 * @return this {@link ITomcatServer} instance, for call-chaining purposes
	 */
	ITomcatServer setJavaSystemProperty(String key, String value);

	/**
	 * Starts the Tomcat server running.
	 * 
	 * @return this {@link ITomcatServer} instance, for call-chaining purposes
	 */
	ITomcatServer start();

	/**
	 * @param path
	 *            the suffix to add to the Tomcat instance's base {@link URL}
	 * @return the base {@link URL} for this Tomcat instance, suffixed with the
	 *         specified path
	 */
	URL getUrlWithPath(String path);

	/**
	 * Shuts down the Tomcat server.
	 */
	void release();
}
