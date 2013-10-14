package net.mosstest.sandbox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SandboxClass<T> {
	Class<T> clazz;
	final boolean allowStatic, instantiable, deny, iface;

	public SandboxClass(Class<T> clazz, boolean allowStatic,
			boolean instantiable, boolean deny, boolean iface) {
		this.clazz = clazz;
		this.allowStatic = allowStatic;
		this.instantiable = instantiable;
		this.deny = deny;
		this.iface = iface;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.clazz == null) ? 0 : this.clazz.hashCode());
		return result;
	}

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
