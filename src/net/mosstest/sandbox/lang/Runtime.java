package net.mosstest.sandbox.lang;

// TODO: Auto-generated Javadoc
/**
 * The Class Runtime.
 */
public class Runtime {
	
	/** The runtime. */
	private java.lang.Runtime runtime = java.lang.Runtime.getRuntime();

	/**
	 * Available processors.
	 *
	 * @return the int
	 */
	public int availableProcessors() {
		return this.runtime.availableProcessors();
	}
	
	/**
	 * Free memory.
	 *
	 * @return the long
	 */
	public long freeMemory() {
		return this.runtime.freeMemory();
	}
	
	/**
	 * Total memory.
	 *
	 * @return the long
	 */
	public long totalMemory() {
		return this.runtime.totalMemory();
	}
	
	/**
	 * Max memory.
	 *
	 * @return the long
	 */
	public long maxMemory() {
		return this.runtime.maxMemory();
	}
	
	/**
	 * Gc.
	 */
	public void gc() {
		this.runtime.gc();
		return;
	}
	
	/**
	 * Instantiates a new runtime.
	 */
	public Runtime() {
	}
}
