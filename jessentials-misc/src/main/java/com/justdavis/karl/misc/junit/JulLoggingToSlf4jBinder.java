package com.justdavis.karl.misc.junit;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * <p>
 * This JUnit {@link TestRule} can be used in test classes to ensure that Java's
 * built-in logging events are properly routed to SLF4J. It should be used as
 * follows:
 * </p>
 * 
 * <pre>
 * <code>
 * public class FooTest {
 * 	&#064;Rule
 * 	public JulLoggingToSlf4jBinder julBinder = new JulLoggingToSlf4jBinder();
 * 
 * 	// ... test cases, etc.
 * }</code>
 * </pre>
 */
public final class JulLoggingToSlf4jBinder extends ExternalResource {
	/**
	 * @see org.junit.rules.ExternalResource#before()
	 */
	@Override
	protected final void before() throws Throwable {
		/*
		 * Bind JUL events to SLF4J. I'm not entirely clear on why the root JUL
		 * Logger's level has to be set to 'ALL', but it absolutely does:
		 * without this call, JUL events below the default JUL level (whatever
		 * that is) will not get routed to SLF4J, regardless of the SLF4J
		 * configuration.
		 */
		Logger.getLogger("").setLevel(Level.ALL);
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		super.before();
	}
}
