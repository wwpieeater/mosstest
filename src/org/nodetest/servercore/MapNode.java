package org.nodetest.servercore;


public class MapNode {
	protected long nodeId;
	public MapNode(NodeParams nodeparams, GenericTexture textureSpace,
			String nodeName, String userFacingName, boolean isLiquid,
			int lightEmission) {
		this.nodeparams = nodeparams;
		this.textureSpace = textureSpace;
		this.nodeName = nodeName;
		this.userFacingName = userFacingName;
		this.isLiquid = isLiquid;
		this.lightEmission = lightEmission;
	}
	public MapNode(GenericTexture textureSpace,
			String nodeName, String userFacingName, boolean isLiquid,
			int lightEmission) {
		this.textureSpace = textureSpace;
		this.nodeName = nodeName;
		this.userFacingName = userFacingName;
		this.isLiquid = isLiquid;
		this.lightEmission = lightEmission;
	}
	public void draw(DrawableWorld world, Position pos) {
		world.addBlockMesh(this, pos);
	}
	public NodeParams nodeparams;	
	public GenericTexture textureSpace;
	public String nodeName;
	public String userFacingName;
	public boolean isLiquid;
	public int lightEmission;	
}
