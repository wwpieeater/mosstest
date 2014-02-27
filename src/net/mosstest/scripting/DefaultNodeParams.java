package net.mosstest.scripting;

import net.mosstest.scripting.MossTool.InteractType;


// TODO: Auto-generated Javadoc
/**
 * The Class DefaultNodeParams.
 */
public class DefaultNodeParams implements INodeParams {

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#onStepOn(net.mosstest.scripting.Player, net.mosstest.scripting.NodePosition)
	 */
	@Override
	public boolean onStepOn(Player player, NodePosition pos) {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#onRightClick(net.mosstest.scripting.Player, net.mosstest.scripting.MossItem, net.mosstest.scripting.NodePosition, net.mosstest.scripting.Face)
	 */
	@Override
	public void onRightClick(Player player, MossItem tool,
			NodePosition target, Face clickedFace) {
		return;

	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#onPunch(net.mosstest.scripting.Player, net.mosstest.scripting.MossItem, net.mosstest.scripting.NodePosition, net.mosstest.scripting.Face)
	 */
	@Override
	public void onPunch(Player player, MossItem tool,
			NodePosition target, Face punchedFace) {
		return;

	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#onPlaceNextTo(net.mosstest.scripting.Player, net.mosstest.scripting.NodePosition, net.mosstest.scripting.NodePosition)
	 */
	@Override
	public void onPlaceNextTo(Player player, NodePosition target,
			NodePosition placed) {
		return;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#onDig(net.mosstest.scripting.Player, net.mosstest.scripting.MossItem, net.mosstest.scripting.NodePosition, net.mosstest.scripting.Face)
	 */
	@Override
	public void onDig(Player player, MossItem tool, NodePosition target,
			Face punchedFace) {
		return;
	}

	

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#calcWalkSpeed(net.mosstest.scripting.Player)
	 */
	@Override
	public double calcWalkSpeed(Player player) {
		return 1;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#calcSprintSpeed(net.mosstest.scripting.Player)
	 */
	@Override
	public double calcSprintSpeed(Player player) {
		return 4;
	}

	

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#calcBounceHeight(net.mosstest.scripting.Player, double)
	 */
	@Override
	public double calcBounceHeight(Player player, double fallheight) {
		return 0.125;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#calcFallDamage(net.mosstest.scripting.Player, double)
	 */
	@Override
	public double calcFallDamage(Player player, double height) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#shouldContinueFall(net.mosstest.scripting.Player, double)
	 */
	@Override
	public boolean shouldContinueFall(Player player, double height) {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#shouldHitAimRay(net.mosstest.scripting.Player, double)
	 */
	@Override
	public boolean shouldHitAimRay(Player player, double distance) {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#calcSinkActive(net.mosstest.scripting.Player, double)
	 */
	@Override
	public double calcSinkActive(Player player, double fallheight) {
		return 0;
	}

	//FIXME move to interact params
	/* (non-Javadoc)
	 * @see net.mosstest.scripting.INodeParams#calcInteractProperties(net.mosstest.scripting.MossTool.InteractType, double)
	 */
	@Override
	public double calcInteractProperties(InteractType key, double strength) {
		return 1;
	}

	@Override
	public double calcSinkPassive(Player player, double fallheight) {
		return 0;
	}

	@Override
	public double calcSinkJump(Player player, double sinkheight) {
		return 0;
	}
}