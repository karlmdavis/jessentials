package com.justdavis.karl.misc.datasources.hsql;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
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
 * Unit tests for {@link HsqlConnector} and {@link HsqlCoordinates}.
 */
public final class HsqlConnectorTest {
	/**
	 * Ensures that {@link HsqlCoordinates} instances can be marshalled.
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
				.newInstance(HsqlCoordinates.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		// Create the instance to be converted to XML.
		HsqlCoordinates coords = new HsqlCoordinates("jdbc:hsqldb:mem:foo");

		// Convert it to XML.
		DOMResult domResult = new DOMResult();
		marshaller.marshal(coords, domResult);

		// Verify the results.
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(new SimpleNamespaceContext("jed",
				XmlNamespace.JE_DATASOURCES));
		Node coordsNode = (Node) xpath.evaluate("/jed:hsqlCoordinates/jed:url",
				domResult.getNode(), XPathConstants.NODE);
		Assert.assertNotNull(coordsNode);
		Assert.assertEquals(coords.getUrl(), coordsNode.getTextContent());
	}

	/**
	 * Ensures that {@link HsqlCoordinates} instances can be unmarshalled.
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
				.newInstance(HsqlCoordinates.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		// Get the XML to be converted.
		URL sourceXmlUrl = Thread.currentThread().getContextClassLoader()
				.getResource("sample-xml/coords-hsql-1.xml");

		// Parse the XML to an object.
		HsqlCoordinates parsedCoords = (HsqlCoordinates) unmarshaller
				.unmarshal(sourceXmlUrl);

		// Verify the results.
		Assert.assertNotNull(parsedCoords);

		// Ensure that the auth token is null (should never be included in XML).
		Assert.assertEquals("jdbc:hsqldb:mem:foo", parsedCoords.getUrl());
	}

	/**
	 * Tests {@link HsqlConnector#createDataSource(HsqlCoordinates)}.
	 * 
	 * @throws SQLException
	 *             (this error indicates the test failed)
	 */
	@Test
	public void createDataSource() throws SQLException {
		Connection hsqlConnection = null;

		try {
			HsqlCoordinates coords = new HsqlCoordinates(
					"jdbc:hsqldb:mem:foo;shutdown=true");

			// Create a DataSource
			HsqlConnector connector = new HsqlConnector();
			DataSource hsqlDataSource = connector.createDataSource(coords);
			Assert.assertNotNull(hsqlDataSource);

			/*
			 * Create and test a Connection. The query here is taken from
			 * http://stackoverflow.com/a/3670000/1851299.
			 */
			hsqlConnection = hsqlDataSource.getConnection();
			Assert.assertNotNull(hsqlConnection);
			PreparedStatement statement = hsqlConnection
					.prepareStatement("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS");
			ResultSet resultSet = statement.executeQuery();
			Assert.assertTrue(resultSet.next());
			Assert.assertEquals(1, resultSet.getInt(1));
		} finally {
			if (hsqlConnection != null)
				hsqlConnection.close();
		}
	}
}
