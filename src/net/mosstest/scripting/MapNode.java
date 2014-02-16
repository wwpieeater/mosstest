package net.mosstest.scripting;

import net.mosstest.servercore.ItemManager;

// TODO: Auto-generated Javadoc
/**
 * The Class MapNode.
 */
public class MapNode {
	
	/** The node id. */
	private short nodeId = 0;
	
	/** The nodeparams. */
	public final transient INodeParams nodeparams;
	
	/** The texture. */
	public final transient String texture;
	
	/** The node name. */
	public String nodeName;
	
	/** The user facing name. */
	public String userFacingName;
	
	/** The light emission. */
	public int lightEmission;
	
	/** The drop item. */
	public MossItem dropItem;
	
	/** The is buildable to. */
	public boolean isBuildableTo = true;
	
	/** The draw type. */
	public DrawType drawType = DrawType.DRAW_BLOCK;
	
	/** The box origin z. */
	public float boxOriginX = 0, boxOriginY = 0, boxOriginZ = 0;
	/**
	 * Each of these is 0.5 for the unit box, since each vertex is +/-0.5 from
	 * the origin in each dimension.
	 */
	public float lengthX = 0.5f, lengthY = 0.5f, lengthZ = 0.5f;

	/**
	 * Instantiates a new map node.
	 *
	 * @param nodeparams the nodeparams
	 * @param texture the texture
	 * @param nodeName the node name
	 * @param userFacingName the user facing name
	 * @param lightEmission the light emission
	 */
	public MapNode(INodeParams nodeparams, String texture, String nodeName,
			String userFacingName, int lightEmission) {
		this.nodeparams = nodeparams;
		this.texture = texture;
		this.nodeName = nodeName;

		this.userFacingName = userFacingName;
		this.lightEmission = lightEmission;
		this.dropItem = ItemManager.getForNode(this);

	}

	/**
	 * Sets the by bounds.
	 *
	 * @param x1 the x1
	 * @param x2 the x2
	 * @param y1 the y1
	 * @param y2 the y2
	 * @param z1 the z1
	 * @param z2 the z2
	 */
	public void setByBounds(float x1, float x2, float y1, float y2, float z1,
			float z2) {
		this.boxOriginX = (x1 + x2) / 2;
		this.boxOriginY = (y1 + y2) / 2;
		this.boxOriginZ = (z1 + z2) / 2;
		this.lengthX = Math.abs(x2 - x1);
		this.lengthY = Math.abs(y2 - y1);
		this.lengthZ = Math.abs(z2 - z1);
	}

	/**
	 * Sets the by size.
	 *
	 * @param originX the origin x
	 * @param originY the origin y
	 * @param originZ the origin z
	 * @param lengthX the length x
	 * @param lengthY the length y
	 * @param lengthZ the length z
	 */
	public void setBySize(float originX, float originY, float originZ,
			float lengthX, float lengthY, float lengthZ) {
		this.boxOriginX = originX;
		this.boxOriginY = originY;
		this.boxOriginZ = originZ;
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.lengthZ = lengthZ;
	}

	/**
	 * Instantiates a new map node.
	 *
	 * @param textures the textures
	 * @param nodeName the node name
	 * @param userFacingName the user facing name
	 * @param lightEmission the light emission
	 */
	public MapNode(String textures, String nodeName, String userFacingName,
			int lightEmission) {
		this.nodeparams = new DefaultNodeParams();
		this.texture = textures;

		this.nodeName = nodeName;
		this.userFacingName = userFacingName;
		this.lightEmission = lightEmission;
		this.dropItem = ItemManager.getForNode(this);
	}

	/**
	 * Gets the node id.
	 *
	 * @return the node id
	 */
	public short getNodeId() {
		return this.nodeId;
	}

	/**
	 * Sets node ID. Scripts should not call this except under special
	 * circumstances.
	 *
	 * @param nodeId the new node id
	 */
	public void setNodeId(short nodeId) {
		this.nodeId = nodeId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.nodeId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/**
	 * Instantiates a new map node.
	 */
	protected MapNode() {
		this.texture = null;
		this.nodeparams = null;
	}

	/**
	 * The Enum DrawType.
	 */
	public enum DrawType {
		
		/** Standard appearance for blocks. */
		DRAW_BLOCK,
		
		/** Drawtype for liquids. */
		DRAW_LIQUID,
		/**
		 * Drawtype for plantlike materials. In this draw, two planes with the
		 * item texture(which should be symmetrical for this) are drawn
		 * intersecting in an X-pattern.
		 */
		DRAW_PLANTLIKE,

		/**
		 * Drawtype for glass. The node should only have boundaries drawn if it
		 * is contacting a node of a differing draw type <i>or a different glass
		 * node</i>.
		 */
		DRAW_GLASS,

		/**
		 * The node is to be drawn as a flat plane in the bottom of the cubic
		 * area for this node unless an adjacent node to the side and UP one
		 * unit from this one also contains an identical node. In that case this
		 * node shall be drawn as a diagonal plane to connect to that node.
		 */
		DRAW_RAIL,

		/**
		 * Do not draw the node at all.
		 */
		DRAW_NONE
	}

}
