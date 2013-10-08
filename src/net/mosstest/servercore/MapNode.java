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
