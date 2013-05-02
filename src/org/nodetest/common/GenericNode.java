package org.nodetest.common;

import org.nodetest.client.Drawable3D;
import org.nodetest.client.DrawableWorld;
import org.nodetest.client.GenericTexture;
import org.nodetest.common.Face;
import org.nodetest.common.Player;

public class GenericNode {

	static long nodeId;
	public void draw(DrawableWorld world) {
		world.addBlockMesh(this);
	}
		
	public GenericTexture textureSpace;
	
	public String nodeName;
	public String userFacingName;
	public boolean isLiquid;
	public int emitsLight;	
	
}
