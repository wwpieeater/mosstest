package net.mosstest.servercore;

import net.mosstest.scripting.EventProcessingCompletedSignal;
import net.mosstest.scripting.MossScriptEnv;

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
	 */
	public abstract double jumpOffHeight(Player player);

	/**
	 * Calculates the height a player will bounce when landing on this node.
	 * 
	 * @param player
	 * @param fallheight
	 *            The height from which the player has fallen.
	 * @return A double value representing the height the player is to bounce.
	 */
	public abstract double calcBounceHeight(Player player, double fallheight);

	/**
	 * Calculates how far a player will sink in after landing on this node.
	 * 
	 * @param player
	 *            The player to calculate sink for.
	 * @param fallheight
	 *            The height from which they have fallen. 0 if the player walks
	 *            onto the node without jumping or falling.
	 * @return The distance for the player to sink. If grater than or equal to
	 *         1, the player will fall through the node fully.
	 */
	public abstract double calcSinkIn(Player player, double fallheight);

	/**
	 * Calculates the rate at which a player will rise out of this node if they
	 * have sunk in. If the player is in multiple node the lowermost node at the
	 * player's horizontal center is considered.
	 * 
	 * @param player
	 *            The player to perform the calculation for.
	 * @param sinkheight
	 *            The depth to which the player has sunk. 1 corresponds to the
	 *            player's's feet being at the bottom of the node, and 2 refers
	 *            to the player being fully submerged in multiple nodes.
	 * @return The rate at which the player should rise when they hold the key
	 *         corresponding to jumping, in nodes per second.
	 */
	public abstract double calcSinkEscape(Player player, double sinkheight);
/**
 * Calculates the speed at which a player will move across this node when walking.
 * @param player The player for which the walk rate is being calculated.
 * @return The rate in nodes/second at which the player should walk.
 */
	public abstract double calcWalkSpeed(Player player);
	/**
	 * Calculates the speed at which a player will move across this node when sprinting.
	 * @param player The player for which the sprint rate is being calculated.
	 * @return The rate in nodes/second at which the player should sprint.
	 */
	public abstract double calcSprintSpeed(Player player);
	/**
	 * Calculates the damage a player will take when falling onto this node.
	 * @param player The player falling onto this node.
	 * @param height The fall height.
	 * @return The damage to be given to the player. A player has a default health of 64 units.
	 */
	public abstract double calcFallDamage(Player player, double height);

}
