package net.mosstest.scripting;

import net.mosstest.scripting.MossTool.InteractType;


public class DefaultNodeParams implements INodeParams {

	@Override
	public boolean onStepOn(Player player, NodePosition pos) {
		return true;
	}

	@Override
	public void onRightClick(Player player, MossItem tool,
			NodePosition target, Face clickedFace) {
		return;

	}

	@Override
	public void onPunch(Player player, MossItem tool,
			NodePosition target, Face punchedFace) {
		return;

	}

	@Override
	public void onPlaceNextTo(Player player, NodePosition target,
			NodePosition placed) {
		return;
	}

	@Override
	public void onDig(Player player, MossItem tool, NodePosition target,
			Face punchedFace) {
		return;
	}

	@Override
	public double jumpOffHeight(Player player) {
		return 1.125;
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
	public double calcSinkIn(Player player, double fallheight) {
		return 0;
	}

	@Override
	public double calcSinkEscape(Player player, double fallheight) {
		return 1;
	}

	@Override
	public double calcBounceHeight(Player player, double fallheight) {
		return 0.125;
	}

	@Override
	public double calcFallDamage(Player player, double height) {
		return 0;
	}

	@Override
	public boolean shouldContinueFall(Player player, double height) {
		return false;
	}

	@Override
	public boolean shouldHitAimRay(Player player, double distance) {
		return false;
	}

	@Override
	public double calcSinkActive(Player player, double fallheight) {
		return 0;
	}

	//FIXME move to interact params
	@Override
	public double calcInteractProperties(InteractType key) {
		return 1;
	}
}