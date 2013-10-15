package net.mosstest.scripting;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.mosstest.sandbox.SandboxClass;

/**
 * Class for scripts to instantiate and access the static methods of various approved classes.
 * @author rarkenin
 *
 */
public class JavaApi {
	
	private static final HashMap<String, SandboxClass> qualifiedClasses=new HashMap<String, SandboxClass>(){{
		put("java.lang.Object", new SandboxClass(java.lang.Object.class, true, true, false, false));
		put("java.lang.String", new SandboxClass(java.lang.String.class, true, true, false, false));
		put("java.lang.Comparable", new SandboxClass(java.lang.Comparable.class, true, true, false, true));
		put("java.lang.CharSequence", new SandboxClass(java.lang.CharSequence.class, true, true, false, true));
		put("java.lang.Class", new SandboxClass(java.lang.Class.class, false, false, true, false));
		put("java.lang.Cloneable", new SandboxClass(java.lang.Cloneable.class, true, true, false, true));
		put("java.lang.ClassLoader", new SandboxClass(java.lang.ClassLoader.class, false, false, true, false));
		put("java.lang.System", new SandboxClass(net.mosstest.sandbox.lang.System.class, true, false, true, false));
		put("java.lang.Throwable", new SandboxClass(java.lang.Throwable.class, true, true, false, false));
		put("java.lang.Error", new SandboxClass(java.lang.Error.class, true, true, false, false));
		put("java.lang.ThreadDeath", new SandboxClass(java.lang.ThreadDeath.class, false, false, true, false));
		put("java.lang.Exception", new SandboxClass(java.lang.Exception.class, true, true, false, false));
		put("java.lang.RuntimeException", new SandboxClass(java.lang.RuntimeException.class, true, true, false, false));
		put("java.lang.ClassCastException", new SandboxClass(java.lang.ClassCastException.class, true, true, false, false));
		put("java.lang.ArrayStoreException", new SandboxClass(java.lang.ArrayStoreException.class, true, true, false, false));
		put("java.lang.ref.Reference", new SandboxClass(java.lang.ref.Reference.class, true, true, false, false));
		put("java.lang.ref.SoftReference", new SandboxClass(java.lang.ref.SoftReference.class, true, true, false, false));
		put("java.lang.ref.WeakReference", new SandboxClass(java.lang.ref.WeakReference.class, true, true, false, false));
		put("java.lang.ref.PhantomReference", new SandboxClass(java.lang.ref.PhantomReference.class, true, true, false, false));
		put("net.mosstest.sandbox.lang.Thread", new SandboxClass(net.mosstest.sandbox.lang.Thread.class, true, true, false, false));
		put("java.lang.Runnable", new SandboxClass(java.lang.Runnable.class, true, true, false, true));
		put("net.mosstest.sandbox.lang.ref.SoftReference", new SandboxClass(net.mosstest.sandbox.lang.ThreadGroup.class, true, true, false, false));
		put("java.util.Properties", new SandboxClass(java.util.Properties.class, true, true, false, false));
		put("java.util.Hashtable", new SandboxClass(java.util.Hashtable.class, true, true, false, false));
		put("java.util.Map", new SandboxClass(java.util.Map.class, true, true, false, false));
		put("java.util.Dictionary", new SandboxClass(java.util.Dictionary.class, true, true, false, false));
		put("java.util.Vector", new SandboxClass(java.util.Vector.class, true, true, false, false));
		put("java.util.List", new SandboxClass(java.util.List.class, true, true, false, false));
		put("java.util.Collection", new SandboxClass(java.util.Collection.class, true, true, false, true));
		put("java.lang.Iterable", new SandboxClass(java.lang.Iterable.class, true, true, false, true));
		put("java.util.RandomAccess", new SandboxClass(java.util.RandomAccess.class, true, true, false, true));
		put("java.util.AbstractList", new SandboxClass(java.util.AbstractList.class, true, true, false, false));
		put("java.util.AbstractCollection", new SandboxClass(java.util.AbstractCollection.class, true, true, false, false));
		put("java.lang.StringBuffer", new SandboxClass(java.lang.StringBuffer.class, true, true, false, false));
		put("java.lang.Appendable", new SandboxClass(java.lang.Appendable.class, true, true, false, false));
		put("net.mosstest.sandbox.lang.StackTraceElement", new SandboxClass(net.mosstest.sandbox.lang.StackTraceElement.class, true, true, false, false));
		put("java.lang.Boolean", new SandboxClass(java.lang.Boolean.class, true, true, false, false));
		put("java.lang.Character", new SandboxClass(java.lang.Character.class, true, true, false, false));
		put("java.lang.Float", new SandboxClass(java.lang.Float.class, true, true, false, false));
		put("java.lang.Number", new SandboxClass(java.lang.Number.class, true, true, false, false));
		put("java.lang.Double", new SandboxClass(java.lang.Double.class, true, true, false, false));
		put("java.lang.Byte", new SandboxClass(java.lang.Byte.class, true, true, false, false));
		put("java.lang.Short", new SandboxClass(java.lang.Short.class, true, true, false, false));
		put("java.lang.Integer", new SandboxClass(java.lang.Integer.class, true, true, false, false));
		put("java.lang.Long", new SandboxClass(java.lang.Long.class, true, true, false, false));
		put("java.lang.NullPointerException", new SandboxClass(java.lang.NullPointerException.class, true, true, false, false));
		put("java.lang.ArithmeticException", new SandboxClass(java.lang.ArithmeticException.class, true, true, false, false));
		put("java.util.Comparator", new SandboxClass(java.util.Comparator.class, true, true, false, true));
		put("java.util.AbstractMap", new SandboxClass(java.util.AbstractMap.class, true, true, false, false));
		put("java.util.HashMap", new SandboxClass(java.util.HashMap.class, true, true, false, false));
		put("java.util.Stack", new SandboxClass(java.util.Stack.class, true, true, false, false));
		put("net.mosstest.sandbox.util.Enumeration", new SandboxClass(net.mosstest.sandbox.util.Enumeration.class, true, true, false, false));
		put("java.util.Iterator", new SandboxClass(java.util.Iterator.class, true, true, false, false));
		put("net.mosstest.sandbox.util.concurrent.atomic.AtomicReferenceFieldUpdater", new SandboxClass(net.mosstest.sandbox.util.concurrent.atomic.AtomicReferenceFieldUpdater.class, true, true, false, false));
		put("java.lang.NoSuchMethodError", new SandboxClass(java.lang.NoSuchMethodError.class, true, true, false, false));
		put("net.mosstest.sandbox.util.Collections", new SandboxClass(net.mosstest.sandbox.util.Collections.class, true, false, false, false));
		put("java.util.AbstractSet", new SandboxClass(java.util.AbstractSet.class, true, true, false, false));
		put("java.util.Set", new SandboxClass(java.util.Set.class, true, true, false, true));
		put("java.lang.ThreadLocal", new SandboxClass(net.mosstest.sandbox.lang.ThreadLocal.class, false, false, false, false));
		put("java.util.concurrent.atomic.AtomicInteger", new SandboxClass(java.util.concurrent.atomic.AtomicInteger.class, true, true, false, false));
		put("java.util.Arrays", new SandboxClass(java.util.Arrays.class, true, false, false, false));
		put("java.lang.Math", new SandboxClass(java.lang.Math.class, true, false, false, false));
		put("java.lang.StringBuilder", new SandboxClass(java.lang.StringBuilder.class, true, true, false, false));
		put("net.mosstest.sandbox.lang.Runtime", new SandboxClass(net.mosstest.sandbox.lang.Runtime.class, true, true, false, false));
		put("java.lang.Readable", new SandboxClass(java.lang.Readable.class, true, true, false, false));
		put("java.util.LinkedHashMap", new SandboxClass(java.util.LinkedHashMap.class, true, true, false, false));
		put("java.util.ArrayList", new SandboxClass(java.util.ArrayList.class, true, true, false, false));
		put("java.util.IdentityHashMap", new SandboxClass(java.util.IdentityHashMap.class, true, true, false, false));
		put("java.util.StringTokenizer", new SandboxClass(java.util.StringTokenizer.class, true, true, false, false));
		put("java.util.concurrent.ConcurrentHashMap", new SandboxClass(java.util.concurrent.ConcurrentHashMap.class, true, true, false, false));
		put("java.util.concurrent", new SandboxClass(java.util.concurrent.ConcurrentMap.class, true, true, false, false));
		put("java.util.concurrent.locks.ReentrantLock", new SandboxClass(java.util.concurrent.locks.ReentrantLock.class, true, true, false, false));
		put("java.util.concurrent.locks.Lock", new SandboxClass(java.util.concurrent.locks.Lock.class, true, true, false, false));
		put("java.util.concurrent.locks.AbstractQueuedSynchronizer", new SandboxClass(java.util.concurrent.locks.AbstractQueuedSynchronizer.class, true, true, false, false));
		put("java.util.concurrent.locks.AbstractOwnableSynchronizer", new SandboxClass(java.util.concurrent.locks.AbstractOwnableSynchronizer.class, true, true, false, false));
		put("java.util.BitSet", new SandboxClass(java.util.BitSet.class, true, true, false, false));
		put("java.util.HashSet", new SandboxClass(java.util.HashSet.class, true, true, false, false));
		put("java.util.LinkedHashSet", new SandboxClass(java.util.LinkedHashSet.class, true, true, false, false));
		put("java.lang.InterruptedException", new SandboxClass(java.lang.InterruptedException.class, true, true, false, false));
		put("net.mosstest.sandbox.util.ResourceBundle", new SandboxClass(net.mosstest.sandbox.util.ResourceBundle.class, true, true, false, false));
		put("java.util.EventListener", new SandboxClass(java.util.EventListener.class, true, true, false, true));
		put("java.awt.geom.AffineTransform", new SandboxClass(java.awt.geom.AffineTransform.class, true, true, false, false));
		put("java.lang.Enum", new SandboxClass(java.lang.Enum.class, true, true, false, false));
		put("java.util.WeakHashMap", new SandboxClass(java.util.WeakHashMap.class, true, true, false, false));
		put("java.awt.geom.Dimension2D", new SandboxClass(java.awt.geom.Dimension2D.class, true, true, false, false));
		put("java.util.concurrent.atomic.AtomicBoolean", new SandboxClass(java.util.concurrent.atomic.AtomicBoolean.class, true, true, false, false));
		put("java.lang.NoSuchMethodException", new SandboxClass(java.lang.NoSuchMethodException.class, true, true, false, false));
		put("java.awt.geom.Point2D", new SandboxClass(java.awt.geom.Point2D.class, true, true, false, false));
		put("java.lang.UnsupportedOperationException", new SandboxClass(java.lang.UnsupportedOperationException.class, true, true, false, false));
		put("java.awt.geom.Rectangle2D", new SandboxClass(java.awt.geom.Rectangle2D.class, true, true, false, false));
		put("java.awt.geom.RectangularShape", new SandboxClass(java.awt.geom.RectangularShape.class, true, true, false, false));
		put("java.lang.SecurityException", new SandboxClass(java.lang.SecurityException.class, true, true, false, false));
		put("java.util.IllegalStateException", new SandboxClass(java.lang.IllegalStateException.class, true, true, false, false));
		put("java.awt.geom.Path2D", new SandboxClass(java.awt.geom.Path2D.class, true, true, false, false));
		put("java.awt.geom.GeneralPath", new SandboxClass(java.awt.geom.GeneralPath.class, true, true, false, false));
		put("java.util.MissingResourceException", new SandboxClass(java.util.MissingResourceException.class, true, true, false, false));
		put("java.util.LinkedList", new SandboxClass(java.util.LinkedList.class, true, true, false, false));
		put("java.util.Deque", new SandboxClass(java.util.Deque.class, true, true, false, false));
		put("java.util.Queue", new SandboxClass(java.util.Queue.class, true, true, false, false));
		put("java.util.AbstractSequentialList", new SandboxClass(java.util.AbstractSequentialList.class, true, true, false, false));
		put("java.util.EmptyStackException", new SandboxClass(java.util.EmptyStackException.class, true, true, false, false));
		put("java.util.TreeSet", new SandboxClass(java.util.TreeSet.class, true, true, false, false));
		put("java.util.NavigableMap", new SandboxClass(java.util.NavigableMap.class, true, true, false, false));
		put("java.util.SortedMap", new SandboxClass(java.util.SortedMap.class, true, true, false, false));
		put("java.util.ListIterator", new SandboxClass(java.util.ListIterator.class, true, true, false, false));
		put("java.lang.IllegalAccessException", new SandboxClass(java.lang.IllegalAccessException.class, true, true, false, false));
		put("java.util.regex.Pattern", new SandboxClass(java.util.regex.Pattern.class, true, true, false, false));
		put("java.lang.StrictMath", new SandboxClass(java.lang.StrictMath.class, true, false, false, false));
		put("java.lang.NumberFormatException", new SandboxClass(java.lang.NumberFormatException.class, true, true, false, false));
		put("java.lang.IllegalArgumentException", new SandboxClass(java.lang.IllegalArgumentException.class, true, true, false, false));
		put("java.util.Date", new SandboxClass(java.util.Date.class, true, true, false, false));
		put("java.util.TimeZone", new SandboxClass(java.util.TimeZone.class, true, true, false, false));
		put("java.util.regex.Matcher", new SandboxClass(java.util.regex.Matcher.class, true, true, false, false));
		put("java.util.regex.MathResult", new SandboxClass(java.util.regex.MatchResult.class, true, true, false, false));
		put("java.util.Random", new SandboxClass(java.util.Random.class, true, true, false, false));
		put("java.util.concurrent.atomic.AtomicLong", new SandboxClass(java.util.concurrent.atomic.AtomicLong.class, true, true, false, false));
		put("java.lang.InternalError", new SandboxClass(java.lang.InternalError.class, true, true, false, false));
		put("java.lang.NoSuchFieldException", new SandboxClass(java.lang.NoSuchFieldException.class, true, true, false, false));
		put("java.lang.InstantiationException", new SandboxClass(java.lang.InstantiationException.class, true, true, false, false));
		put("java.lang.ArrayIndexOutOfBoundsException", new SandboxClass(java.lang.ArrayIndexOutOfBoundsException.class, true, true, false, false));
		put("java.lang.IllegalAccessError", new SandboxClass(java.lang.IllegalAccessError.class, true, true, false, false));
		put("java.lang.CloneNotSupportedException", new SandboxClass(java.lang.CloneNotSupportedException.class, true, true, false, false));
		put("java.lang.UnsatisfiedLinkError", new SandboxClass(java.lang.UnsatisfiedLinkError.class, true, true, false, false));
		put("java.util.Calendar", new SandboxClass(java.util.Calendar.class, true, true, false, false));
		put("java.util.GregorianCalendar", new SandboxClass(java.util.GregorianCalendar.class, true, true, false, false));
		put("java.util.Currency", new SandboxClass(java.util.Currency.class, true, true, false, false));
		put("java.math.RoundingMode", new SandboxClass(java.math.RoundingMode.class, true, true, false, false));
		put("java.lang.StringIndexOutOfBoundsException", new SandboxClass(java.lang.StringIndexOutOfBoundsException.class, true, true, false, false));
		put("java.math.BigInteger", new SandboxClass(java.math.BigInteger.class, true, true, false, false));
		put("java.util.EnumMap", new SandboxClass(java.util.EnumMap.class, true, true, false, false));
		put("net.mosstest.sandbox.lang.SecurityManager", new SandboxClass(net.mosstest.sandbox.lang.SecurityManager.class, false, false, false, false));
	}};
	
	public static Object getInstance(String clazz, Object... constructorParams) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SandboxClass<?> sc=qualifiedClasses.get(clazz);
		return sc.getInstance(constructorParams);
	}
	
	public static Object invokeStatic(String clazz, String method,
			Object... parameters) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SandboxClass<?> sc=qualifiedClasses.get(clazz);
		return sc.invokeStatic(method, parameters);
	}
}
