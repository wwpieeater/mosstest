package org.nodetest.servercore;

public interface NodeParams {

	public abstract void onPunch(Player player, GenericTool tool, NodeInstance target, Face punchedFace);
	public abstract void onDig(Player player, GenericTool tool, NodeInstance target, Face punchedFace);
	public abstract void onPlaceNextTo(Player player, GenericTool tool, NodeInstance target, NodeInstance placed);
	public abstract void onRightClick(Player player, GenericTool tool, NodeInstance target, Face clickedFace);
	public abstract void onWorldTick(World world);
	public abstract boolean onStepOn(Player player);
	public abstract double jumpOffHeight(Player player);
	public abstract double calcBounceHeight(Player player, double fallheight);
	public abstract double calcSinkIn(Player player, double fallheight);
	public abstract double calcSinkEscape(Player player, double fallheight);
	public abstract double calcWalkSpeed(Player player);
	public abstract double calcSprintSpeed(Player player);

}
