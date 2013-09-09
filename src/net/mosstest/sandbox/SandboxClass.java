package net.mosstest.sandbox;

public class SandboxClass {
	Class<?> clazz;
	final boolean allowStatic, instantiable, deny, iface;
	public SandboxClass(Class<?> clazz, boolean allowStatic,
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
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
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
		SandboxClass other = (SandboxClass) obj;
		if (clazz == null) {
			if (other.clazz != null) {
				return false;
			}
		} else if (!clazz.equals(other.clazz)) {
			return false;
		}
		return true;
	}
	
	
}
