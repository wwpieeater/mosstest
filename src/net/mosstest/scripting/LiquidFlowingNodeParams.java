package net.mosstest.scripting;


public class LiquidFlowingNodeParams extends DefaultNodeParams implements
		LiquidNodeParams {

	@Override
	public double calcFlowRate(NodePosition pos) {
		return 1;
	}

	@Override
	public void onLiquidFlow(NodePosition pos) {
		// pass
	}

	@Override
	public double calcDrainRate(NodePosition pos) {
		return 1;
	}

	@Override
	public boolean shouldFill(int contacting, int flowing) {
		// TODO Auto-generated method stub
		return (contacting>=2);
	}

	@Override
	public double jumpOffHeight(Player player) {
		return 1.125;
	}

	@Override
	public double calcWalkSpeed(Player player) {
		return 0.6;
	}

	@Override
	public double calcSprintSpeed(Player player) {
		return 0.8;
	}

	@Override
	public double calcSinkIn(Player player, double fallheight) {
		return 0.4;
	}

	@Override
	public double calcSinkEscape(Player player, double fallheight) {
		return 1;
	}

	@Override
	public double calcBounceHeight(Player player, double fallheight) {
		return 0;
	}

	@Override
	public double calcFallDamage(Player player, double height) {
		return 0;
	}

	@Override
	public boolean shouldContinueFall(Player player, double height) {
		return false;
	}

}
