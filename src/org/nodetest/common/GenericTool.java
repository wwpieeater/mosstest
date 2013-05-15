package org.nodetest.common;

import org.nodetest.client.GameMesh;

public abstract class GenericTool {
	public abstract void onPunch(Player player, GenericTool tool, NodeInstance target, Face punchedFace);
	public abstract void onDig(Player player, GenericTool tool, NodeInstance target, Face punchedFace);
	public abstract void onRightClick(Player player, GenericTool tool, NodeInstance target, Face clickedFace);
	public GameMesh wieldMesh;
	//TODO;
}
