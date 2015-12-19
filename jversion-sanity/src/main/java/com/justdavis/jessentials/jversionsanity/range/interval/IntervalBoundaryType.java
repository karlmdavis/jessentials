package com.justdavis.jessentials.jversionsanity.range.interval;

import com.justdavis.jessentials.jversionsanity.range.VersionRange;

/**
 * Enumerates the various bounding types for <a href=
 * "http://en.wikipedia.org/wiki/Interval_%28mathematics%29#Notations_for_intervals"
 * >interval notation</a>. See {@link IntervalParser} for more details.
 */
public enum IntervalBoundaryType {
	/**
	 * Indicates that the range includes the start/end version. Would be
	 * "greater than" or "less than" in mathematical terms.
	 */
	INCLUSIVE('[', ']'),

	/**
	 * Indicates that the range excludes the start/end version. Would be
	 * "greater than or equal to" or "less than or equal to" in mathematical
	 * terms.
	 */
	EXCLUSIVE('(', ')'),

	/**
	 * The meaning of this {@link BoundaryTypes} is dependent on the
	 * {@link VersionRange} implementation. That said, Apache Maven uses this
	 * boundary type to represent an implicit "greater than or equal to."
	 */
	OMITTED(null, null);

	private final Character lowerBoundarySymbol;
	private final Character upperBoundarySymbol;

	/**
	 * Enum constant constructor.
	 * 
	 * @param lowerBoundarySymbol
	 *            the value to use for {@link #getSymbolForLowerBoundary()}
	 * @param upperBoundarySymbol
	 *            the value to use for {@link #getSymbolForUpperBoundary()}
	 */
	private IntervalBoundaryType(Character lowerBoundarySymbol, Character upperBoundarySymbol) {
		this.lowerBoundarySymbol = lowerBoundarySymbol;
		this.upperBoundarySymbol = upperBoundarySymbol;
	}

	/**
	 * @return the symbol that represents this {@link IntervalBoundaryType} for
	 *         lower bounds, or <code>null</code> if this
	 *         {@link IntervalBoundaryType} is represented by the
	 *         <em>absence</em> of any such symbol
	 */
	public Character getSymbolForLowerBoundary() {
		return lowerBoundarySymbol;
	}

	/**
	 * @return the symbol that represents this {@link IntervalBoundaryType} for
	 *         upper bounds, or <code>null</code> if this
	 *         {@link IntervalBoundaryType} is represented by the
	 *         <em>absence</em> of any such symbol
	 */
	public Character getSymbolForUpperBoundary() {
		return upperBoundarySymbol;
	}

	/**
	 * Determines which of the {@link IntervalBoundaryType} constants the
	 * specified string matches.
	 * 
	 * @param boundaryString
	 *            the {@link String} to match against
	 * @return the {@link IntervalBoundaryType} constant that the specified
	 *         string matches
	 * @throws IllegalArgumentException
	 *             An {@link IllegalArgumentException} will be thrown if the
	 *             specified string does not match any of the known boundary
	 *             types.
	 */
	public static IntervalBoundaryType determineBoundaryType(String boundaryString) throws IllegalArgumentException {
		if (boundaryString == null || boundaryString.isEmpty())
			return OMITTED;
		else if (boundaryString.length() != 1)
			throw new IllegalArgumentException("Boundary symbol not of length 0 or 1: '%s%'.");
		else if (boundaryString.charAt(0) == INCLUSIVE.lowerBoundarySymbol
				|| boundaryString.charAt(0) == INCLUSIVE.upperBoundarySymbol)
			return INCLUSIVE;
		else if (boundaryString.charAt(0) == EXCLUSIVE.lowerBoundarySymbol
				|| boundaryString.charAt(0) == EXCLUSIVE.upperBoundarySymbol)
			return EXCLUSIVE;
		else
			throw new IllegalArgumentException("Unknown boundary symbol: '%s'.");
	}
}