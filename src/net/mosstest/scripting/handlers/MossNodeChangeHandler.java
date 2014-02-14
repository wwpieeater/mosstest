package net.mosstest.scripting.handlers;

import net.mosstest.scripting.MapNode;
import net.mosstest.scripting.MossItem;
import net.mosstest.scripting.NodePosition;
import net.mosstest.scripting.Player;

public interface MossNodeChangeHandler {
	public enum NodeActionType {
		/**
		 * Fired when the user fully digs a node.
		 */
		NODE_ACTION_DIG,
		/**
		 * Fired when the user punches (left-clicks) a node
		 */
		NODE_ACTION_PUNCH,
		/**
		 * Fired when the user punches a node with a right-click. The default
		 * action is to place a node; onDig should return false to avoid this.
		 */
		NODE_ACTION_RIGHTPUNCH,
		/**
		 * Fired when the user punches a node with a middle-click.
		 */
		NODE_ACTION_MIDDLE_MOUSE_PUNCH,
		/**
		 * Fired when the user right-clicks the node while pressing shift. This
		 * is generally a sign of attempting to place a node as opposed to
		 * right-punching it.
		 */
		NODE_ACTION_PLACE,
		/**
		 * Currently unused.
		 */
		NODE_ACTION_REPLACE
	}

	/**
	 * Called when a node is acted upon.
	 * 
	 * @param pl
	 *            The player performing the action, null if none can be
	 *            determined.
	 * @param pos
	 *            The position of the node in question.
	 * @param before
	 *            The node existing before this action.
	 * @param wieldItem
	 *            The item wielded by the player, null for the hand. If this is
	 *            a node place event the item will correspond to the node being
	 *            placed.
	 * @param type
	 *            The type of action performed.
	 * @param timestamp
	 *            A game timestamp that is assigned to events in a consistent
	 *            order. This means that each event will have a differing
	 *            timestamp, and any event received by the server or at the
	 *            client after another will have a greater timestamp. This value
	 *            is <i>not</i> a real-time clock.
	 * @return True if this event should be processed by other subscribed and
	 *         registered handlers, false otherwise.
	 */
	public abstract boolean onAction(Player pl, NodePosition pos,
			MapNode before, MossItem wieldItem, NodeActionType type,
			long timestamp);

}
