package com.justdavis.karl.misc.xml;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import com.justdavis.karl.misc.exceptions.BadCodeMonkeyException;

/**
 * A simple {@link NamespaceContext} implementation.
 */
public final class SimpleNamespaceContext implements NamespaceContext {
	private final List<NamespaceBinding> bindings;

	/**
	 * Constructs a new {@link SimpleNamespaceContext} instance.
	 * 
	 * @param bindings
	 *            the {@link NamespaceBinding}s to use in the
	 *            {@link SimpleNamespaceContext}
	 */
	public SimpleNamespaceContext(NamespaceBinding... bindings) {
		// Create a List with the specified NamespaceBindings.
		List<NamespaceBinding> bindingsList = new LinkedList<SimpleNamespaceContext.NamespaceBinding>();
		for (NamespaceBinding binding : bindings)
			bindingsList.add(binding);

		// Add in the default mappings defined in the interface.
		bindingsList.add(new NamespaceBinding(XMLConstants.XML_NS_PREFIX,
				XMLConstants.XML_NS_URI));
		bindingsList.add(new NamespaceBinding(XMLConstants.XMLNS_ATTRIBUTE,
				XMLConstants.XMLNS_ATTRIBUTE_NS_URI));

		// Sanity check: same prefix bound twice.
		Set<String> prefixes = new HashSet<String>();
		for (NamespaceBinding binding : bindingsList) {
			if (prefixes.contains(binding.getPrefix()))
				throw new BadCodeMonkeyException("Prefix bound twice: "
						+ binding.getPrefix());
			prefixes.add(binding.getPrefix());
		}

		this.bindings = Collections.unmodifiableList(bindingsList);
	}

	/**
	 * Constructs a new {@link SimpleNamespaceContext} instance.
	 * 
	 * @param prefix
	 *            the single XML prefix to map
	 * @param namespace
	 *            the single XML namespace URI to map
	 */
	public SimpleNamespaceContext(String prefix, String namespace) {
		this(new NamespaceBinding(prefix, namespace));
	}

	/**
	 * Constructs a new {@link SimpleNamespaceContext} instance.
	 * 
	 * @param namespace
	 *            the single XML namespace URI to map to the default prefix (
	 *            <code>""</code>)
	 */
	public SimpleNamespaceContext(String namespace) {
		this(new NamespaceBinding(XMLConstants.DEFAULT_NS_PREFIX, namespace));
	}

	/**
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
	@Override
	public String getNamespaceURI(String prefix) {
		/*
		 * If the specified prefix is null, the interface mandates that we throw
		 * the following exception.
		 */
		if (prefix == null)
			throw new IllegalArgumentException();

		// Is the specified prefix bound?
		for (NamespaceBinding binding : bindings)
			if (binding.getPrefix().equals(prefix))
				return binding.getNamespace();

		/*
		 * If the prefix isn't bound, the interface mandates that we return the
		 * following constant.
		 */
		return XMLConstants.NULL_NS_URI;
	}

	/**
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
	@Override
	public String getPrefix(String namespaceURI) {
		return (String) getPrefixes(namespaceURI).next();
	}

	/**
	 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<?> getPrefixes(String namespaceURI) {
		/*
		 * If the specified namespace is null, the interface mandates that we
		 * throw the following exception.
		 */
		if (namespaceURI == null)
			throw new IllegalArgumentException();

		// Is the specified prefix bound?
		for (NamespaceBinding binding : bindings)
			if (binding.getNamespace().equals(namespaceURI))
				return Collections.unmodifiableList(
						Arrays.asList(binding.getPrefix())).iterator();

		/*
		 * If the prefix isn't bound, the interface mandates that we return the
		 * following constant.
		 */
		return Collections.unmodifiableList(Collections.EMPTY_LIST).iterator();
	}

	/**
	 * Represents an XML namespace-prefix pair, for use with
	 * {@link SimpleNamespaceContext}.
	 */
	public static final class NamespaceBinding {
		private final String prefix;
		private final String namespace;

		/**
		 * Constructs a new new {@link NamespaceBinding}.
		 * 
		 * @param prefix
		 *            the value to use for {@link #getPrefix()}
		 * @param namespace
		 *            the value to use for {@link #getNamespace()}
		 */
		public NamespaceBinding(String prefix, String namespace) {
			this.prefix = prefix;
			this.namespace = namespace;
		}

		/**
		 * @return the XML namespace prefix
		 */
		public String getPrefix() {
			return prefix;
		}

		/**
		 * @return the XML namespace URI
		 */
		public String getNamespace() {
			return namespace;
		}
	}
}
