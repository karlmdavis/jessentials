package com.justdavis.jessentials.jversionsanity.range.interval;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.justdavis.jessentials.jversionsanity.Version;
import com.justdavis.jessentials.jversionsanity.range.VersionRange;

/**
 * A {@link Matcher} that determines whether or not specific {@link Version}s
 * match a given {@link VersionRange}, according to the logic described in the
 * class JavaDoc for {@link Interval}.
 */
final class IntervalMatcher<V extends Version> extends BaseMatcher<V> {
	private final Interval<V> range;

	/**
	 * Constructor.
	 * 
	 * @param range
	 *            the {@link Interval} version range that {@link Version}s will
	 *            be matched against
	 */
	public IntervalMatcher(Interval<V> range) {
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

		return CoreMatchers.allOf(new LowerBoundMatcher<V>(range),
				new UpperBoundMatcher<V>(range)).matches(version);
	}

	/**
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This {@link org.hamcrest.Matcher} verifies that {@link Version}s match
	 * the lower bound (if any) of {@link Interval} version ranges.
	 * 
	 * @param <V>
	 *            the {@link Version} implementation that will be checked
	 */
	private static final class LowerBoundMatcher<V extends Version> extends
			BaseMatcher<V> {
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
			/*
			 * This will store the "greater than" and (possibly) "equal to"
			 * comparisons.
			 */
			Matcher<V> equalityMatcher;

			// We always need to perform a "greater than" comparison.
			equalityMatcher = new GreaterThanMatcher<V>(range.getVersionLower());

			// Is an "equal to" comparison needed?
			if (range.getTypeLower() == IntervalBoundaryType.OMITTED
					|| range.getTypeLower() == IntervalBoundaryType.INCLUSIVE) {
				/*
				 * Note: An OMITTED bound is equivalent to an INCLUSIVE one.
				 */

				equalityMatcher = CoreMatchers.either(equalityMatcher).or(
						new EqualToMatcher<V>(range.getVersionLower()));
			}

			return equalityMatcher.matches(item);
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
	private static final class UpperBoundMatcher<V extends Version> extends
			BaseMatcher<V> {
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
			/*
			 * This will store the "less than" and (possibly) "equal to"
			 * comparisons.
			 */
			Matcher<V> equalityMatcher;

			// We always need to perform a "greater than" comparison.
			equalityMatcher = new LessThanMatcher<V>(range.getVersionUpper());

			// Is an "equal to" comparison needed?
			if (range.getTypeUpper() == IntervalBoundaryType.OMITTED
					|| range.getTypeUpper() == IntervalBoundaryType.INCLUSIVE) {
				/*
				 * Note: An OMITTED bound is equivalent to an INCLUSIVE one.
				 */

				equalityMatcher = CoreMatchers.either(equalityMatcher).or(
						new EqualToMatcher<V>(range.getVersionUpper()));
			}

			return equalityMatcher.matches(item);
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
	 * This {@link org.hamcrest.Matcher} verifies that one {@link Version} is
	 * equal/equivalent to another.
	 * 
	 * @param <V>
	 *            the {@link Version} implementation that will be checked
	 */
	private static final class EqualToMatcher<V extends Version> extends
			BaseMatcher<V> {
		private final V boundingVersion;

		/**
		 * Constructor.
		 * 
		 * @param boundingVersion
		 *            the bounding {@link Version} that other {@link Version}s
		 *            will be compared against, or <code>null</code> to
		 *            represent "any version"
		 */
		public EqualToMatcher(V boundingVersion) {
			this.boundingVersion = boundingVersion;
		}

		/**
		 * @see org.hamcrest.Matcher#matches(java.lang.Object)
		 */
		@Override
		public boolean matches(Object item) {
			if (!(item instanceof Version))
				throw new IllegalArgumentException();
			Version version = (Version) item;

			if (boundingVersion != null)
				return version.compareTo(boundingVersion) == 0;
			else
				return true;
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
	 * This {@link org.hamcrest.Matcher} verifies that one {@link Version} is
	 * less/lower than another.
	 * 
	 * @param <V>
	 *            the {@link Version} implementation that will be checked
	 */
	private static final class LessThanMatcher<V extends Version> extends
			BaseMatcher<V> {
		private final V boundingVersion;

		/**
		 * Constructor.
		 * 
		 * @param boundingVersion
		 *            the bounding {@link Version} that other {@link Version}s
		 *            will be compared against, or <code>null</code> to
		 *            represent "any version"
		 */
		public LessThanMatcher(V boundingVersion) {
			this.boundingVersion = boundingVersion;
		}

		/**
		 * @see org.hamcrest.Matcher#matches(java.lang.Object)
		 */
		@Override
		public boolean matches(Object item) {
			if (!(item instanceof Version))
				throw new IllegalArgumentException();
			Version version = (Version) item;

			if (boundingVersion != null)
				return version.compareTo(boundingVersion) < 0;
			else
				return true;
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
	 * This {@link org.hamcrest.Matcher} verifies that one {@link Version} is
	 * greater/higher than another.
	 * 
	 * @param <V>
	 *            the {@link Version} implementation that will be checked
	 */
	private static final class GreaterThanMatcher<V extends Version> extends
			BaseMatcher<V> {
		private final V boundingVersion;

		/**
		 * Constructor.
		 * 
		 * @param boundingVersion
		 *            the bounding {@link Version} that other {@link Version}s
		 *            will be compared against, or <code>null</code> to
		 *            represent "any version"
		 */
		public GreaterThanMatcher(V boundingVersion) {
			this.boundingVersion = boundingVersion;
		}

		/**
		 * @see org.hamcrest.Matcher#matches(java.lang.Object)
		 */
		@Override
		public boolean matches(Object item) {
			if (!(item instanceof Version))
				throw new IllegalArgumentException();
			Version version = (Version) item;

			if (boundingVersion != null)
				return version.compareTo(boundingVersion) > 0;
			else
				return true;
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
