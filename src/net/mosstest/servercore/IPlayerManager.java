package net.mosstest.servercore;

import net.mosstest.scripting.Player;

public interface IPlayerManager {
	public abstract Player getOrCreate(String name);
	

}
