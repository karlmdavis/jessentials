package com.justdavis.karl.misc.datasources.provisioners.hsql;

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
 * Unit tests for {@link HsqlProvisioningTarget}.
 */
public final class HsqlProvisioningTargetTest {
	/**
	 * Ensures that {@link HsqlProvisioningTarget} instances can be marshalled
	 * (even though they won't have any content).
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
				.newInstance(HsqlProvisioningTarget.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		// Create the instance to be converted to XML.
		HsqlProvisioningTarget target = new HsqlProvisioningTarget();

		// Convert it to XML.
		DOMResult domResult = new DOMResult();
		marshaller.marshal(target, domResult);

		// Verify the results.
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(new SimpleNamespaceContext("jed",
				XmlNamespace.JE_DATASOURCES));
		Node targetNode = (Node) xpath.evaluate("/jed:hsqlProvisioningTarget",
				domResult.getNode(), XPathConstants.NODE);
		Assert.assertNotNull(targetNode);
	}

	/**
	 * Ensures that {@link HsqlProvisioningTarget} instances can be unmarshalled
	 * (even though they won't have any content).
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
				.newInstance(HsqlProvisioningTarget.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		// Get the XML to be converted.
		URL sourceXmlUrl = Thread.currentThread().getContextClassLoader()
				.getResource("sample-xml/provisioningTarget-hsql-1.xml");

		// Parse the XML to an object.
		HsqlProvisioningTarget parsedTarget = (HsqlProvisioningTarget) unmarshaller
				.unmarshal(sourceXmlUrl);

		// Verify the results.
		Assert.assertNotNull(parsedTarget);
	}
}
