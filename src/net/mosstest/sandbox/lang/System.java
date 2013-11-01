package net.mosstest.sandbox.lang;

import java.io.InputStream;
import java.io.PrintStream;

public final class System {
	private System() {
	};

	public static final PrintStream err = java.lang.System.err;
	public static final PrintStream out = java.lang.System.out;
	public static final InputStream in = java.lang.System.in;

	public static void arraycopy(Object src, int srcPos, Object dest,
			int destPos, int length) {
		java.lang.System.arraycopy(src, srcPos, dest, destPos, length);
	}

	public static long currentTimeMillis() {
		return java.lang.System.currentTimeMillis();
	}

	public static void gc() {
		java.lang.System.gc();
	}

	public static int identityHashCode(Object o) {
		return java.lang.System.identityHashCode(o);
	}
	
	public static String lineSeparator() {
		return java.lang.System.lineSeparator();
	}
	
	public static long nanoTime() {
		return java.lang.System.nanoTime();
	}
}
