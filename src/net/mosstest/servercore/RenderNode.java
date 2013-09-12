package net.mosstest.servercore;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class RenderNode extends Geometry {
	MapNode definition;
	
	public RenderNode (Material mat, Vector3f loc, float blockSize, MapNode def) {
		super("Box", new Box(loc, blockSize, blockSize/2, blockSize));
		super.setMaterial(mat);
		definition = def;
	}
}
