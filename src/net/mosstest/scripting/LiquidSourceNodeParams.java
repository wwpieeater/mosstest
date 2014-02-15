package net.mosstest.scripting;


// TODO: Auto-generated Javadoc
/**
 * The Class LiquidSourceNodeParams.
 */
public class LiquidSourceNodeParams extends DefaultNodeParams implements
		LiquidNodeParams {

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.LiquidNodeParams#calcFlowRate(net.mosstest.scripting.NodePosition)
	 */
	@Override
	public double calcFlowRate(NodePosition pos) {
		return 1;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.LiquidNodeParams#onLiquidFlow(net.mosstest.scripting.NodePosition)
	 */
	@Override
	public void onLiquidFlow(NodePosition pos) {
		// pass
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.LiquidNodeParams#calcDrainRate(net.mosstest.scripting.NodePosition)
	 */
	@Override
	public double calcDrainRate(NodePosition pos) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.LiquidNodeParams#shouldFill(int, int)
	 */
	@Override
	public boolean shouldFill(int contacting, int flowing) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#jumpOffHeight(net.mosstest.scripting.Player)
	 */
	@Override
	public double jumpOffHeight(Player player) {
		return 1.125;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#calcWalkSpeed(net.mosstest.scripting.Player)
	 */
	@Override
	public double calcWalkSpeed(Player player) {
		return 0.6;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#calcSprintSpeed(net.mosstest.scripting.Player)
	 */
	@Override
	public double calcSprintSpeed(Player player) {
		return 0.8;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#calcSinkIn(net.mosstest.scripting.Player, double)
	 */
	@Override
	public double calcSinkIn(Player player, double fallheight) {
		return 0.4;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#calcSinkEscape(net.mosstest.scripting.Player, double)
	 */
	@Override
	public double calcSinkEscape(Player player, double fallheight) {
		return 1;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#calcBounceHeight(net.mosstest.scripting.Player, double)
	 */
	@Override
	public double calcBounceHeight(Player player, double fallheight) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#calcFallDamage(net.mosstest.scripting.Player, double)
	 */
	@Override
	public double calcFallDamage(Player player, double height) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#shouldContinueFall(net.mosstest.scripting.Player, double)
	 */
	@Override
	public boolean shouldContinueFall(Player player, double height) {
		return false;
	}

}
