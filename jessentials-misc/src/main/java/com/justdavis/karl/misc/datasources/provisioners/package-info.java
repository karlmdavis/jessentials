/**
 * The root package for the database provisioning API. This API allows for the 
 * creation, loading, etc. of database instances. While 
 * {@link com.justdavis.karl.misc.datasources.provisioners.IDataSourceProvisioner} 
 * is the primary interface of the API, the 
 * {@link com.justdavis.karl.misc.datasources.provisioners.DataSourceProvisionersManager}
 * class is provided as a more user-friendly entry point.
 */
@XmlSchema(namespace = XmlNamespace.JE_DATASOURCES, xmlns = { @XmlNs(prefix = "jed", namespaceURI = XmlNamespace.JE_DATASOURCES) }, elementFormDefault = XmlNsForm.QUALIFIED)
package com.justdavis.karl.misc.datasources.provisioners;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

import com.justdavis.karl.misc.datasources.XmlNamespace;

