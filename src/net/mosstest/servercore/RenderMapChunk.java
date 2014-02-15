package net.mosstest.servercore;

import net.mosstest.scripting.MapNode;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
// TODO: Auto-generated Javadoc

/**
 * The Class RenderMapChunk.
 */
@Deprecated
public class RenderMapChunk {
	
	/** The my nodes. */
	private RenderNode[][][] myNodes = new RenderNode[16][16][16];
	
	/** The z. */
	private int x, y, z;
	
	/**
	 * Instantiates a new render map chunk.
	 *
	 * @param holdThese must be length [16][16][16]
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public RenderMapChunk (RenderNode[][][] holdThese, int x, int y, int z) {
		for(int i = 0; i < holdThese.length; i++) {
			for(int j = 0; j < holdThese[i].length; j++) {
				for(int k = 0; k < holdThese[i][j].length; k++) {
					myNodes[i][j][k] = holdThese[i][j][k];
				}
			}
		}
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Adds the node.
	 *
	 * @param def the def
	 * @param mat the mat
	 * @param size the size
	 * @param i the i
	 * @param j the j
	 * @param k the k
	 */
	public void addNode (MapNode def, Material mat, float size, int i, int j, int k) {
		Vector3f loc = new Vector3f(x+i*size, y-j*size-40, z+k*size);
		//myNodes[i][j][k] = new RenderNode(mat, loc, size, def);
	}
	
	/**
	 * Gets the node loc.
	 *
	 * @param i the i
	 * @param j the j
	 * @param k the k
	 * @param size the size
	 * @return the node loc
	 */
	public Vector3f getNodeLoc (int i, int j, int k, float size) {
		Vector3f loc = new Vector3f(x*i*size, y-j*size, z+k*size);
		return loc;
	}
}
