package com.justdavis.jessentials.jversionsanity.bareint;

import org.junit.Assert;
import org.junit.Test;

import com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest;
import com.justdavis.jessentials.jversionsanity.VersionParser;
import com.justdavis.jessentials.jversionsanity.VersionRangeParseException;
import com.justdavis.jessentials.jversionsanity.util.IntervalBoundaryType;

/**
 * Unit tests for {@link IntegerVersionRange}.
 */
public final class IntegerVersionRangeTest extends
		AbstractVersionRangeTest<IntegerVersion, IntegerVersionRange> {
	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest#getParser()
	 */
	@Override
	protected VersionParser<IntegerVersion, IntegerVersionRange> getParser() {
		return new IntegerVersionParser();
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest#getSample1()
	 */
	@Override
	protected IntegerVersionRange getSample1() {
		return new IntegerVersionRange(IntervalBoundaryType.INCLUSIVE,
				new IntegerVersion(1), new IntegerVersion(4),
				IntervalBoundaryType.EXCLUSIVE);
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest#getSample1String()
	 */
	@Override
	protected String getSample1String() {
		return " [ 1 , 4 ) ";
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest#getSample1VersionMatching()
	 */
	@Override
	protected IntegerVersion getSample1VersionMatching() {
		return new IntegerVersion(1);
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest#getSample1VersionNotMatching()
	 */
	@Override
	protected IntegerVersion getSample1VersionNotMatching() {
		return new IntegerVersion(4);
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.AbstractVersionRangeTest#getSample2()
	 */
	@Override
	protected IntegerVersionRange getSample2() {
		return new IntegerVersionRange(IntervalBoundaryType.OMITTED,
				new IntegerVersion(2), null, IntervalBoundaryType.OMITTED);
	}

	/**
	 * Verifies that
	 * {@link IntegerVersionRange#IntegerVersionRange(IntervalBoundaryType, IntegerVersion, IntegerVersion, IntervalBoundaryType)}
	 * fails as expected when passed invalid parameters.
	 */
	@Test
	public void constructWithInvalidParameters() {
		boolean exceptionWasThrown;

		exceptionWasThrown = false;
		try {
			new IntegerVersionRange(IntervalBoundaryType.EXCLUSIVE,
					new IntegerVersion(1), new IntegerVersion(2),
					IntervalBoundaryType.OMITTED);
		} catch (IllegalArgumentException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);

		exceptionWasThrown = false;
		try {
			new IntegerVersionRange(IntervalBoundaryType.INCLUSIVE,
					new IntegerVersion(2), new IntegerVersion(1),
					IntervalBoundaryType.INCLUSIVE);
		} catch (IllegalArgumentException e) {
			exceptionWasThrown = true;
		}
		Assert.assertTrue(exceptionWasThrown);
	}

	/**
	 * Verifies that {@link IntegerVersionRange#IntegerVersionRange(String)}
	 * fails as expected when passed an invalid range string to parse.
	 */
	@Test(expected = VersionRangeParseException.class)
	public void parseInvalidString() {
		new IntegerVersionRange("4.1");
	}

	/**
	 * Verifies that {@link IntegerVersionRange#matches(IntegerVersion)} works
	 * as expected in various scenarios.
	 */
	@Test
	public void matches_implementationTests() {
		Assert.assertTrue(new IntegerVersionRange("4")
				.matches(new IntegerVersion(4)));
		Assert.assertTrue(new IntegerVersionRange("4")
				.matches(new IntegerVersion(5)));
		Assert.assertFalse(new IntegerVersionRange("4")
				.matches(new IntegerVersion(3)));

		Assert.assertTrue(new IntegerVersionRange("[4]")
				.matches(new IntegerVersion(4)));
		Assert.assertFalse(new IntegerVersionRange("[4]")
				.matches(new IntegerVersion(5)));

		Assert.assertFalse(new IntegerVersionRange("(4)")
				.matches(new IntegerVersion(4)));

		Assert.assertTrue(new IntegerVersionRange("[4,7]")
				.matches(new IntegerVersion(4)));
		Assert.assertTrue(new IntegerVersionRange("[4,7]")
				.matches(new IntegerVersion(7)));
		Assert.assertTrue(new IntegerVersionRange("[4,7]")
				.matches(new IntegerVersion(5)));
		Assert.assertFalse(new IntegerVersionRange("[4,7]")
				.matches(new IntegerVersion(3)));
		Assert.assertFalse(new IntegerVersionRange("[4,7]")
				.matches(new IntegerVersion(8)));

		Assert.assertFalse(new IntegerVersionRange("(4,7)")
				.matches(new IntegerVersion(4)));
		Assert.assertFalse(new IntegerVersionRange("(4,7)")
				.matches(new IntegerVersion(7)));
		Assert.assertTrue(new IntegerVersionRange("[4,7]")
				.matches(new IntegerVersion(5)));
		Assert.assertFalse(new IntegerVersionRange("(4,7)")
				.matches(new IntegerVersion(3)));
		Assert.assertFalse(new IntegerVersionRange("(4,7)")
				.matches(new IntegerVersion(8)));
	}
}
