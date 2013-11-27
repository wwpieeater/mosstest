package net.mosstest.servercore;

import net.mosstest.scripting.Player;

public interface IPlayerManager extends PlayerCommunicator {
	public abstract Player getOrCreate(String name);

}
