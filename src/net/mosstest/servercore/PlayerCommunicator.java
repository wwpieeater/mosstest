package net.mosstest.servercore;

import net.mosstest.scripting.Player;

public interface PlayerCommunicator {

	void forceSetPosition(Player pl, int cx, int cy, int cz, double offsetx,
			double offsety, double offsetz) throws InterruptedException;
}
