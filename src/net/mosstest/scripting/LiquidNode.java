package net.mosstest.scripting;


// TODO: Auto-generated Javadoc
/**
 * The Class LiquidNode.
 */
public class LiquidNode extends MapNode {
	
	/** The node params. */
	public final LiquidNodeParams nodeParams;
	
	/** The liquid levels. */
	public LiquidNode[] liquidLevels = new LiquidNode[8];
	
	/** The level. */
	public int level;
	
	/**
	 * Instantiates a new liquid node.
	 *
	 * @param nodeparams the nodeparams
	 * @param texture the texture
	 * @param nodeName the node name
	 * @param userFacingName the user facing name
	 * @param lightEmission the light emission
	 */
	public LiquidNode(LiquidNodeParams nodeparams, String texture,
			String nodeName, String userFacingName, int lightEmission) {
		super(nodeparams, texture, nodeName, userFacingName, lightEmission);
		this.nodeParams = nodeparams;

	}

}
