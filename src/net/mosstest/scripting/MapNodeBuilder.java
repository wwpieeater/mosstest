package net.mosstest.scripting;

import net.mosstest.scripting.MapNode.DrawType;

// TODO: Auto-generated Javadoc
/**
 * The Class MapNodeBuilder.
 */
public class MapNodeBuilder {
	
	/** The name. */
	private String name;
	
	/** The ui name. */
	private String uiName;
	
	/** The params. */
	private INodeParams params;
	
	/** The light emission. */
	private int lightEmission = 0;
	
	/** The drop item. */
	private MossItem dropItem;
	
	/** The is buildable to. */
	private boolean isBuildableTo = true;
	
	/** The draw type. */
	private DrawType drawType = DrawType.DRAW_BLOCK;
	
	/** The box origin z. */
	private float boxOriginX = 0, boxOriginY = 0, boxOriginZ = 0;
	
	/** The length z. */
	private float lengthX = 0.5f, lengthY = 0.5f, lengthZ = 0.5f;
	
	/**
	 * Instantiates a new map node builder.
	 *
	 * @param name the name
	 * @param uiName the ui name
	 */
	public MapNodeBuilder(String name, String uiName) {
		this.name = name;
		this.uiName = uiName;
	}
	
	/**
	 * Params.
	 *
	 * @param params the params
	 * @return the map node builder
	 */
	public MapNodeBuilder params(INodeParams params) {
		this.params = params;
		return this;
	}
	
}
