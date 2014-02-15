package net.mosstest.scripting.events;

import net.mosstest.servercore.MosstestSecurityManager;


/**
 * Empty class, but has a security-oriented constructor.
 *
 */
public class ThreadStopEvent implements IMossEvent {
	
	/**
	 * Instantiates a new thread stop event.
	 *
	 * @throws SecurityException the security exception
	 */
	public ThreadStopEvent() throws SecurityException {
		MosstestSecurityManager.instance.checkMosstestControl();
	}
}
