package com.justdavis.karl.misc.datasources.postgresql;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import com.justdavis.karl.misc.datasources.XmlNamespace;
import com.justdavis.karl.misc.xml.SimpleNamespaceContext;

/**
 * Unit tests for {@link PostgreSqlCoordinates}.
 */
public final class PostgreSqlCoordinatesTest {
	/**
	 * Ensures that {@link PostgreSqlCoordinates} instances can be marshalled.
	 * 
	 * @throws JAXBException
	 *             (shouldn't be thrown if things are working)
	 * @throws XPathExpressionException
	 *             (shouldn't be thrown if things are working)
	 */
	@Test
	public void jaxbMarshalling() throws JAXBException,
			XPathExpressionException {
		// Create the Marshaller needed.
		JAXBContext jaxbContext = JAXBContext
				.newInstance(PostgreSqlCoordinates.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		// Create the instance to be converted to XML.
		PostgreSqlCoordinates coords = new PostgreSqlCoordinates(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true");

		// Convert it to XML.
		DOMResult domResult = new DOMResult();
		marshaller.marshal(coords, domResult);

		// Verify the results.
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(new SimpleNamespaceContext("jed",
				XmlNamespace.JE_DATASOURCES));
		Node coordsNode = (Node) xpath.evaluate(
				"/jed:postgreSqlCoordinates/jed:url", domResult.getNode(),
				XPathConstants.NODE);
		Assert.assertNotNull(coordsNode);
		Assert.assertEquals(coords.getUrl(), coordsNode.getTextContent());
	}

	/**
	 * Ensures that {@link PostgreSqlCoordinates} instances can be unmarshalled.
	 * 
	 * @throws JAXBException
	 *             (shouldn't be thrown if things are working)
	 * @throws XPathExpressionException
	 *             (shouldn't be thrown if things are working)
	 */
	@Test
	public void jaxbUnmarshalling() throws JAXBException,
			XPathExpressionException {
		// Create the Unmarshaller needed.
		JAXBContext jaxbContext = JAXBContext
				.newInstance(PostgreSqlCoordinates.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		// Get the XML to be converted.
		URL sourceXmlUrl = Thread.currentThread().getContextClassLoader()
				.getResource("sample-xml/coords-postgresql-1.xml");

		// Parse the XML to an object.
		PostgreSqlCoordinates parsedCoords = (PostgreSqlCoordinates) unmarshaller
				.unmarshal(sourceXmlUrl);

		// Verify the results.
		Assert.assertNotNull(parsedCoords);

		// Ensure that the auth token is null (should never be included in XML).
		Assert.assertEquals(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true",
				parsedCoords.getUrl());
	}

	/**
	 * Ensures that {@link PostgreSqlCoordinates} instances can be unmarshalled
	 * correctly when the XML document contains unfiltered Maven properties.
	 * 
	 * @throws JAXBException
	 *             (shouldn't be thrown if things are working)
	 * @throws XPathExpressionException
	 *             (shouldn't be thrown if things are working)
	 */
	@Test
	public void jaxbUnmarshallingWithUnfilteredProperties()
			throws JAXBException, XPathExpressionException {
		// Create the Unmarshaller needed.
		JAXBContext jaxbContext = JAXBContext
				.newInstance(PostgreSqlCoordinates.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		// Get the XML to be converted.
		URL sourceXmlUrl = Thread.currentThread().getContextClassLoader()
				.getResource("sample-xml/coords-postgresql-2.xml");

		// Parse the XML to an object.
		PostgreSqlCoordinates parsedCoords = (PostgreSqlCoordinates) unmarshaller
				.unmarshal(sourceXmlUrl);

		// Verify the results.
		Assert.assertNotNull(parsedCoords);

		// Ensure that the auth token is null (should never be included in XML).
		Assert.assertEquals(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true",
				parsedCoords.getUrl());
		Assert.assertNull(parsedCoords.getUser());
		Assert.assertNull(parsedCoords.getPassword());
	}

	/**
	 * Tests
	 * {@link PostgreSqlCoordinates#PostgreSqlCoordinates(PostgreSqlCoordinates, String)}
	 * .
	 */
	@Test
	public void constructWithNewDatabaseName() {
		PostgreSqlCoordinates coordsWithJustDb = new PostgreSqlCoordinates(
				"jdbc:postgresql:foo");
		Assert.assertEquals("jdbc:postgresql:bar", new PostgreSqlCoordinates(
				coordsWithJustDb, "bar").getUrl());

		PostgreSqlCoordinates coordsWithHost = new PostgreSqlCoordinates(
				"jdbc:postgresql://host/foo");
		Assert.assertEquals("jdbc:postgresql://host/bar",
				new PostgreSqlCoordinates(coordsWithHost, "bar").getUrl());

		PostgreSqlCoordinates coordsWithHostAndPort = new PostgreSqlCoordinates(
				"jdbc:postgresql://host:port/foo");
		Assert.assertEquals("jdbc:postgresql://host:port/bar",
				new PostgreSqlCoordinates(coordsWithHostAndPort, "bar")
						.getUrl());

		PostgreSqlCoordinates coordsWithHostAndParams1 = new PostgreSqlCoordinates(
				"jdbc:postgresql://localhost/foo?user=fred&password=secret&ssl=true");
		Assert.assertEquals(
				"jdbc:postgresql://localhost/bar?user=fred&password=secret&ssl=true",
				new PostgreSqlCoordinates(coordsWithHostAndParams1, "bar")
						.getUrl());
	}

	/**
	 * Tests {@link PostgreSqlCoordinates#toString()} to ensure that it doesn't
	 * include clear-text passwords. This would be a huge security risk, if it
	 * did.
	 */
	@Test
	public void toStringSecurity() {
		PostgreSqlCoordinates coordsWithJustDb = new PostgreSqlCoordinates(
				"jdbc:postgresql:foo", "fred", "secret");
		Assert.assertFalse(coordsWithJustDb.toString().contains("secret"));
	}
}
