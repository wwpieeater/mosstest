package net.mosstest.servercore;

import net.mosstest.scripting.LiquidNodeParams;

public class LiquidNode extends MapNode {
	public final LiquidNodeParams nodeParams;
	public LiquidNode[] liquidLevels = new LiquidNode[8];
	public int level;
	public LiquidNode(LiquidNodeParams nodeparams, String texture,
			String nodeName, String userFacingName, int lightEmission) {
		super(nodeparams, texture, nodeName, userFacingName, lightEmission);
		this.nodeParams = nodeparams;

	}

}
