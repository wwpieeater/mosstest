package net.mosstest.sandbox.lang;

import java.io.InputStream;
import java.io.PrintStream;

// TODO: Auto-generated Javadoc
/**
 * The Class System.
 */
public final class System {
	
	/**
	 * Instantiates a new system.
	 */
	private System() {
	};

	/** The Constant err. */
	public static final PrintStream err = java.lang.System.err;
	
	/** The Constant out. */
	public static final PrintStream out = java.lang.System.out;
	
	/** The Constant in. */
	public static final InputStream in = java.lang.System.in;

	/**
	 * Arraycopy.
	 *
	 * @param src the src
	 * @param srcPos the src pos
	 * @param dest the dest
	 * @param destPos the dest pos
	 * @param length the length
	 */
	public static void arraycopy(Object src, int srcPos, Object dest,
			int destPos, int length) {
		java.lang.System.arraycopy(src, srcPos, dest, destPos, length);
	}

	/**
	 * Current time millis.
	 *
	 * @return the long
	 */
	public static long currentTimeMillis() {
		return java.lang.System.currentTimeMillis();
	}

	/**
	 * Gc.
	 */
	public static void gc() {
		java.lang.System.gc();
	}

	/**
	 * Identity hash code.
	 *
	 * @param o the o
	 * @return the int
	 */
	public static int identityHashCode(Object o) {
		return java.lang.System.identityHashCode(o);
	}
	
	/**
	 * Line separator.
	 *
	 * @return the string
	 */
	public static String lineSeparator() {
		return java.lang.System.lineSeparator();
	}
	
	/**
	 * Nano time.
	 *
	 * @return the long
	 */
	public static long nanoTime() {
		return java.lang.System.nanoTime();
	}
}
