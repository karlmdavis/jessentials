package com.justdavis.jessentials.jversionsanity;

import com.justdavis.jessentials.jversionsanity.bareint.IntegerVersion;

/**
 * <p>
 * Each implementation of this {@link Version} interface acts as a
 * representation of a version "number" in a given versioning scheme.
 * <p>
 * Regardless of the versioning scheme, all {@link Version}
 * instances/implementation <strong>will</strong> provide:
 * </p>
 * <ul>
 * <li>A constructor that accepts a single {@link String} parameter as an
 * argument, from which the represented version will be parsed. Any parsing
 * failures will result in a {@link VersionParseException}.</li>
 * <li>A corresponding {@link VersionParser} implementation.</li>
 * <li>Use of {@link #toString()} such that {@link Version} instances can be
 * "round tripped" to/from a {@link String} with no change to the {@link String}
 * .</li>
 * <li>Implement {@link Comparable#compareTo(Object)} such that versions of its
 * own type can be compared.</li>
 * <li>Implement {@link Comparable#compareTo(Object)} such that trying to
 * compare a version of a different type throws a {@link ClassCastException}.
 * </li>
 * <li>Use of {@link #equals(Object)} such that {@link Version}s of the same
 * type/implementation can be compared for equivalency.</li>
 * </ul>
 * <p>
 * All {@link Version} instances/implementations explicitly <strong>will
 * not</strong> do the following:
 * </p>
 * <ul>
 * <li>Allow themselves to be mutated.</li>
 * </ul>
 * <p>
 * <strong>Note for implementors:</strong> Each implementation of this interface
 * must pass all of the unit tests provided by
 * <code>com.justdavis.jessentials.jversionsanity.AbstractVersionTest</code>.
 * </p>
 */
public interface Version extends Comparable<Version> {
	/**
	 * <p>
	 * The {@link #toString()} method will be implemented as follows:
	 * </p>
	 * <ul>
	 * <li>If this {@link Version} instance was constructed by parsing a
	 * {@link String} representation, this method will return a {@link String}
	 * exactly equal to that original {@link String}.</li>
	 * <li>If this {@link Version} instance was constructed some other way (e.g.
	 * via a constructor that takes something other than just a {@link String}),
	 * this method will return a {@link String} that represents this
	 * {@link Version}.</li>
	 * </ul>
	 * <p>
	 * In either case, if the {@link String} produced by this method is then
	 * parsed back into a {@link Version} instance of the same type, that
	 * {@link Version} will return <code>true</code> when passed to this
	 * instance's {@link #equals(Object)} method.
	 * </p>
	 */
	@Override
	public String toString();

	/**
	 * <p>
	 * The {@link #equals(Object)} method will be implemented as follows:
	 * </p>
	 * <ul>
	 * <li>If the specified object is not of the same {@link Version} type as
	 * this one, the method will return <code>false</code>.</li>
	 * <li>If the specified object is of the same {@link Version} type as this
	 * one, the two objects will be compared for equivalency. For instance,
	 * using the {@link IntegerVersion} implementation, all of the following
	 * versions would be equivalent: "<code>1</code>", "<code>01</code>", and "
	 * <code> 1 </code>" (note the spaces). Of course, each versioning scheme
	 * must define what equivalency means for itself.</li>
	 * </ul>
	 * 
	 * @param other
	 *            the object to compare to this {@link Version} instance
	 */
	@Override
	public boolean equals(Object other);

	/**
	 * <p>
	 * The {@link #compareTo(Version)} method will be implemented as follows:
	 * </p>
	 * <ul>
	 * <li>If the specified object is not of the same {@link Version} type as
	 * this one, the method will throw a {@link ClassCastException}.</li>
	 * <li>If the specified object is of the same {@link Version} type as this
	 * one, the two objects will be compared in terms of which is "lower" versus
	 * "higher". Each versioning scheme is free to define what that ordering is.
	 * </li>
	 * </ul>
	 * 
	 * @param otherVersion
	 *            the other {@link Version} instance to compare to this one
	 * @throws ClassCastException
	 *             A {@link ClassCastException} will be thrown if the specified
	 *             {@link Version} parameter is not of the exact same type as
	 *             this one.
	 */
	@Override
	public int compareTo(Version otherVersion) throws ClassCastException;
}
