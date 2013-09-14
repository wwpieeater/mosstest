package net.mosstest.servercore;

public class RenderMapChunk {
	RenderNode[][][] myNodes = new RenderNode[16][16][16];
	/**
	 * 
	 * @param holdThese must be length [16][16][16]
	 */
	public RenderMapChunk (RenderNode[][][] holdThese) {
		for(int i = 0; i < holdThese.length; i++) {
			for(int j = 0; j < holdThese[i].length; j++) {
				for(int k = 0; k < holdThese[i][j].length; k++) {
					myNodes[i][j][k] = holdThese[i][j][k];
				}
			}
		}
	}
}
