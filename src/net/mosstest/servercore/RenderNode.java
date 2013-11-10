package net.mosstest.servercore;

import net.mosstest.scripting.MapNode;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;

public class RenderNode extends Geometry {
	MapNode definition;
	//THIS WILL NEED TO BE MODIFIED LATER
	public RenderNode (Material mat, Mesh mesh, MapNode def) {
		//super(""
		super.setMaterial(mat);
		definition = def;
	}
}
