package net.mosstest.scripting;

/**
 * 
 * @deprecated Use net.mosstest.scripting.handlers.* instead.
 * 
 */
@Deprecated
public interface MossEventHandler {
	/**
	 * Process an event
	 * 
	 * @param myEvent
	 *            The event
	 * @return `true` to skip all future handlers for this event, false to run
	 *         the next handler.
	 * @throws MossScriptException
	 */

	boolean processEvent(MossEvent myEvent) throws MossScriptException;
}