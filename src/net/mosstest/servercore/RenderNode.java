package net.mosstest.servercore;

import net.mosstest.scripting.MapNode;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;

// TODO: Auto-generated Javadoc
/**
 * The Class RenderNode.
 */
public class RenderNode extends Geometry {
	
	/** The definition. */
	MapNode definition;
	//THIS WILL NEED TO BE MODIFIED LATER
	/**
	 * Instantiates a new render node.
	 *
	 * @param mat the mat
	 * @param mesh the mesh
	 * @param def the def
	 */
	public RenderNode (Material mat, Mesh mesh, MapNode def) {
		//super(""
		super.setMaterial(mat);
		definition = def;
	}
}
