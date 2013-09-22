package com.justdavis.jessentials.jversionsanity.range;

import com.justdavis.jessentials.jversionsanity.Version;

/**
 * <p>
 * Each implementation of this {@link VersionRange} interface represents a
 * method for modeling a set of constraints on {@link Version}s. Each
 * {@link VersionRange} specifies a set of allowable/matching {@link Version}s.
 * <p>
 * <strong>Note for implementors:</strong> It is strongly recommended that
 * {@link VersionRange} instances be immutable.
 * </p>
 * 
 * @param <V>
 *            the {@link Version} implementation that this {@link VersionRange}
 *            implementation is associated with
 */
public interface VersionRange<V extends Version> {
	/**
	 * Determines whether or not the specified {@link Version} complies
	 * with/matches this {@link VersionRange}.
	 * 
	 * @param version
	 *            the {@link Version} to check against this {@link VersionRange}
	 * @return <code>true</code> if the specified {@link Version} complies
	 *         with/matches this {@link VersionRange}, <code>false</code> if it
	 *         does not
	 */
	public boolean matches(V version);
}
