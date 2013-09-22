package com.justdavis.jessentials.jversionsanity;

/**
 * Implementations of this interface can parse {@link String} representations of
 * their corresponding {@link Version} implementation into instances of those
 * {@link Version}s. Typically, the {@link String}s must match the format
 * produced by the {@link Version} implementation's {@link Version#toString()}
 * method.
 * 
 * @param <V>
 *            the {@link Version} implementation that this {@link VersionParser}
 *            is paired with
 */
public interface VersionParser<V extends Version> {
	/**
	 * Parses a {@link Version} from the specified {@link String} representation
	 * of it.
	 * 
	 * @param versionString
	 *            the {@link String} representation of the {@link Version} to be
	 *            parsed
	 * @return a {@link Version} instance equivalent to the {@link String}
	 *         representation that was parsed
	 * @throws VersionParseException
	 *             A {@link VersionParseException} will be thrown if an error
	 *             occurs that prevents parsing the version. Typically, this
	 *             indicates a malformed {@link String}.
	 */
	V parseVersion(String versionString) throws VersionParseException;
}
