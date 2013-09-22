package com.justdavis.jessentials.jversionsanity;

/**
 * Represents the ID of a specific versioning scheme, which will include a
 * unique implementation of the {@link Version} interface and, optionally, the
 * {@link VersionRange} interface.
 */
public final class VersioningScheme {
	private final Class<? extends Version> versionImplementation;
	private final Class<? extends VersionRange<?>> versionRangeImplementation;

	/**
	 * Constructor.
	 * 
	 * @param versionImplementation
	 *            the value for {@link #getVersionImpl()}
	 * @param versionRangeImplementation
	 *            the value for {@link #getRangeImpl()}
	 */
	public VersioningScheme(Class<? extends Version> versionImplementation,
			Class<? extends VersionRange<?>> versionRangeImplementation) {
		if (versionImplementation == null)
			throw new IllegalArgumentException();

		this.versionImplementation = versionImplementation;
		this.versionRangeImplementation = versionRangeImplementation;
	}

	/**
	 * @return the {@link Version} implementation used in this
	 *         {@link VersioningScheme}
	 */
	public Class<? extends Version> getVersionImpl() {
		return versionImplementation;
	}

	/**
	 * @return the {@link VersionRange} implementation used in this
	 *         {@link VersioningScheme}, or <code>null</code> if this scheme
	 *         does not have a {@link VersionRange} implementations
	 */
	public Class<? extends VersionRange<?>> getRangeImpl() {
		return versionRangeImplementation;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VersioningScheme [versionImplementation="
				+ versionImplementation + ", versionRangeImplementation="
				+ versionRangeImplementation + "]";
	}
}
