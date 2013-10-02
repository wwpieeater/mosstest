package net.mosstest.scripting;

import net.mosstest.servercore.Face;
import net.mosstest.servercore.GenericTool;
import net.mosstest.servercore.NodePosition;
import net.mosstest.servercore.Player;

public class DefaultNodeParams implements NodeParams {

	@Override
	public boolean onStepOn(Player player, NodePosition pos) {
		return true;
	}

	@Override
	public void onRightClick(Player player, GenericTool tool,
			NodePosition target, Face clickedFace) {
		return;

	}

	@Override
	public void onPunch(Player player, GenericTool tool,
			NodePosition target, Face punchedFace) {
		return;

	}

	@Override
	public void onPlaceNextTo(Player player, NodePosition target,
			NodePosition placed) {
		return;
	}

	@Override
	public void onDig(Player player, GenericTool tool, NodePosition target,
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
		// TODO Auto-generated method stub
		return 0.125;
	}

	@Override
	public double calcFallDamage(Player player, double height) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean shouldContinueFall(Player player, double height) {
		return false;
	}
}