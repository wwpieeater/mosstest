package net.mosstest.servercore;

import net.mosstest.scripting.Player;

// TODO: Auto-generated Javadoc
/**
 * The Interface IPlayerManager.
 */
public interface IPlayerManager extends PlayerCommunicator {
	
	/**
	 * Gets the or create.
	 *
	 * @param name the name
	 * @return the or create
	 */
	public abstract Player getOrCreate(String name);

}
