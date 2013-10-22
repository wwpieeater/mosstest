package net.mosstest.scripting;

import net.mosstest.servercore.ItemManager;

public class MapNode {
	private short nodeId = 0;
	public final transient NodeParams nodeparams;
	public final transient String texture;

	public String nodeName;
	public String userFacingName;
	public int lightEmission;
	public MossItem dropItem;
	public boolean isBuildableTo = true;
	public DrawType drawType = DrawType.DRAW_BLOCK;
	public float boxOriginX = 0, boxOriginY = 0, boxOriginZ = 0;
	/**
	 * Each of these is 0.5 for the unit box, since each vertex is +/-0.5 from
	 * the origin in each dimension.
	 */
	public float lengthX = 0.5f, lengthY = 0.5f, lengthZ = 0.5f;

	public MapNode(NodeParams nodeparams, String texture, String nodeName,
			String userFacingName, int lightEmission) {
		this.nodeparams = nodeparams;
		this.texture = texture;
		this.nodeName = nodeName;

		this.userFacingName = userFacingName;
		this.lightEmission = lightEmission;
		this.dropItem = ItemManager.getForNode(this);

	}

	public void setByBounds(float x1, float x2, float y1, float y2, float z1,
			float z2) {
		this.boxOriginX = (x1 + x2) / 2;
		this.boxOriginY = (y1 + y2) / 2;
		this.boxOriginZ = (z1 + z2) / 2;
		this.lengthX = Math.abs(x2 - x1);
		this.lengthY = Math.abs(y2 - y1);
		this.lengthZ = Math.abs(z2 - z1);
	}

	public void setBySize(float originX, float originY, float originZ,
			float lengthX, float lengthY, float lengthZ) {
		this.boxOriginX = originX;
		this.boxOriginY = originY;
		this.boxOriginZ = originZ;
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.lengthZ = lengthZ;
	}

	public MapNode(String textures, String nodeName, String userFacingName,
			int lightEmission) {
		this.nodeparams = new DefaultNodeParams();
		this.texture = textures;

		this.nodeName = nodeName;
		this.userFacingName = userFacingName;
		this.lightEmission = lightEmission;
		this.dropItem = ItemManager.getForNode(this);
	}

	public short getNodeId() {
		return this.nodeId;
	}

	/**
	 * Sets node ID. Scripts should not call this except under special
	 * circumstances.
	 * 
	 * @param nodeId
	 */
	public void setNodeId(short nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.nodeId;
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
		if (!(obj instanceof MapNode)) {
			return false;
		}
		MapNode other = (MapNode) obj;
		if (this.nodeId != other.nodeId) {
			return false;
		}
		return true;
	}

	protected MapNode() {
		this.texture = null;
		this.nodeparams = null;
	}

	public enum DrawType {
		/**
		 * Standard appearance for blocks
		 */
		DRAW_BLOCK,
		/**
		 * Drawtype for liquids
		 */
		DRAW_LIQUID,
		/**
		 * Drawtype for plantlike materials. In this draw, two planes with the
		 * item texture(which should be symmetrical for this) are drawn
		 * intersecting in an X-pattern.
		 */
		DRAW_PLANTLIKE,

		/**
		 * Do not draw the node at all.
		 */
		DRAW_NONE
	}

}
