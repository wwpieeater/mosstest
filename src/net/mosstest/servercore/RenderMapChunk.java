package net.mosstest.servercore;

public class RenderMapChunk {
	private RenderNode[][][] nodes = new RenderNode[16][16][16];
	
	public RenderMapChunk (RenderNode[][][] inputNodes) {
		for(int i = 0; i < inputNodes.length; i++) {
			for(int j = 0; j < inputNodes[i].length; j++) {
				for(int k = 0; k < inputNodes[i][j].length; k++) {
					nodes[i][j][k] = inputNodes[i][j][k];
				}
			}
		}
	}
	
	public RenderNode getNodeByLoc(int i, int j, int k) {
		return nodes[i][j][k];
	}
	
	
}
