/**
 * Provides APIs related to {@link javax.sql.DataSource}s. The
 * {@link com.justdavis.karl.misc.datasources.IDataSourceConnector} interface is
 * the main "entry point" for this API.
 */
@XmlSchema(namespace = XmlNamespace.JE_DATASOURCES, xmlns = {
		@XmlNs(prefix = "jed", namespaceURI = XmlNamespace.JE_DATASOURCES) }, elementFormDefault = XmlNsForm.QUALIFIED)
package com.justdavis.karl.misc.datasources;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
