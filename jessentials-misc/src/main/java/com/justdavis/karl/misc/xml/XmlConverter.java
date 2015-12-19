package com.justdavis.karl.misc.xml;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.justdavis.karl.misc.exceptions.BadCodeMonkeyException;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedTransformerException;

/**
 * Contains utility methods for converting XML objects to other types.
 */
public final class XmlConverter {
	private final TransformerFactory transformerFactory;

	/**
	 * Constructs a new {@link XmlConverter} instance.
	 */
	public XmlConverter() {
		this.transformerFactory = TransformerFactory.newInstance();
	}

	/**
	 * @param xmlSource
	 *            the XML to convert to a {@link String}
	 * @return a {@link String}-serialized representation of the specified XML
	 */
	public String convertToString(Source xmlSource) {
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			StringWriter writer = new StringWriter();
			transformer.transform(xmlSource, new StreamResult(writer));

			return writer.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			// Shouldn't happen unless something's really wrong with the JRE.
			throw new BadCodeMonkeyException(e);
		} catch (TransformerException e) {
			throw new UncheckedTransformerException(e);
		}
	}

	/**
	 * @param xmlSource
	 *            the XML to convert to a {@link String}
	 * @return a {@link String}-serialized representation of the specified XML
	 */
	public String convertToString(Node xmlSource) {
		return convertToString(new DOMSource(xmlSource));
	}
}
