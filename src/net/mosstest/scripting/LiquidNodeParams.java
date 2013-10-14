package net.mosstest.scripting;

import net.mosstest.servercore.NodePosition;

public interface LiquidNodeParams extends NodeParams {

	/**
	 * Calculates the flow rate of the liquid
	 * 
	 * @param pos
	 *            The NodePosition of the liquid's location.
	 * @return A double representing the flow rate, in a fully filled node in 1
	 *         second for 1.0
	 */
	public double calcFlowRate(NodePosition pos);
	
	/**
	 * Callback for liquid flow
	 * @param pos Position.
	 */
	public void onLiquidFlow(NodePosition pos);

}
