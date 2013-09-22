package com.justdavis.jessentials.jversionsanity;

import com.justdavis.jessentials.jversionsanity.bareint.IntegerVersion;
import com.justdavis.jessentials.jversionsanity.bareint.IntegerVersionRange;

/**
 * Enumerates the {@link VersioningScheme}s built-in to this library.
 */
public enum StandardVersioningSchemes {
	INTEGER(new VersioningScheme(IntegerVersion.class,
			IntegerVersionRange.class));

	private final VersioningScheme scheme;

	/**
	 * Enum constant constructor.
	 * 
	 * @param scheme
	 *            the value to use for {@link #getVersioningScheme()}
	 */
	private StandardVersioningSchemes(VersioningScheme scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the {@link VersioningScheme} associated with this
	 *         {@link StandardVersioningSchemes} constant
	 */
	public VersioningScheme getVersioningScheme() {
		return this.scheme;
	}
}
