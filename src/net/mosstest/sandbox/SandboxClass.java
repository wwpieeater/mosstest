package net.mosstest.sandbox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// TODO: Auto-generated Javadoc
/**
 * The Class SandboxClass.
 *
 * @param <T> the generic type
 */
public class SandboxClass<T> {
	
	/** The clazz. */
	Class<T> clazz;
	
	/** The iface. */
	final boolean allowStatic, instantiable, deny, iface;

	/**
	 * Instantiates a new sandbox class.
	 *
	 * @param clazz the clazz
	 * @param allowStatic the allow static
	 * @param instantiable the instantiable
	 * @param deny the deny
	 * @param iface the iface
	 */
	public SandboxClass(Class<T> clazz, boolean allowStatic,
			boolean instantiable, boolean deny, boolean iface) {
		this.clazz = clazz;
		this.allowStatic = allowStatic;
		this.instantiable = instantiable;
		this.deny = deny;
		this.iface = iface;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.clazz == null) ? 0 : this.clazz.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SandboxClass)) {
			return false;
		}
		SandboxClass<?> other = (SandboxClass<?>) obj;
		if (this.clazz == null) {
			if (other.clazz != null) {
				return false;
			}
		} else if (!this.clazz.equals(other.clazz)) {
			return false;
		}
		return true;
	}

	/**
	 * Invoke static.
	 *
	 * @param method the method
	 * @param params the params
	 * @return the object
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public Object invokeStatic(String method, Object... params)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Class<?>[] clazzes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			clazzes[i] = params[i].getClass();
		}
		Method mth = this.clazz.getMethod(method, clazzes);
		return mth.invoke(null);

	}

	/**
	 * Gets the single instance of SandboxClass.
	 *
	 * @param params the params
	 * @return single instance of SandboxClass
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public T getInstance(Object... params) throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?>[] clazzes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			clazzes[i] = params[i].getClass();
		}
		Constructor<T> constr = this.clazz.getConstructor(clazzes);
		return constr.newInstance(params);
	}

}
