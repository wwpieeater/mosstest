package net.mosstest.servercore;

// TODO: Auto-generated Javadoc
/**
 * The Class MossRenderAddAssetPath.
 */
public class MossRenderAddAssetPath extends MossRenderEvent {
	
	/** The path. */
	String path;
	
	/**
	 * Instantiates a new moss render add asset path.
	 *
	 * @param newpath the newpath
	 */
	public MossRenderAddAssetPath (String newpath) {
		path = newpath;
	}
	
	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath () {
		return path;
	}
}
