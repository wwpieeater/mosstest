package net.mosstest.scripting;

import net.mosstest.servercore.MossEvent;

public interface MossEventHandler {
	/*
	 * void processEvent(Entity actor, long posx, long posy, long posz,
	 * MapNode nodeBefore, MapNode nodeAfter, Entity recvEntity,
	 * MossFormspec fspec, MossInventoryAction action, String
	 * initiatingMessage) throws NullPointerException;
	 */
	void processEvent(MossEvent myEvent) throws EventProcessingCompletedSignal, MossScriptException;
}