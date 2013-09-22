package com.justdavis.jessentials.jversionsanity.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.justdavis.jessentials.jversionsanity.Version;
import com.justdavis.jessentials.jversionsanity.VersionRange;

/**
 * A {@link Matcher} that determines whether or not specific {@link Version}s
 * match a given {@link VersionRange}, according to the logic described in the
 * class JavaDoc for {@link Interval}.
 */
public final class IntervalMatcher<V extends Version> extends BaseMatcher<V> {
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
				 * If no upper bound is specified it's effectively
				 * "version >= -infinity".
				 */
				if (range.getVersionLower() == null)
					return true;

				/*
				 * Otherwise, compare the version to the interval's lower bound.
				 */
				return version.compareTo(range.getVersionLower()) >= 0;
			} else if (range.getTypeLower() == IntervalBoundaryType.EXCLUSIVE) {
				/*
				 * If no upper bound is specified it's effectively
				 * "version > -infinity".
				 */
				if (range.getVersionLower() == null)
					return true;

				/*
				 * Otherwise, compare the version to the interval's lower bound.
				 */
				return version.compareTo(range.getVersionLower()) > 0;
			} else {
				/*
				 * Library author error: this will only occur if a new boundary
				 * type has been added
				 */
				throw new IllegalStateException(String.format(
						"Unsupported %s constant: %s.",
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
				 * If no upper bound is specified it's effectively
				 * "version <= -infinity".
				 */
				if (range.getVersionUpper() == null)
					return true;

				/*
				 * Otherwise, compare the version to the interval's upper bound.
				 */
				return version.compareTo(range.getVersionUpper()) <= 0;
			} else if (range.getTypeUpper() == IntervalBoundaryType.EXCLUSIVE) {
				/*
				 * If no upper bound is specified it's effectively
				 * "version < -infinity".
				 */
				if (range.getVersionUpper() == null)
					return true;

				/*
				 * Otherwise, compare the version to the interval's upper bound.
				 */
				return version.compareTo(range.getVersionUpper()) < 0;
			} else {
				/*
				 * Library author error: this will only occur if a new boundary
				 * type has been added
				 */
				throw new IllegalStateException(String.format(
						"Unsupported %s constant: %s.",
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
