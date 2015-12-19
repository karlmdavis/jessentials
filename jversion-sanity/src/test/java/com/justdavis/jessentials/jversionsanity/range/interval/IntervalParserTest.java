package com.justdavis.jessentials.jversionsanity.range.interval;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.jessentials.jversionsanity.range.VersionRangeParseException;

/**
 * Unit tests for {@link IntervalParser}.
 */
public final class IntervalParserTest {
	/**
	 * Tests {@link IntervalParser#IntervalNotationParser()}.
	 */
	@Test
	public void constructor() {
		// Shouldn't throw an exception:
		new IntervalParser();
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * a "bare" version.
	 */
	@Test
	public void parseRange_version() {
		IntervalParser parser = new IntervalParser();
		Interval<String> interval = parser.parseVersionRange("1.2");
		Assert.assertNotNull(interval);
		Assert.assertEquals(IntervalBoundaryType.OMITTED, interval.getTypeLower());
		Assert.assertEquals("1.2", interval.getVersionLower());
		Assert.assertNull(interval.getVersionUpper());
		Assert.assertEquals(IntervalBoundaryType.OMITTED, interval.getTypeUpper());
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * a single version inside boundary characters.
	 */
	@Test
	public void parseRange_versionWithBoundaries() {
		IntervalParser parser = new IntervalParser();
		Interval<String> interval = parser.parseVersionRange("[1.2)");
		Assert.assertNotNull(interval);
		Assert.assertEquals(IntervalBoundaryType.INCLUSIVE, interval.getTypeLower());
		Assert.assertEquals("1.2", interval.getVersionLower());
		Assert.assertEquals("1.2", interval.getVersionUpper());
		Assert.assertEquals(IntervalBoundaryType.EXCLUSIVE, interval.getTypeUpper());
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * a single version inside boundary characters.
	 */
	@Test
	public void parseRange_versionWithBoundariesAndComma() {
		IntervalParser parser = new IntervalParser();
		Interval<String> interval = parser.parseVersionRange("(1.2,]");
		Assert.assertNotNull(interval);
		Assert.assertEquals(IntervalBoundaryType.EXCLUSIVE, interval.getTypeLower());
		Assert.assertEquals("1.2", interval.getVersionLower());
		Assert.assertNull(interval.getVersionUpper());
		Assert.assertEquals(IntervalBoundaryType.INCLUSIVE, interval.getTypeUpper());
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * two versions inside boundary characters.
	 */
	@Test
	public void parseRange_versions() {
		IntervalParser parser = new IntervalParser();
		Interval<String> interval = parser.parseVersionRange("(1.2,3.4]");
		Assert.assertNotNull(interval);
		Assert.assertEquals(IntervalBoundaryType.EXCLUSIVE, interval.getTypeLower());
		Assert.assertEquals("1.2", interval.getVersionLower());
		Assert.assertEquals("3.4", interval.getVersionUpper());
		Assert.assertEquals(IntervalBoundaryType.INCLUSIVE, interval.getTypeUpper());
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * two versions inside boundary characters, with a whole bunch of useless
	 * whitespace around the tokens.
	 */
	@Test
	public void parseRange_versionsAndOutsideWhitespace() {
		IntervalParser parser = new IntervalParser();
		Interval<String> interval = parser.parseVersionRange("  (  1.2  ,  3.4  ]  ");
		Assert.assertNotNull(interval);
		Assert.assertEquals(IntervalBoundaryType.EXCLUSIVE, interval.getTypeLower());
		Assert.assertEquals("1.2", interval.getVersionLower());
		Assert.assertEquals("3.4", interval.getVersionUpper());
		Assert.assertEquals(IntervalBoundaryType.INCLUSIVE, interval.getTypeUpper());
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * two versions inside boundary characters, with a whole bunch of whitespace
	 * in the version tokens.
	 */
	@Test
	public void parseRange_versionsWithInternalWhitespace() {
		IntervalParser parser = new IntervalParser();
		Interval<String> interval = parser.parseVersionRange("  (  1  .  2  ,  3  .  4  ]  ");
		Assert.assertNotNull(interval);
		Assert.assertEquals(IntervalBoundaryType.EXCLUSIVE, interval.getTypeLower());
		Assert.assertEquals("1  .  2", interval.getVersionLower());
		Assert.assertEquals("3  .  4", interval.getVersionUpper());
		Assert.assertEquals(IntervalBoundaryType.INCLUSIVE, interval.getTypeUpper());
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * two versions inside boundary characters, with a whole bunch of random
	 * characters in the versions.
	 */
	@Test
	public void parseRange_versionsWithAlphasAndSuch() {
		IntervalParser parser = new IntervalParser();
		Interval<String> interval = parser.parseVersionRange("(1a.e2-be, 3*@.Gk4e]");
		Assert.assertNotNull(interval);
		Assert.assertEquals(IntervalBoundaryType.EXCLUSIVE, interval.getTypeLower());
		Assert.assertEquals("1a.e2-be", interval.getVersionLower());
		Assert.assertEquals("3*@.Gk4e", interval.getVersionUpper());
		Assert.assertEquals(IntervalBoundaryType.INCLUSIVE, interval.getTypeUpper());
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * a range with mismatched boundaries (one boundary present and another
	 * omitted).
	 */
	@Test(expected = VersionRangeParseException.class)
	public void parseRange_unbalancedBoundaries() {
		IntervalParser parser = new IntervalParser();
		parser.parseVersionRange("(1");
	}

	/**
	 * Tests {@link IntervalParser#parseVersionRange(String, Class)} when passed
	 * a range with extra boundary characters.
	 */
	@Test(expected = VersionRangeParseException.class)
	public void parseRange_extraBoundaries() {
		IntervalParser parser = new IntervalParser();
		parser.parseVersionRange("[1.2)3.4]");
	}
}
