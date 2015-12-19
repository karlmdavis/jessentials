package com.justdavis.jessentials.jversionsanity.range.interval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;

import com.justdavis.jessentials.jversionsanity.Version;
import com.justdavis.jessentials.jversionsanity.range.VersionRangeParseException;

/**
 * <p>
 * A utility class that is used to parse {@link Interval}s represented in
 * <a href=
 * "http://en.wikipedia.org/wiki/Interval_%28mathematics%29#Notations_for_intervals"
 * >interval notation</a>.
 * </p>
 * <p>
 * Some notes on how this parser functions:
 * </p>
 * <ul>
 * <li>Whitespace will be ignored. Whitespace internal to a version number, e.g.
 * "<code>3.1 4</code>" will be maintained in the parse results; it's up to the
 * caller to decide what to do with it.</li>
 * <li>Version numbers can contain any characters other than: '<code>[</code>',
 * '<code>(</code>', '<code>]</code>', '<code>)</code>', and '<code>,</code>'.
 * </li>
 * <li></li>
 * </ul>
 * 
 * @see Interval
 * @see IntervalVersionRange
 */
final class IntervalParser {
	private static final String WHITESPACE = "\\s*";
	private static final String VERSION_STRING = "([^\\[\\(\\]\\),]+)?";
	private static final String BOUNDARY_LEFT = "([\\(\\[])?";
	private static final String BOUNDARY_RIGHT = "([\\)\\]])?";
	private static final String COMMA = "(,)?";

	/**
	 * <p>
	 * The {@link Pattern} that can be used to match and pick apart intervals.
	 * Has the following capture groups, all of which are optional:
	 * </p>
	 * <ul>
	 * <li>0: the entire {@link String}</li>
	 * <li>1: the left-side '[' or '(' character</li>
	 * <li>2: the left-side version</li>
	 * <li>3: the comma</li>
	 * <li>4: the right-side version</li>
	 * <li>5: the right-side ']' or ')' character</li>
	 * </ul>
	 */
	private static final Pattern INTERVAL_REGEX = Pattern
			.compile(WHITESPACE + BOUNDARY_LEFT + WHITESPACE + VERSION_STRING + WHITESPACE + COMMA + WHITESPACE
					+ VERSION_STRING + WHITESPACE + BOUNDARY_RIGHT + WHITESPACE);

	/**
	 * Parses the specified version range {@link String} into an
	 * {@link Interval} model object that breaks out the range's components to
	 * make it easier to interpret it.
	 * 
	 * @param versionRangeString
	 *            the version range {@link String} representation to be parsed
	 * @return an {@link Interval} model object that breaks out the range's
	 *         components to make it easier to interpret it
	 * @throws VersionRangeParseException
	 *             An {@link IllegalArgumentException} will be thrown if the
	 *             version range {@link String} could not be properly parsed.
	 */
	public Interval<String> parseVersionRange(String versionRangeString) throws VersionRangeParseException {
		Matcher rangeMatcher = INTERVAL_REGEX.matcher(versionRangeString);
		if (!rangeMatcher.matches())
			throw new VersionRangeParseException("Invalid range format.");

		// Pull apart the regular expression match
		String boundaryLeftString = rangeMatcher.group(1);
		IntervalBoundaryType boundaryLeft = IntervalBoundaryType.determineBoundaryType(boundaryLeftString);
		String versionLowerString = rangeMatcher.group(2);
		if (versionLowerString != null)
			versionLowerString = versionLowerString.trim();
		String commaString = rangeMatcher.group(3);
		String versionUpperString = rangeMatcher.group(4);
		if (versionUpperString != null)
			versionUpperString = versionUpperString.trim();
		String boundaryRightString = rangeMatcher.group(5);
		IntervalBoundaryType boundaryRight = IntervalBoundaryType.determineBoundaryType(boundaryRightString);

		// Sanity check: either 0 or 2 boundary symbols must be present
		if ((boundaryLeft != IntervalBoundaryType.OMITTED) != (boundaryRight != IntervalBoundaryType.OMITTED))
			throw new VersionRangeParseException("unbalanced boundary symbols");

		// Sanity check: if boundary symbols aren't present, there must not be a
		// comma
		if (boundaryLeft == IntervalBoundaryType.OMITTED && commaString != null)
			throw new VersionRangeParseException("comma should not be present");

		// Construct the interval, being sure to use the single-valued or
		// non-single-valued constructor, as appropriate.
		try {
			if (commaString != null || boundaryLeft == IntervalBoundaryType.OMITTED)
				return new Interval<String>(boundaryLeft, versionLowerString, versionUpperString, boundaryRight);
			else
				return new Interval<String>(boundaryLeft, versionLowerString, boundaryRight);
		} catch (IllegalArgumentException e) {
			throw new VersionRangeParseException("Invalid version range.", e);
		}
	}

	/**
	 * Determines whether or not a specific {@link Version} matches a given
	 * version range, as represented by the specified {@link Interval}.
	 * 
	 * @param range
	 *            the {@link Interval} version range to compare the specified
	 *            {@link Version} against
	 * @param version
	 *            the {@link Version} being compared against the range
	 * @return <code>true</code> if the specified {@link Version} matches the
	 *         specified interval version range, <code>false</code> if it does
	 *         not
	 */
	public <V extends Version> boolean matches(Interval<V> range, V version) {
		/*
		 * There are basically two ways to implement this: A) as a collection of
		 * edge cases, with lots of if-else blocks, or B) as predicate logic.
		 * Option (B) is a lot easier to read, so I've opted for it, even though
		 * it requires the use of Hamcrest (or some hand-written predicate logic
		 * classes).
		 */
		return CoreMatchers.allOf(new LowerBoundMatcher<V>(range), new UpperBoundMatcher<V>(range)).matches(version);
	}

	/**
	 * This {@link org.hamcrest.Matcher} verifies that {@link Version}s match
	 * the lower bound (if any) of {@link Interval} version ranges.
	 * 
	 * @param <V>
	 *            the {@link Version} implementation that will be checked
	 */
	private static final class LowerBoundMatcher<V extends Version> extends BaseMatcher<V> {
		private final Interval<V> range;

		/**
		 * Constructor.
		 * 
		 * @param range
		 *            the {@link Interval} version range that {@link Version}s
		 *            will be matched against
		 */
		public LowerBoundMatcher(Interval<V> range) {
			this.range = range;
		}

		/**
		 * @see org.hamcrest.Matcher#matches(java.lang.Object)
		 */
		@Override
		public boolean matches(Object item) {
			if (!(item instanceof Version))
				throw new IllegalArgumentException();
			Version version = (Version) item;

			/*
			 * Design note: This logic could also be broken up further into
			 * predicate clauses. I think that the logic at this level is
			 * clearer when expressed imperatively, though.
			 */

			if (range.getTypeLower() == IntervalBoundaryType.OMITTED
					|| range.getTypeLower() == IntervalBoundaryType.INCLUSIVE) {
				/*
				 * Note: An OMITTED lower bound is equivalent to an INCLUSIVE
				 * one.
				 */

				/*
				 * If no lower bound is specified, it's effectively
				 * "version >= -infinity".
				 */
				if (range.getVersionLower() == null)
					return true;

				return version.compareTo(range.getVersionLower()) >= 0;
			} else if (range.getTypeLower() == IntervalBoundaryType.EXCLUSIVE) {
				return version.compareTo(range.getVersionLower()) > 0;
			} else {
				/*
				 * Library author error: this will only occur if a new boundary
				 * type has been added
				 */
				throw new IllegalStateException(String.format("Unsupported %s constant: %s.",
						IntervalBoundaryType.class, range.getTypeLower()));
			}
		}

		/**
		 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
		 */
		@Override
		public void describeTo(Description description) {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * This {@link org.hamcrest.Matcher} verifies that {@link Version}s match
	 * the upper bound (if any) of {@link Interval} version ranges.
	 * 
	 * @param <V>
	 *            the {@link Version} implementation that will be checked
	 */
	private static final class UpperBoundMatcher<V extends Version> extends BaseMatcher<V> {
		private final Interval<V> range;

		/**
		 * Constructor.
		 * 
		 * @param range
		 *            the {@link Interval} version range that {@link Version}s
		 *            will be matched against
		 */
		public UpperBoundMatcher(Interval<V> range) {
			this.range = range;
		}

		/**
		 * @see org.hamcrest.Matcher#matches(java.lang.Object)
		 */
		@Override
		public boolean matches(Object item) {
			if (!(item instanceof Version))
				throw new IllegalArgumentException();
			Version version = (Version) item;

			/*
			 * Design note: This logic could also be broken up further into
			 * predicate clauses. I think that the logic at this level is
			 * clearer when expressed imperatively, though.
			 */

			if (range.getTypeUpper() == IntervalBoundaryType.OMITTED
					|| range.getTypeUpper() == IntervalBoundaryType.INCLUSIVE) {
				/*
				 * Note: An OMITTED upper bound is functionally equivalent to
				 * either an INCLUSIVE or an EXCLUSIVE one.
				 */

				/*
				 * If no upper bound is specified, it's effectively
				 * "version <= -infinity".
				 */
				if (range.getVersionUpper() == null)
					return true;

				return version.compareTo(range.getVersionUpper()) <= 0;
			} else if (range.getTypeUpper() == IntervalBoundaryType.EXCLUSIVE) {
				return version.compareTo(range.getVersionUpper()) < 0;
			} else {
				/*
				 * Library author error: this will only occur if a new boundary
				 * type has been added
				 */
				throw new IllegalStateException(String.format("Unsupported %s constant: %s.",
						IntervalBoundaryType.class, range.getTypeLower()));
			}
		}

		/**
		 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
		 */
		@Override
		public void describeTo(Description description) {
			throw new UnsupportedOperationException();
		}
	}
}
