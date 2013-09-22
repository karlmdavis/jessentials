package com.justdavis.jessentials.jversionsanity;

/**
 * Indicates that a {@link Version} could not be parsed from a {@link String}
 * representation of a version.
 */
public class VersionParseException extends RuntimeException {
	private static final long serialVersionUID = -6120863288693205230L;

	/**
	 * Constructor.
	 * 
	 * @param versionString
	 *            the {@link String} (supposed) representation of a version that
	 *            could not be parsed.
	 * @param versionImplementation
	 *            the {@link Version} implementation class that parsing was
	 *            attempted for/with.
	 * @param cause
	 *            the other {@link Throwable} that terminated the parsing
	 *            attempt, or <code>null</code> if there was no such other
	 *            exception
	 */
	public VersionParseException(String versionString,
			Class<? extends Version> versionImplementation, Throwable cause) {
		super(
				String.format("The version string '%s' could not be parsed"
						+ " into a %s instance.", versionString,
						versionImplementation), cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param versionString
	 *            the {@link String} (supposed) representation of a version that
	 *            could not be parsed.
	 * @param versionImplementation
	 *            the {@link Version} implementation class that parsing was
	 *            attempted for/with.
	 */
	public VersionParseException(String versionString,
			Class<? extends Version> versionImplementation) {
		this(versionString, versionImplementation, null);
	}
}
