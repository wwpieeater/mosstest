package net.mosstest.scripting;

import net.mosstest.scripting.MossTool.InteractType;


public class AirNodeParams implements INodeParams {

	@Override
	public void onPunch(Player player, MossItem tool,
			NodePosition target, Face punchedFace)
			throws EventProcessingCompletedSignal {
		// pass

	}

	@Override
	public void onDig(Player player, MossItem tool, NodePosition target,
			Face punchedFace) throws EventProcessingCompletedSignal {
		// pass

	}

	@Override
	public void onPlaceNextTo(Player player, NodePosition target,
			NodePosition placed) throws EventProcessingCompletedSignal {
		// pass

	}

	@Override
	public void onRightClick(Player player, MossItem tool,
			NodePosition target, Face clickedFace)
			throws EventProcessingCompletedSignal {
		// pass

	}

	@Override
	public boolean onStepOn(Player player, NodePosition pos)
			throws EventProcessingCompletedSignal {
		// pass
		return false;
	}

	@Override
	public double jumpOffHeight(Player player) {
		return 0;
	}

	@Override
	public double calcBounceHeight(Player player, double fallheight) {
		return 0;
	}

	@Override
	public double calcSinkIn(Player player, double fallheight) {
		return 1;
	}

	@Override
	public double calcSinkEscape(Player player, double sinkheight) {
		return 1;
	}

	@Override
	public double calcWalkSpeed(Player player) {
		return 1;
	}

	@Override
	public double calcSprintSpeed(Player player) {
		return 4;
	}

	@Override
	public double calcFallDamage(Player player, double height) {
		return 0;
	}

	@Override
	public boolean shouldContinueFall(Player player, double height) {
		return true;
	}

	@Override
	public boolean shouldHitAimRay(Player player, double distance) {
		return false;
	}



	@Override
	public double calcSinkActive(Player player, double fallheight) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calcInteractProperties(InteractType key) {
		// TODO Auto-generated method stub
		return 0;
	}

}