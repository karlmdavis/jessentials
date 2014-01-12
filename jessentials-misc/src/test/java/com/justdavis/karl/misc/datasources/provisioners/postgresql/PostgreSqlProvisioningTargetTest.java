package com.justdavis.karl.misc.datasources.provisioners.postgresql;

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
import com.justdavis.karl.misc.datasources.postgresql.PostgreSqlCoordinates;
import com.justdavis.karl.misc.xml.SimpleNamespaceContext;

/**
 * Unit tests for {@link PostgreSqlProvisioningTarget}.
 */
public final class PostgreSqlProvisioningTargetTest {
	/**
	 * Ensures that {@link PostgreSqlProvisioningTarget} instances can be
	 * marshalled.
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
				.newInstance(PostgreSqlProvisioningTarget.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		// Create the instance to be converted to XML.
		PostgreSqlCoordinates coords = new PostgreSqlCoordinates(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true");
		PostgreSqlProvisioningTarget target = new PostgreSqlProvisioningTarget(
				coords);

		// Convert it to XML.
		DOMResult domResult = new DOMResult();
		marshaller.marshal(target, domResult);

		// Verify the results.
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(new SimpleNamespaceContext("jed",
				XmlNamespace.JE_DATASOURCES));
		Node targetNode = (Node) xpath.evaluate(
				"/jed:postgreSqlProvisioningTarget/jed:serverCoords/jed:url",
				domResult.getNode(), XPathConstants.NODE);
		Assert.assertNotNull(targetNode);
		Assert.assertEquals(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true",
				targetNode.getTextContent());
	}

	/**
	 * Ensures that {@link PostgreSqlProvisioningTarget} instances can be
	 * unmarshalled.
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
				.newInstance(PostgreSqlProvisioningTarget.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		// Get the XML to be converted.
		URL sourceXmlUrl = Thread.currentThread().getContextClassLoader()
				.getResource("sample-xml/provisioningTarget-postgreSql-1.xml");

		// Parse the XML to an object.
		PostgreSqlProvisioningTarget parsedTarget = (PostgreSqlProvisioningTarget) unmarshaller
				.unmarshal(sourceXmlUrl);

		// Verify the results.
		Assert.assertNotNull(parsedTarget);
		Assert.assertEquals(
				"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true",
				parsedTarget.getServerCoords().getUrl());
	}
}
