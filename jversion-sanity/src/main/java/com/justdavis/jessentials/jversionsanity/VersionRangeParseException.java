package com.justdavis.jessentials.jversionsanity;

/**
 * Indicates that a {@link VersionRange} could not be parsed from a
 * {@link String} representation of a version range.
 */
public class VersionRangeParseException extends RuntimeException {
	private static final long serialVersionUID = 2176571051879639L;

	/**
	 * Constructor.
	 * 
	 * @param versionRangeString
	 *            the {@link String} (supposed) representation of a version
	 *            range that could not be parsed.
	 * @param versionRangeImplementation
	 *            the {@link VersionRange} implementation class that parsing was
	 *            attempted for/with.
	 * @param cause
	 *            the other {@link Throwable} that terminated the parsing
	 *            attempt, or <code>null</code> if there was no such other
	 *            exception
	 */
	public VersionRangeParseException(String versionRangeString,
			Class<? extends VersionRange<?>> versionRangeImplementation,
			Throwable cause) {
		super(String.format("The version range string '%s' could not be parsed"
				+ " into a %s instance.", versionRangeString,
				versionRangeImplementation), cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param versionRangeString
	 *            the {@link String} (supposed) representation of a version
	 *            range that could not be parsed.
	 * @param versionRangeImplementation
	 *            the {@link VersionRange} implementation class that parsing was
	 *            attempted for/with.
	 */
	public VersionRangeParseException(String versionRangeString,
			Class<? extends VersionRange<?>> versionRangeImplementation) {
		this(versionRangeString, versionRangeImplementation, null);
	}
}
