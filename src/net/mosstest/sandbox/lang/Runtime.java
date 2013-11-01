package net.mosstest.sandbox.lang;

public class Runtime {
	private java.lang.Runtime runtime = java.lang.Runtime.getRuntime();

	public int availableProcessors() {
		return this.runtime.availableProcessors();
	}
	
	public long freeMemory() {
		return this.runtime.freeMemory();
	}
	
	public long totalMemory() {
		return this.runtime.totalMemory();
	}
	
	public long maxMemory() {
		return this.runtime.maxMemory();
	}
	public void gc() {
		this.runtime.gc();
		return;
	}
	
	public Runtime() {
	}
}
