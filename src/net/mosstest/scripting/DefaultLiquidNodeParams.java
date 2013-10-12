package net.mosstest.scripting;

import net.mosstest.servercore.NodePosition;

public class DefaultLiquidNodeParams extends DefaultNodeParams implements
		LiquidNodeParams {

	@Override
	public double calcFlowRate(NodePosition pos) {
		return 1;
	}

	@Override
	public void onLiquidFlow(NodePosition pos) {
		// pass
	}

}
