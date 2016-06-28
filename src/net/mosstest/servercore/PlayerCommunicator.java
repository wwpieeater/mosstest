package net.mosstest.servercore;

import net.mosstest.scripting.Player;

// TODO: Auto-generated Javadoc
/**
 * The Interface PlayerCommunicator.
 */
public interface PlayerCommunicator {

	/**
	 * Force set position.
	 *
	 * @param pl the pl
	 * @param cx the cx
	 * @param cy the cy
	 * @param cz the cz
	 * @param offsetx the offsetx
	 * @param offsety the offsety
	 * @param offsetz the offsetz
	 * @throws InterruptedException the interrupted exception
	 */
	void forceSetPosition(Player pl, int cx, int cy, int cz, double offsetx,
			double offsety, double offsetz) throws InterruptedException;
}
