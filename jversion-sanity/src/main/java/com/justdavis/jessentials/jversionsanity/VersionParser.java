package com.justdavis.jessentials.jversionsanity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.justdavis.jessentials.exceptions.UncheckedIllegalAccessException;
import com.justdavis.jessentials.exceptions.UncheckedInstantiationException;
import com.justdavis.jessentials.exceptions.UncheckedNoSuchMethodException;

/**
 * <p>
 * Parses {@link String} representations of {@link Version} and
 * {@link VersionRange} instances.
 * </p>
 * <p>
 * <strong>Design Note:</strong> As each {@link Version} and
 * {@link VersionRange} implementation is required to have a constructor that
 * takes a single {@link String} as an argument, this class is a bit
 * unnecessary. It's provided as a convenience on the off-chance that someone
 * needs to parse multiple different types of {@link VersioningScheme}s.
 * </p>
 */
public final class VersionParser {
	private static final Map<VersioningScheme, Constructor<? extends Version>> versionConstructorsCache = new HashMap<VersioningScheme, Constructor<? extends Version>>();
	private static final Map<VersioningScheme, Constructor<? extends VersionRange<?>>> rangeConstructorsCache = new HashMap<VersioningScheme, Constructor<? extends VersionRange<?>>>();

	/**
	 * Parses a {@link Version} of the specified {@link VersioningScheme} from
	 * the specified {@link String} representation of it.
	 * 
	 * @param scheme
	 *            the {@link VersioningScheme} that the {@link String} is part
	 *            of
	 * @param versionString
	 *            the {@link String} representation of the {@link Version} to be
	 *            parsed
	 * @return a {@link Version} instance equivalent to the {@link String}
	 *         representation that was parsed
	 * @throws VersionParseException
	 *             A {@link VersionParseException} will be thrown if an error
	 *             occurs that prevents parsing the version.
	 */
	public Version parseVersion(VersioningScheme scheme, String versionString)
			throws VersionParseException {
		if (scheme == null)
			throw new IllegalArgumentException();

		Constructor<? extends Version> versionImplConstructor = getVersionConstructor(scheme);
		try {
			Version parsedVersion = versionImplConstructor
					.newInstance(versionString);
			return parsedVersion;
		} catch (InvocationTargetException e) {
			/*
			 * This might occur if the constructor is called correctly, but
			 * nonetheless throws an exception (e.g. if it itself throws a
			 * VersionParseException).
			 */
			throw new VersionParseException(versionString,
					scheme.getVersionImpl(), e);
		} catch (InstantiationException e) {
			/*
			 * This will not occur unless there's an error somewhere right here
			 * in this class.
			 */
			throw new UncheckedInstantiationException(e);
		} catch (IllegalAccessException e) {
			/*
			 * This will only occur if the Java security settings have been
			 * misconfigured.
			 */
			throw new UncheckedIllegalAccessException(e);
		}
	}

	/**
	 * Looks up the {@link Version} implementation/instance constructor for the
	 * specified {@link VersioningScheme}. Caches results to speed later
	 * lookups.
	 * 
	 * @param scheme
	 *            the {@link VersioningScheme} to find the parsing
	 *            {@link Version} constructor for
	 * @return the {@link Version} implementation/instance constructor for the
	 *         specified {@link VersioningScheme}
	 */
	private Constructor<? extends Version> getVersionConstructor(
			VersioningScheme scheme) {
		// Try to look up the constructor from the cache first.
		Constructor<? extends Version> versionImplConstructor = versionConstructorsCache
				.get(scheme);
		if (versionImplConstructor != null)
			return versionImplConstructor;

		// Look up the constructor the hard way and stick it into the cache
		Class<? extends Version> versionImpl = scheme.getVersionImpl();
		try {
			versionImplConstructor = versionImpl.getConstructor(String.class);
			versionConstructorsCache.put(scheme, versionImplConstructor);
		} catch (NoSuchMethodException e) {
			throw new UncheckedNoSuchMethodException(e);
		}

		return versionImplConstructor;
	}

	/**
	 * Parses a {@link VersionRange} of the specified {@link VersioningScheme}
	 * from the specified {@link String} representation of it.
	 * 
	 * @param scheme
	 *            the {@link VersioningScheme} that the {@link String} is part
	 *            of
	 * @param rangeString
	 *            the {@link String} representation of the {@link VersionRange}
	 *            to be parsed
	 * @return a {@link VersionRange} instance equivalent to the {@link String}
	 *         representation that was parsed
	 * @throws VersionRangeParseException
	 *             A {@link VersionRangeParseException} will be thrown if an
	 *             error occurs that prevents parsing the range.
	 */
	public VersionRange<?> parseRange(VersioningScheme scheme,
			String rangeString) throws VersionRangeParseException {
		if (scheme == null)
			throw new IllegalArgumentException(
					"VersioningScheme must be specified.");
		if (scheme.getRangeImpl() == null)
			throw new IllegalArgumentException(
					"VersioningScheme does not have a VersionRange implementation.");

		Constructor<? extends VersionRange<?>> rangeImplConstructor = getRangeConstructor(scheme);
		try {
			VersionRange<?> parsedRange = rangeImplConstructor
					.newInstance(rangeString);
			return parsedRange;
		} catch (InvocationTargetException e) {
			/*
			 * This might occur if the constructor is called correctly, but
			 * nonetheless throws an exception (e.g. if it itself throws a
			 * VersionRangeParseException).
			 */
			throw new VersionRangeParseException(rangeString,
					scheme.getRangeImpl(), e);
		} catch (InstantiationException e) {
			/*
			 * This will not occur unless there's an error somewhere right here
			 * in this class.
			 */
			throw new UncheckedInstantiationException(e);
		} catch (IllegalAccessException e) {
			/*
			 * This will only occur if the Java security settings have been
			 * misconfigured.
			 */
			throw new UncheckedIllegalAccessException(e);
		}
	}

	/**
	 * Looks up the {@link VersionRange} implementation/instance constructor for
	 * the specified {@link VersioningScheme}. Caches results to speed later
	 * lookups.
	 * 
	 * @param scheme
	 *            the {@link VersioningScheme} to find the parsing
	 *            {@link VersionRange} constructor for
	 * @return the {@link Version} implementation/instance constructor for the
	 *         specified {@link VersioningScheme}
	 */
	private Constructor<? extends VersionRange<?>> getRangeConstructor(
			VersioningScheme scheme) {
		// Try to look up the constructor from the cache first.
		Constructor<? extends VersionRange<?>> rangeImplConstructor = rangeConstructorsCache
				.get(scheme);
		if (rangeImplConstructor != null)
			return rangeImplConstructor;

		// Look up the constructor the hard way and stick it into the cache
		Class<? extends VersionRange<?>> rangeImpl = scheme.getRangeImpl();
		try {
			rangeImplConstructor = rangeImpl.getConstructor(String.class);
			rangeConstructorsCache.put(scheme, rangeImplConstructor);
		} catch (NoSuchMethodException e) {
			throw new UncheckedNoSuchMethodException(e);
		}

		return rangeImplConstructor;
	}
}
