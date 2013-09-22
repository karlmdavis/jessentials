package com.justdavis.jessentials.jversionsanity;

/**
 * <p>
 * Each implementation of this {@link VersionRange} interface acts as a
 * representation of a version range in a given versioning scheme. A version
 * range is a constraint/filter that specifies a set of allowable/matching
 * {@link Version}s. Note that not all versioning schemes will provide a
 * {@link VersionRange} implementation.
 * <p>
 * Regardless of the versioning scheme, all {@link VersionRange}
 * instances/implementation <strong>will</strong> provide:
 * </p>
 * <ul>
 * <li>A constructor that accepts a single {@link String} parameter as an
 * argument, from which the represented version range will be parsed. Any
 * parsing failures will result in a {@link VersionRangeParseException}.</li>
 * <li>Support for parsing from a {@link String} representation of the version
 * into an instance of the {@link VersionRange} subtype/implementation via
 * {@link VersionParser#parseRange(VersioningScheme, String)}.</li>
 * <li>Use of {@link #toString()} such that {@link VersionRange} instances can
 * be "round tripped" to/from a {@link String} with no change to the
 * {@link String} .</li>
 * <li>Use of {@link #equals(Object)} such that {@link VersionRange}s of the
 * same type/implementation can be compared for equivalency.</li>
 * </ul>
 * <li>Working implementations of the methods specified in this interface.</li>
 * </ul>
 * <p>
 * All {@link VersionRange} instances/implementations explicitly <strong>will
 * not</strong> do the following:
 * </p>
 * <ul>
 * <li>Allow themselves to be mutated.</li>
 * </ul>
 * <p>
 * <strong>Note for implementors:</strong> Each implementation of this interface
 * must pass all of the unit tests provided by
 * <code>com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest</code>
 * .
 * </p>
 * 
 * @param <V>
 *            the {@link Version} implementation that this {@link VersionRange}
 *            implementation is associated with
 */
public interface VersionRange<V extends Version> {
	/**
	 * Determines whether or not the specified {@link Version} complies
	 * with/matches this {@link VersionRange}.
	 * 
	 * @param version
	 *            the {@link Version} to check against this {@link VersionRange}
	 * @return <code>true</code> if the specified {@link Version} complies
	 *         with/matches this {@link VersionRange}, <code>false</code> if it
	 *         does not
	 */
	boolean matches(V version);
}
