package net.mosstest.scripting;


// TODO: Auto-generated Javadoc
/**
 * The Class LiquidFlowingNodeParams.
 */
public class LiquidFlowingNodeParams extends DefaultNodeParams implements
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
		return 1;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.LiquidNodeParams#shouldFill(int, int)
	 */
	@Override
	public boolean shouldFill(int contacting, int flowing) {
		return (contacting>=2);
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.DefaultNodeParams#jumpOffHeight(net.mosstest.scripting.Player)
	 */


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

	@Override
	public double calcSinkActive(Player player, double fallheight) {
		return -0.8;
	}
	@Override
	public double calcSinkPassive(Player player, double fallheight) {
		return (fallheight > -0.25)?0:0.05;
	}

	@Override
	public double calcSinkJump(Player player, double sinkheight) {
		return (sinkheight > -0.25)?0:0.6;
	}
}
