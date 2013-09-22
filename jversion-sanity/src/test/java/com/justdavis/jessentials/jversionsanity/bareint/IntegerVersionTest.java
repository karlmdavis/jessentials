package com.justdavis.jessentials.jversionsanity.bareint;

import org.junit.Test;

import com.justdavis.jessentials.jversionsanity.AbstractVersionTest;
import com.justdavis.jessentials.jversionsanity.StandardVersioningSchemes;
import com.justdavis.jessentials.jversionsanity.Version;
import com.justdavis.jessentials.jversionsanity.VersionParseException;
import com.justdavis.jessentials.jversionsanity.VersioningScheme;

/**
 * Unit tests for {@link IntegerVersion}.
 */
public final class IntegerVersionTest extends AbstractVersionTest {
	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionTest#getVersioningScheme()
	 */
	@Override
	protected VersioningScheme getVersioningScheme() {
		return StandardVersioningSchemes.INTEGER.getVersioningScheme();
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionTest#getSample1()
	 */
	@Override
	protected IntegerVersion getSample1() {
		return new IntegerVersion(0);
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionTest#getSample1String()
	 */
	@Override
	protected String getSample1String() {
		return " 00 ";
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionTest#getSample2()
	 */
	@Override
	protected Version getSample2() {
		return new IntegerVersion(42);
	}

	/**
	 * Verifies that {@link IntegerVersion#IntegerVersion(Integer)} fails as
	 * expected when passed a <code>null</code> {@link Integer}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructWithNullInteger() {
		new IntegerVersion((Integer) null);
	}

	/**
	 * Verifies that {@link IntegerVersion#IntegerVersion(String)} fails as
	 * expected when passed an invalid version string to parse.
	 */
	@Test(expected = VersionParseException.class)
	public void parseInvalidString() {
		new IntegerVersion("4.1");
	}
}
