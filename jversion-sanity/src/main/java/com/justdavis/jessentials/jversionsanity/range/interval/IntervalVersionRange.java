package com.justdavis.jessentials.jversionsanity.range.interval;

import com.justdavis.jessentials.jversionsanity.Version;
import com.justdavis.jessentials.jversionsanity.VersionParseException;
import com.justdavis.jessentials.jversionsanity.VersionParser;
import com.justdavis.jessentials.jversionsanity.range.VersionRange;
import com.justdavis.jessentials.jversionsanity.range.VersionRangeParseException;

/**
 * <p>
 * Models a {@link VersionRange} as a
 * <a href= "http://en.wikipedia.org/wiki/Interval_%28mathematics%29" >
 * mathematical interval</a>.
 * </p>
 * <p>
 * Each {@link IntervalVersionRange} can be represented as a {@link String} (via
 * {@link #toString()}) and can also be parsed from a {@link String} (via
 * {@link #IntervalVersionRange(VersionParser, String)}). The format of these
 * {@link String}s is as follows (where <code>a</code> and <code>b</code> are
 * actual versions):
 * </p>
 * <ul>
 * <li><code>[a]</code>: will only match versions that are exactly
 * <code>a</code></li>
 * <li><code>[a,a]</code>: (same as above)</li>
 * <li><code>(a)</code>: will not match any versions</li>
 * <li><code>(a,a]</code>: (same as above)</li>
 * <li><code>[a,a)</code>: (same as above)</li>
 * <li><code>[a,b]</code>: will only match versions <code>v</code> where
 * <code>a &lt;= v &lt;= b</code></li>
 * <li><code>(a,b)</code>: will only match versions <code>v</code> where
 * <code>a &lt; v &lt; b</code></li>
 * <li><code>[a,b)</code>: will only match versions <code>v</code> where
 * <code>a &lt;= v &lt; b</code></li>
 * <li><code>(a,b]</code>: will only match versions <code>v</code> where
 * <code>a &lt; v &lt;= b</code></li>
 * <li><code>[a,]</code>: will only match versions <code>v</code> where
 * <code>v &gt;= a</code></li>
 * <li><code>[,b)</code>: will only match versions <code>v</code> where
 * <code>v &lt; b</code></li>
 * <li><code>a</code>: equivalent to <code>[a,]</code></li>
 * </ul>
 */
public final class IntervalVersionRange<V extends Version> implements VersionRange<V> {
	/*
	 * TODO Consider implementing multi-sets and "1.0 as recommended" per
	 * http://
	 * docs.codehaus.org/display/MAVEN/Dependency+Mediation+and+Conflict+
	 * Resolution
	 * #DependencyMediationandConflictResolution-DependencyVersionRanges
	 */

	/**
	 * Stores the {@link String} that this {@link IntervalVersionRange} instance
	 * was parsed from, or <code>null</code> if it was constructed manually.
	 */
	private final String rangeString;

	private final Interval<V> interval;

	/**
	 * Parses a {@link VersionRange} from the specified {@link String}
	 * representation.
	 * 
	 * @param versionParser
	 *            the {@link VersionParser} that should be used to parse the
	 *            {@link Version}s represented in the {@link String}
	 * @param rangeString
	 *            a {@link String} representation of a
	 *            {@link IntervalVersionRange}, e.g. as produced by
	 *            {@link #toString()}
	 * @throws VersionRangeParseException
	 *             A {@link VersionRangeParseException} will be thrown if the
	 *             specified {@link String} cannot be parsed.
	 */
	public IntervalVersionRange(VersionParser<V> versionParser, String rangeString) throws VersionRangeParseException {
		if (rangeString == null)
			throw new IllegalArgumentException("Null range strings not supported.");

		this.rangeString = rangeString;

		IntervalParser intervalParser = new IntervalParser();
		Interval<String> parsedStringInterval = intervalParser.parseVersionRange(rangeString);

		try {
			V versionLower;
			if (parsedStringInterval.getVersionLower() != null)
				versionLower = versionParser.parseVersion(parsedStringInterval.getVersionLower());
			else
				versionLower = null;

			V versionUpper;
			if (parsedStringInterval.getVersionUpper() != null)
				versionUpper = versionParser.parseVersion(parsedStringInterval.getVersionUpper());
			else
				versionUpper = null;

			// Sanity check: ensure that the versions aren't backwards
			if (versionLower != null && versionUpper != null && versionLower.compareTo(versionUpper) > 0)
				throw new IllegalArgumentException("The 'lower' version is actually greater than the 'upper' version.");

			this.interval = new Interval<V>(parsedStringInterval.getTypeLower(), versionLower, versionUpper,
					parsedStringInterval.getTypeUpper());
		} catch (VersionParseException e) {
			throw new VersionRangeParseException(rangeString, e);
		}
	}

	/**
	 * Constructs a new {@link IntervalVersionRange}.
	 * 
	 * @param boundaryLower
	 *            the {@link IntervalBoundaryType} constant for the lower
	 *            boundary of this {@link IntervalVersionRange}
	 * @param versionLower
	 *            the {@link Version} that represents the lower boundary of this
	 *            {@link IntervalVersionRange}, or <code>null</code> if the
	 *            range has no lower bound
	 * @param versionUpper
	 *            the {@link Version} that represents the upper boundary of this
	 *            {@link IntervalVersionRange}, or <code>null</code> if the
	 *            range has no upper bound
	 * @param boundaryUpper
	 *            the {@link IntervalBoundaryType} constant for the upper
	 *            boundary of this {@link IntervalVersionRange}
	 */
	public IntervalVersionRange(IntervalBoundaryType boundaryLower, V versionLower, V versionUpper,
			IntervalBoundaryType boundaryUpper) {
		// Sanity check: ensure that the versions aren't backwards
		if (versionLower != null && versionUpper != null && versionLower.compareTo(versionUpper) > 0)
			throw new IllegalArgumentException("The 'lower' version is actually greater than the 'upper' version.");

		this.rangeString = null;
		this.interval = new Interval<V>(boundaryLower, versionLower, versionUpper, boundaryUpper);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (rangeString != null)
			return rangeString;
		else
			return interval.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		/*
		 * This method was generated by Eclipse's 'Source > Generate hashCode()
		 * and equals()...' feature.
		 */

		final int prime = 31;
		int result = 1;
		result = prime * result + ((interval == null) ? 0 : interval.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		/*
		 * This method was generated by Eclipse's 'Source > Generate hashCode()
		 * and equals()...' feature.
		 */

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntervalVersionRange<?> other = (IntervalVersionRange<?>) obj;
		if (interval == null) {
			if (other.interval != null)
				return false;
		} else if (!interval.equals(other.interval))
			return false;
		return true;
	}

	/**
	 * @see com.justdavis.jessentials.jversionsanity.range.VersionRange#matches(com.justdavis.jessentials.jversionsanity.Version)
	 */
	@Override
	public boolean matches(V version) {
		/*
		 * There are basically two ways to implement this: A) as a collection of
		 * edge cases, with lots of if-else blocks, or B) as predicate logic.
		 * Option (B) is a lot easier to read, so I've opted for it, even though
		 * it requires the use of Hamcrest (or some hand-written predicate logic
		 * classes).
		 */
		return new IntervalMatcher<V>(interval).matches(version);
	}
}
