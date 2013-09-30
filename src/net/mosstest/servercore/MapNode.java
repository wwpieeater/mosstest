package net.mosstest.servercore;

import net.mosstest.scripting.EventProcessingCompletedSignal;
import net.mosstest.scripting.NodeParams;

public class MapNode {
	private short nodeId = 0;
	public final NodeParams nodeparams;
	private final String[] textureNames;
	private final MossTexture[] tex;
	public String nodeName;
	public String userFacingName;
	public boolean isLiquid;
	public int lightEmission;
	public MossItem dropItem;
	public boolean isBuildableTo=true;
	public MapNode(NodeParams nodeparams, String[] texs, String nodeName,
			String userFacingName, boolean isLiquid, int lightEmission) {
		this.nodeparams = nodeparams;
		this.textureNames = texs;
		this.nodeName = nodeName;

		try {
			setNodeId(NodeManager.putNode(this));
		} catch (MossWorldLoadException e) {
			// pass
		}

		this.userFacingName = userFacingName;
		this.isLiquid = isLiquid;
		this.lightEmission = lightEmission;
		tex = new MossTexture[texs.length];
		for (int i = 0; i < tex.length; i++) {
			tex[i] = MossTextureManager.get(texs[i]);
		}
	}

	public MapNode(String[] texs, String nodeName, String userFacingName,
			boolean isLiquid, int lightEmission) {
		this.nodeparams = new DefaultNodeParams();
		this.textureNames = texs;
		try {
			setNodeId(NodeManager.putNode(this));
		} catch (MossWorldLoadException e) {
			// pass
		}
		this.nodeName = nodeName;
		this.userFacingName = userFacingName;
		this.isLiquid = isLiquid;
		this.lightEmission = lightEmission;
		tex = new MossTexture[texs.length];
		for (int i = 0; i < tex.length; i++) {
			tex[i] = MossTextureManager.get(texs[i]);
		}
	}

	public static class AirNodeParams implements NodeParams{

		@Override
		public void onPunch(Player player, GenericTool tool,
				NodePosition target, Face punchedFace)
				throws EventProcessingCompletedSignal {
			//pass
			
		}

		@Override
		public void onDig(Player player, GenericTool tool, NodePosition target,
				Face punchedFace) throws EventProcessingCompletedSignal {
			// pass
			
		}

		@Override
		public void onPlaceNextTo(Player player, NodePosition target,
				NodePosition placed) throws EventProcessingCompletedSignal {
			// pass
			
		}

		@Override
		public void onRightClick(Player player, GenericTool tool,
				NodePosition target, Face clickedFace)
				throws EventProcessingCompletedSignal {
			// pass
			
		}

		@Override
		public boolean onStepOn(Player player, NodePosition pos)
				throws EventProcessingCompletedSignal {
			// pass
			return false;
		}

		@Override
		public double jumpOffHeight(Player player) {
			return 0;
		}

		@Override
		public double calcBounceHeight(Player player, double fallheight) {
			return 0;
		}

		@Override
		public double calcSinkIn(Player player, double fallheight) {
			return 1;
		}

		@Override
		public double calcSinkEscape(Player player, double sinkheight) {
			return 1;
		}

		@Override
		public double calcWalkSpeed(Player player) {
			return 1;
		}

		@Override
		public double calcSprintSpeed(Player player) {
			return 4;
		}

		@Override
		public double calcFallDamage(Player player, double height) {
			return 0;
		}

		@Override
		public boolean shouldContinueFall(Player player, double height) {
			return true;
		}
		
	}
	
	public static class DefaultNodeParams implements NodeParams {

		@Override
		public boolean onStepOn(Player player, NodePosition pos) {
			return true;
		}

		@Override
		public void onRightClick(Player player, GenericTool tool,
				NodePosition target, Face clickedFace) {
			return;

		}

		@Override
		public void onPunch(Player player, GenericTool tool,
				NodePosition target, Face punchedFace) {
			return;

		}

		@Override
		public void onPlaceNextTo(Player player, NodePosition target,
				NodePosition placed) {
			return;
		}

		@Override
		public void onDig(Player player, GenericTool tool, NodePosition target,
				Face punchedFace) {
			return;
		}

		@Override
		public double jumpOffHeight(Player player) {
			return 1.125;
		}

		@Override
		public double calcWalkSpeed(Player player) {
			return 1;
		}

		@Override
		public double calcSprintSpeed(Player player) {
			return 4;
		}

		@Override
		public double calcSinkIn(Player player, double fallheight) {
			return 0;
		}

		@Override
		public double calcSinkEscape(Player player, double fallheight) {
			return 1;
		}

		@Override
		public double calcBounceHeight(Player player, double fallheight) {
			// TODO Auto-generated method stub
			return 0.125;
		}

		@Override
		public double calcFallDamage(Player player, double height) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean shouldContinueFall(Player player, double height) {
			return false;
		}
	};

	public short getNodeId() {
		return this.nodeId;
	}

	void setNodeId(short nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.nodeId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MapNode)) {
			return false;
		}
		MapNode other = (MapNode) obj;
		if (this.nodeId != other.nodeId) {
			return false;
		}
		return true;
	}

}
