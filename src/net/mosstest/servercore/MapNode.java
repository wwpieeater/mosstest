package net.mosstest.servercore;

import net.mosstest.scripting.NodeParams;

public class MapNode {
	short nodeId = 0;
	public final NodeParams nodeparams;
	private final String[] textureSpace;
	public String nodeName;
	public String userFacingName;
	public boolean isLiquid;
	public int lightEmission;
	public MossItem dropItem;

	public MapNode(NodeParams nodeparams, String[] tex, String nodeName,
			String userFacingName, boolean isLiquid, int lightEmission) {
		this.nodeparams = nodeparams;
		this.textureSpace = tex;
		this.nodeName = nodeName;

		try {
			nodeId = NodeManager.putNode(this);
		} catch (MossWorldLoadException e) {

		}

		this.userFacingName = userFacingName;
		this.isLiquid = isLiquid;
		this.lightEmission = lightEmission;
	}

	public MapNode(String [] tex, String nodeName,
			String userFacingName, boolean isLiquid, int lightEmission) {
		this.nodeparams = getDefaultParams();
		this.textureSpace = tex;
		try {
			nodeId = NodeManager.putNode(this);
		} catch (MossWorldLoadException e) {

		}
		this.nodeName = nodeName;
		this.userFacingName = userFacingName;
		this.isLiquid = isLiquid;
		this.lightEmission = lightEmission;
	}

	private static NodeParams getDefaultParams() {
		return new NodeParams() {

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
			public void onDig(Player player, GenericTool tool,
					NodePosition target, Face punchedFace) {
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
		};
	}

}
