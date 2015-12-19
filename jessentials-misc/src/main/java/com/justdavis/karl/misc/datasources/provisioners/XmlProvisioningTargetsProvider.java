package com.justdavis.karl.misc.datasources.provisioners;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.justdavis.karl.misc.datasources.IDataSourceCoordinates;
import com.justdavis.karl.misc.exceptions.unchecked.UncheckedJaxbException;

/**
 * This {@link IProvisioningTargetsProvider} implementation reads in the
 * available {@link IProvisioningTarget}s from an XML file.
 */
public final class XmlProvisioningTargetsProvider implements IProvisioningTargetsProvider {
	private final DataSourceProvisionersManager provisionersManager;
	private final ProvisioningTargets targets;

	/**
	 * Constructs a new {@link XmlProvisioningTargetsProvider} instance.
	 * 
	 * @param provisionersManager
	 *            the {@link DataSourceProvisionersManager} to use
	 * @param targetsDocUrl
	 *            a {@link URL} to an XML document containing a JAX-B-marshalled
	 *            {@link ProvisioningTargets} instance
	 */
	public XmlProvisioningTargetsProvider(DataSourceProvisionersManager provisionersManager, URL targetsDocUrl) {
		this.provisionersManager = provisionersManager;
		this.targets = parseTargetsDoc(targetsDocUrl);
	}

	/**
	 * @param targetsDocUrl
	 *            the URL for the document to parse, which must contain a
	 *            JAX-B-marshalled representation of a
	 *            {@link ProvisioningTargets} instance
	 * @return the {@link ProvisioningTargets} parsed from the specified
	 *         document
	 */
	private ProvisioningTargets parseTargetsDoc(URL targetsDocUrl) {
		try {
			// Create the List of classes that might be unmarshalled.
			List<Class<?>> jaxbClasses = new LinkedList<Class<?>>();
			jaxbClasses.add(ProvisioningTargets.class);
			for (IDataSourceProvisioner<? extends IDataSourceCoordinates, ? extends IProvisioningTarget, ? extends IProvisioningRequest> provisioner : provisionersManager
					.getProvisioners())
				jaxbClasses.add(provisioner.getTargetType());

			// Create the Unmarshaller needed.
			JAXBContext jaxbContext = JAXBContext.newInstance(jaxbClasses.toArray(new Class<?>[jaxbClasses.size()]));
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			// Try to unmarshall the config file.
			ProvisioningTargets parsedTargets = (ProvisioningTargets) unmarshaller.unmarshal(targetsDocUrl);

			return parsedTargets;
		} catch (JAXBException e) {
			throw new UncheckedJaxbException(e);
		}
	}

	/**
	 * @see com.justdavis.karl.misc.datasources.provisioners.IProvisioningTargetsProvider#findTarget(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IProvisioningTarget> T findTarget(Class<T> targetType)
			throws UnmatchedProvisioningTargetException {
		for (IProvisioningTarget target : targets.getTargets())
			if (target.getClass().equals(targetType))
				return (T) target;

		// No match found.
		throw new UnmatchedProvisioningTargetException(targetType);
	}

	/**
	 * Represents a list of {@link IProvisioningTarget}s. Is unmarshallable via
	 * JAX-B.
	 */
	@XmlRootElement
	public static final class ProvisioningTargets {
		@XmlElementRef
		private final List<IProvisioningTarget> targets;

		/**
		 * This no-arg default constructor is required by JAX-B.
		 */
		@SuppressWarnings("unused")
		private ProvisioningTargets() {
			this.targets = null;
		}

		/**
		 * Constructs a new {@link ProvisioningTargets} instance.
		 * 
		 * @param targets
		 *            the value to use for {@link #getTargets()}
		 */
		public ProvisioningTargets(List<IProvisioningTarget> targets) {
			this.targets = targets;
		}

		/**
		 * @return the {@link List} of {@link IProvisioningTarget}s
		 */
		public List<IProvisioningTarget> getTargets() {
			return targets;
		}
	}
}
