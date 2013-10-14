package net.mosstest.servercore;

import net.mosstest.scripting.DefaultNodeParams;
import net.mosstest.scripting.NodeParams;

public class MapNode {
	private short nodeId = 0;
	public final NodeParams nodeparams;
	public final String texture;

	public String nodeName;
	public String userFacingName;
	public int lightEmission;
	public MossItem dropItem;
	public boolean isBuildableTo = true;

	public double boxOriginX = 0, boxOriginY = 0, boxOriginZ = 0;
	/**
	 * Each of these is 0.5 for the unit box, since each vertex is +/-0.5 from
	 * the origin in each dimension.
	 */
	public double lengthX = 0.5, lengthY = 0.5, lengthZ = 0.5;

	public MapNode(NodeParams nodeparams, String texture, String nodeName,
			String userFacingName, int lightEmission) {
		this.nodeparams = nodeparams;
		this.texture = texture;
		this.nodeName = nodeName;

		try {
			setNodeId(NodeManager.putNode(this));
		} catch (MossWorldLoadException e) {
			// pass
		}

		this.userFacingName = userFacingName;
		this.lightEmission = lightEmission;
		this.dropItem = ItemManager.getForNode(this);

	}

	public void setByBounds(double x1, double x2, double y1, double y2,
			double z1, double z2) {
		this.boxOriginX = (x1 + x2) / 2;
		this.boxOriginY = (y1 + y2) / 2;
		this.boxOriginZ = (z1 + z2) / 2;
		this.lengthX = Math.abs(x2 - x1);
		this.lengthY = Math.abs(y2 - y1);
		this.lengthZ = Math.abs(z2 - z1);
	}

	public void setBySize(double originX, double originY, double originZ,
			double lengthX, double lengthY, double lengthZ) {
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
		try {
			setNodeId(NodeManager.putNode(this));
		} catch (MossWorldLoadException e) {
			// pass
		}
		this.nodeName = nodeName;
		this.userFacingName = userFacingName;
		this.lightEmission = lightEmission;
		this.dropItem = ItemManager.getForNode(this);
	}

	public short getNodeId() {
		return this.nodeId;
	}

	void setNodeId(short nodeId) {
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

}
