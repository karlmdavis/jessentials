package com.justdavis.karl.misc.resources;

/**
 * <p>
 * Represents the path to classpath resource files.
 * </p>
 */
public final class ResourcePath {
	private final String path;

	/**
	 * <p>
	 * Constructs a new {@link ResourcePath} to a classpath resource.
	 * </p>
	 * <p>
	 * This is intentionally a break from the typical Maven
	 * <code>src/main/resources</code> layout, which can often lead to path
	 * collisions between resources in different JARs.
	 * </p>
	 * 
	 * @param scope
	 *            the path/namespace that the resource <code>name</code> is
	 *            relative to
	 * @param name
	 *            the relative name of the resource file
	 */
	public ResourcePath(Class<?> scope, String name) {
		// Sanity check: null scope.
		if (scope == null)
			throw new IllegalArgumentException();
		// Sanity check: null name.
		if (name == null)
			throw new IllegalArgumentException();
		// Sanity check: non-relative name.
		if (name.startsWith("/"))
			throw new IllegalArgumentException();

		StringBuilder path = new StringBuilder();
		path.append(getPackagePath(scope));
		path.append('/');
		path.append(name);

		this.path = path.toString();
	}

	/**
	 * @param scope
	 *            the {@link Class} to get the package classpath path to
	 * @return the namespace/package for the specified {@link Class}, with all
	 *         of the '<code>.</code>' characters converted to '<code>/</code>'
	 *         characters
	 */
	private static String getPackagePath(Class<?> scope) {
		return scope.getPackage().getName().replace('.', '/');
	}

	/**
	 * @return the classpath path to the resource represented by this
	 *         {@link ResourcePath}
	 */
	public String getPath() {
		return path;
	}
}
