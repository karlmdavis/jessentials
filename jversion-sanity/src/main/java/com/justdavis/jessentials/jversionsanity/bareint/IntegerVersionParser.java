package com.justdavis.jessentials.jversionsanity.bareint;

import com.justdavis.jessentials.jversionsanity.VersionParseException;
import com.justdavis.jessentials.jversionsanity.VersionParser;
import com.justdavis.jessentials.jversionsanity.VersionRangeParseException;

/**
 * The {@link VersionParser} for {@link IntegerVersion}s.
 */
public final class IntegerVersionParser implements
		VersionParser<IntegerVersion, IntegerVersionRange> {
	/**
	 * @see com.justdavis.jessentials.jversionsanity.VersionParser#parseVersion(java.lang.String)
	 */
	@Override
	public IntegerVersion parseVersion(String versionString)
			throws VersionParseException {
		return new IntegerVersion(versionString);
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.VersionParser#parseRange(java.lang.String)
	 */
	@Override
	public IntegerVersionRange parseRange(String rangeString)
			throws VersionRangeParseException {
		return new IntegerVersionRange(rangeString);
	}
}
