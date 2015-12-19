package com.justdavis.karl.misc.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit tests for {@link XmlConverter}.
 */
public final class XmlConverterTest {
	/**
	 * Tests {@link XmlConverter} against a basic DOM object.
	 * 
	 * @throws ParserConfigurationException
	 *             (shouldn't be thrown if things are working)
	 */
	@Test
	public void simpleUsage() throws ParserConfigurationException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("root");
		doc.appendChild(root);
		Element foo = doc.createElement("foo");
		root.appendChild(foo);

		String xmlAsString = new XmlConverter().convertToString(doc);
		String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<root>\n"
				+ "    <foo/>\n" + "</root>\n";
		Assert.assertEquals(expectedString, xmlAsString);
	}
}
