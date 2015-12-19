package com.justdavis.karl.misc.xml;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link SimpleNamespaceContext}.
 */
public final class SimpleNamespaceContextTest {
	/**
	 * Tests {@link SimpleNamespaceContext}.
	 * 
	 * @throws ParserConfigurationException
	 *             (shouldn't be thrown if things are working)
	 */
	@Test
	public void simpleUsage() throws ParserConfigurationException {
		SimpleNamespaceContext contextWithDefault = new SimpleNamespaceContext("bob");
		Assert.assertEquals("bob", contextWithDefault.getNamespaceURI(XMLConstants.DEFAULT_NS_PREFIX));
		Iterator<?> prefixes = contextWithDefault.getPrefixes("bob");
		Assert.assertEquals(XMLConstants.DEFAULT_NS_PREFIX, prefixes.next());
		Assert.assertFalse(XMLConstants.DEFAULT_NS_PREFIX, prefixes.hasNext());
	}
}
