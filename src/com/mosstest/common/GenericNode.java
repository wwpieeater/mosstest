package com.mosstest.common;

import com.mosstest.common.Face;
import com.mosstest.common.Player;
import com.mosstest.client.Drawable3D;
import com.mosstest.client.DrawableWorld;
import com.mosstest.client.GenericTexture;

public abstract class GenericNode implements Drawable3D {

	@Override
	public void draw(DrawableWorld world) {
		world.addBlockMesh(this);
	}
	
	public GenericTexture textureSpace;
	
	public String nodeName;
	
	public boolean isLiquid;
	public int emitsLight;	
	public abstract void onPunch(Player player, GenericTool tool, Face punchedFace);
	public abstract void onDig(Player player, GenericTool tool, Face punchedFace);
	public abstract void onPlaceNextTo(Player player, GenericTool tool, NodeInstance node);
	public abstract void onRightClick(Player player, GenericTool tool, Face clickedFace);
	public abstract void onWorldTick(World world);
	public abstract boolean onStepOn(Player player);
	public abstract double jumpOffHeight(Player player);
	public abstract double calcBounceHeight(Player player, double fallheight);
	public abstract double calcSinkIn(Player player, double fallheight);
	public abstract double calcSinkEscape(Player player, double fallheight);
	public abstract double calcWalkSpeed(Player player);
	public abstract double calcSprintSpeed(Player player);
	
}
