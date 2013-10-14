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
