package net.mosstest.servercore;

import java.util.Arrays;

import com.jme3.scene.shape.Box;

public class RenderMapChunk {
	Box[][][] refs = new Box[16][16][16];
	public RenderMapChunk (Box[][][] holdThese) {
		refs = Arrays.copyOf(holdThese, holdThese.length);
	}
}
