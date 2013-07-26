package org.nodetest.servercore;

import org.nodetest.scripting.EventProcessingCompletedSignal;
import org.nodetest.scripting.MossScriptEnv;

/**
 * Interface to specify handlers to be called when various actions are taken by
 * players directed at a node. These handlers are called after the ones defined
 * in {@link MossScriptEnv} but before the default handlers. Handlers have a
 * chance to throw {@link EventProcessingCompletedSignal} to bypass the default
 * handler.
 * 
 * @author rarkenin
 * 
 */
public interface NodeParams {
	/**
	 * Function that is called when a node is punched.
	 * 
	 * @param player
	 *            The player punching the node.
	 * @param tool
	 *            The tool being used to punch the node.
	 * @param target
	 *            The position of the targeted node.
	 * @param punchedFace
	 *            The face being punched.
	 */
	public abstract void onPunch(Player player, GenericTool tool,
			NodePosition target, Face punchedFace)
			throws EventProcessingCompletedSignal;

	/**
	 * Called when a node is fully dug.
	 * 
	 * @param player
	 *            The player digging the node.
	 * @param tool
	 *            The tool used to dig the node.
	 * @param target
	 *            The position of the node that has been dug.
	 * @param punchedFace
	 *            The face on which the the tool was pointed when digging was
	 *            complete. Note that if a dig begins on one face but the player
	 *            moves the aim to another face the onPunch handler may receive
	 *            a different face than this handler.
	 * @throws EventProcessingCompletedSignal
	 */
	public abstract void onDig(Player player, GenericTool tool,
			NodePosition target, Face punchedFace)
			throws EventProcessingCompletedSignal;

	/**
	 * Called when a node is placed directly above, below, left, right, in front
	 * of, or behind this node.
	 * 
	 * @param player
	 *            The player placing the node.
	 * @param target
	 * @param placed
	 * @throws EventProcessingCompletedSignal
	 */
	public abstract void onPlaceNextTo(Player player, NodePosition target,
			NodePosition placed) throws EventProcessingCompletedSignal;

	/**
	 * Called when a right-click is done on a node. Note that this is called
	 * even if no node is placed, such as if the player is not holding anything,
	 * or is holding a tool.
	 * 
	 * @param player
	 *            Player performing the action.
	 * @param tool
	 *            Tool held by user in active slot during this action.
	 * @param target
	 *            Position of targeted node
	 * @param clickedFace
	 *            Face which was right-clicked.
	 * @throws EventProcessingCompletedSignal
	 */
	public abstract void onRightClick(Player player, GenericTool tool,
			NodePosition target, Face clickedFace)
			throws EventProcessingCompletedSignal;

	/**
	 * Called when a player steps on a node either from jumping, falling, or
	 * walking forward.
	 * 
	 * @param player
	 *            Player that steps on this node.
	 * @param pos
	 *            Position of node that is stepped on.
	 * @return Currently unused.
	 * @throws EventProcessingCompletedSignal
	 */
	public abstract boolean onStepOn(Player player, NodePosition pos)
			throws EventProcessingCompletedSignal;

	/**
	 * Calculates the height a player may jump off of this node. One node is
	 * equal to 1.0.
	 * 
	 * @param player
	 * @return The height the player may jump off this node.
	 * @throws EventProcessingCompletedSignal
	 */
	public abstract double jumpOffHeight(Player player)
			throws EventProcessingCompletedSignal;

	/**
	 * Calculates the height a player will bounce when landing on this node.
	 * 
	 * @param player
	 * @param fallheight
	 *            The height from which the player has fallen.
	 * @return A double value representing the height the player is to bounce.
	 * @throws EventProcessingCompletedSignal
	 */
	public abstract double calcBounceHeight(Player player, double fallheight)
			throws EventProcessingCompletedSignal;

	public abstract double calcSinkIn(Player player, double fallheight)
			throws EventProcessingCompletedSignal;

	public abstract double calcSinkEscape(Player player, double fallheight)
			throws EventProcessingCompletedSignal;

	public abstract double calcWalkSpeed(Player player)
			throws EventProcessingCompletedSignal;

	public abstract double calcSprintSpeed(Player player)
			throws EventProcessingCompletedSignal;

}
