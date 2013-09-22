package com.justdavis.jessentials.jversionsanity.bareint;

import com.justdavis.jessentials.jversionsanity.VersionParseException;
import com.justdavis.jessentials.jversionsanity.VersionParser;

/**
 * The {@link VersionParser} for {@link IntegerVersion}s.
 */
public final class IntegerVersionParser implements
		VersionParser<IntegerVersion> {
	/**
	 * @see com.justdavis.jessentials.jversionsanity.VersionParser#parseVersion(java.lang.String)
	 */
	@Override
	public IntegerVersion parseVersion(String versionString)
			throws VersionParseException {
		return new IntegerVersion(versionString);
	}
}
