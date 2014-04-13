package net.mosstest.scripting.handlers;

import net.mosstest.scripting.MapNode;
import net.mosstest.scripting.MossItem;
import net.mosstest.scripting.NodePosition;
import net.mosstest.scripting.Player;
import net.mosstest.scripting.events.MossNodeChangeEvent;

// TODO: Auto-generated Javadoc
/**
 * The Interface MossNodeChangeHandler.
 */
public interface MossNodeChangeHandler extends MossEventHandler {


	/**
	 * Called when a node is acted upon.
	 * 
	 * @param evt A NodeChangeEvent object describing the event.
	 * @return True if this event should be processed by other subscribed and
	 *         registered handlers, false otherwise.
	 */
	public abstract boolean onAction(MossNodeChangeEvent evt);
	
}
