/**
 * An {@link com.justdavis.karl.misc.datasources.IDataSourceConnector}
 * implementation for <a href="http://hsqldb.org/">HSQL</a> databases.
 */
@XmlSchema(namespace = XmlNamespace.JE_DATASOURCES, xmlns = {
		@XmlNs(prefix = "jed", namespaceURI = XmlNamespace.JE_DATASOURCES) }, elementFormDefault = XmlNsForm.QUALIFIED)
package com.justdavis.karl.misc.datasources.hsql;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

import com.justdavis.karl.misc.datasources.XmlNamespace;
