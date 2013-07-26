package org.nodetest.servercore;

import java.nio.ByteBuffer;


public class MossItem {
	MossFile invTex;
	MossFile wieldTex;
	double invWeight;
	boolean mayStack;
	String displayName;
	/**
	 * Determines the dig time for the specified node with this tool, in milliseconds.
	 * @param nd The node
	 * @return
	 */
	int getDigTime(MapNode nd) {
		return 1000;
		//TODO
	}
}
