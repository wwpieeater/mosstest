package org.nodetest.servercore;

import com.jme3.math.Vector3f;

public class MossRenderPlayerMoveEvent extends MossRenderEvent {
	private Vector3f pos;
	
	public MossRenderPlayerMoveEvent (Vector3f vect) {
		pos = vect;
	}
	public Vector3f getPos () {
		return pos;
	}
}
