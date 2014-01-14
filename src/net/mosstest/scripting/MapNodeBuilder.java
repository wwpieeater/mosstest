package net.mosstest.scripting;

import net.mosstest.scripting.MapNode.DrawType;

public class MapNodeBuilder {
	private String name;
	private String uiName;
	private INodeParams params;
	private int lightEmission = 0;
	private MossItem dropItem;
	private boolean isBuildableTo = true;
	private DrawType drawType = DrawType.DRAW_BLOCK;
	private float boxOriginX = 0, boxOriginY = 0, boxOriginZ = 0;
	
	private float lengthX = 0.5f, lengthY = 0.5f, lengthZ = 0.5f;
	public MapNodeBuilder(String name, String uiName) {
		this.name = name;
		this.uiName = uiName;
	}
	public MapNodeBuilder params(INodeParams params) {
		this.params = params;
		return this;
	}
	
}
