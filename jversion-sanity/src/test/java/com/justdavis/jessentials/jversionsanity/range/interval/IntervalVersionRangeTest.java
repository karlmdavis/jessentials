package com.justdavis.jessentials.jversionsanity.range.interval;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.jessentials.jversionsanity.Version;
import com.justdavis.jessentials.jversionsanity.bareint.IntegerVersion;
import com.justdavis.jessentials.jversionsanity.bareint.IntegerVersionParser;
import com.justdavis.jessentials.jversionsanity.range.VersionRangeParseException;

/**
 * Unit tests for {@link IntervalVersionRange}. These tests use
 * {@link IntegerVersion} as the ranges' {@link Version} format, but should be
 * valid for any other format.
 */
public final class IntervalVersionRangeTest {
	/**
	 * Returns a new instance of the {@link IntervalVersionRange} represented by
	 * the specified {@link String}. Just a convenience method to eliminate some
	 * boilerplate.
	 * 
	 * @param rangeString
	 *            the {@link String} representation of an
	 *            {@link IntervalVersionRange} to be parsed
	 * @return a new instance of the {@link IntervalVersionRange} represented by
	 *         the specified {@link String}
	 */
	private IntervalVersionRange<IntegerVersion> parseIntegerRange(
			String rangeString) {
		return new IntervalVersionRange<IntegerVersion>(
				new IntegerVersionParser(), rangeString);
	}

	/**
	 * Verifies that
	 * {@link IntervalVersionRange#IntervalVersionRange(IntervalBoundaryType, Version, Version, IntervalBoundaryType)}
	 * fails as expected when passed invalid parameters.
	 */
	@Test
	public void constructWithInvalidParameters() {
		boolean exceptionWasThrown;

		exceptionWasThrown = false;
		try {
			new IntervalVersionRange<IntegerVersion>(
					IntervalBoundaryType.EXCLUSIVE, new IntegerVersion(1),
					new IntegerVersion(2), IntervalBoundaryType.OMITTED);
		} catch (IllegalArgumentException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);

		exceptionWasThrown = false;
		try {
			new IntervalVersionRange<IntegerVersion>(
					IntervalBoundaryType.INCLUSIVE, new IntegerVersion(2),
					new IntegerVersion(1), IntervalBoundaryType.INCLUSIVE);
		} catch (IllegalArgumentException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);
	}

	/**
	 * Ensures that the {@link IntervalVersionRange} implementation's parse
	 * constructor works correctly.
	 */
	@Test
	public void parseValidString() {
		Assert.assertEquals(new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.INCLUSIVE, new IntegerVersion(1),
				new IntegerVersion(4), IntervalBoundaryType.EXCLUSIVE),
				parseIntegerRange("[1,4)"));
		Assert.assertEquals(new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.EXCLUSIVE, new IntegerVersion(1),
				new IntegerVersion(4), IntervalBoundaryType.INCLUSIVE),
				parseIntegerRange("(1,4]"));

		Assert.assertEquals(new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.INCLUSIVE, new IntegerVersion(1), null,
				IntervalBoundaryType.INCLUSIVE), parseIntegerRange("[1,]"));
		Assert.assertEquals(new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.INCLUSIVE, null, new IntegerVersion(4),
				IntervalBoundaryType.INCLUSIVE), parseIntegerRange("[,4]"));

		Assert.assertEquals(new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.OMITTED, new IntegerVersion(2), null,
				IntervalBoundaryType.OMITTED), parseIntegerRange("2"));
		Assert.assertEquals(new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.INCLUSIVE, new IntegerVersion(2),
				new IntegerVersion(2), IntervalBoundaryType.INCLUSIVE),
				parseIntegerRange("[2]"));
		Assert.assertEquals(new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.EXCLUSIVE, new IntegerVersion(2),
				new IntegerVersion(2), IntervalBoundaryType.EXCLUSIVE),
				parseIntegerRange("(2)"));
	}

	/**
	 * Verifies that {@link IntervalVersionRange}'s parsing constructor fails as
	 * expected when passed an invalid range string to parse.
	 */
	@Test
	public void parseInvalidString() {
		boolean exceptionWasThrown;

		exceptionWasThrown = false;
		try {
			new IntervalVersionRange<IntegerVersion>(
					new IntegerVersionParser(), "4.1");
		} catch (VersionRangeParseException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);

		exceptionWasThrown = false;
		try {
			new IntervalVersionRange<IntegerVersion>(
					new IntegerVersionParser(), "[,]");
		} catch (VersionRangeParseException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);

		exceptionWasThrown = false;
		try {
			new IntervalVersionRange<IntegerVersion>(
					new IntegerVersionParser(), "");
		} catch (VersionRangeParseException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);

		exceptionWasThrown = false;
		try {
			new IntervalVersionRange<IntegerVersion>(
					new IntegerVersionParser(), "[1,4");
		} catch (VersionRangeParseException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);

		exceptionWasThrown = false;
		try {
			new IntervalVersionRange<IntegerVersion>(
					new IntegerVersionParser(), "1,4)");
		} catch (VersionRangeParseException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);
	}

	/**
	 * Ensures that the {@link IntervalVersionRange} implementation implements
	 * {@link Object#equals(Object)} and {@link Object#hashCode()} correctly.
	 */
	@Test
	public void equalsAndHashCode() {
		IntervalVersionRange<IntegerVersion> sample1 = parseIntegerRange("[1,4)");

		Assert.assertEquals(sample1, sample1);
		Assert.assertEquals(sample1.hashCode(), sample1.hashCode());

		Assert.assertEquals(sample1, parseIntegerRange("[1,4)"));
		Assert.assertEquals(sample1.hashCode(), parseIntegerRange("[1,4)")
				.hashCode());

		Assert.assertNotEquals(parseIntegerRange("[1,4)"),
				parseIntegerRange("2"));
		/*
		 * Note: there's no requirement that hasCode() return different values
		 * for inequal objects; some hashes may collide.
		 */
	}

	/**
	 * Ensures that the {@link IntervalVersionRange} implementation implements
	 * {@link #toString()} correctly.
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals(" [ 1 , 4 )", parseIntegerRange(" [ 1 , 4 )")
				.toString());
		Assert.assertEquals("[1,4)", new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.INCLUSIVE, new IntegerVersion(1),
				new IntegerVersion(4), IntervalBoundaryType.EXCLUSIVE)
				.toString());
		Assert.assertEquals("2", new IntervalVersionRange<IntegerVersion>(
				IntervalBoundaryType.OMITTED, new IntegerVersion(2), null,
				IntervalBoundaryType.OMITTED).toString());
	}

	/**
	 * Verifies that {@link IntegerVersionRange#matches(IntegerVersion)} works
	 * as expected in various scenarios.
	 */
	@Test
	public void matches() {
		Assert.assertTrue(parseIntegerRange("4").matches(new IntegerVersion(4)));
		Assert.assertTrue(parseIntegerRange("4").matches(new IntegerVersion(5)));
		Assert.assertFalse(parseIntegerRange("4")
				.matches(new IntegerVersion(3)));

		Assert.assertTrue(parseIntegerRange("[4]").matches(
				new IntegerVersion(4)));
		Assert.assertFalse(parseIntegerRange("[4]").matches(
				new IntegerVersion(5)));

		Assert.assertFalse(parseIntegerRange("(4)").matches(
				new IntegerVersion(4)));

		Assert.assertTrue(parseIntegerRange("[4,7]").matches(
				new IntegerVersion(4)));
		Assert.assertTrue(parseIntegerRange("[4,7]").matches(
				new IntegerVersion(7)));
		Assert.assertTrue(parseIntegerRange("[4,7]").matches(
				new IntegerVersion(5)));
		Assert.assertFalse(parseIntegerRange("[4,7]").matches(
				new IntegerVersion(3)));
		Assert.assertFalse(parseIntegerRange("[4,7]").matches(
				new IntegerVersion(8)));

		Assert.assertFalse(parseIntegerRange("(4,7)").matches(
				new IntegerVersion(4)));
		Assert.assertFalse(parseIntegerRange("(4,7)").matches(
				new IntegerVersion(7)));
		Assert.assertTrue(parseIntegerRange("[4,7]").matches(
				new IntegerVersion(5)));
		Assert.assertFalse(parseIntegerRange("(4,7)").matches(
				new IntegerVersion(3)));
		Assert.assertFalse(parseIntegerRange("(4,7)").matches(
				new IntegerVersion(8)));
	}
}
