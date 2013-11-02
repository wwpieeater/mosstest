package net.mosstest.servercore;

import java.util.ArrayList;
import java.util.HashMap;

import net.mosstest.scripting.MapNode;

import org.iq80.leveldb.DB;

import com.google.common.collect.HashBiMap;

public abstract class AbstractNodeManager implements INodeManager {
	protected ArrayList<MapNode> definedNodes = new ArrayList<>();
	protected HashMap<String, MapNode> defNodeByName = new HashMap<>();
	protected HashBiMap<Short, String> pending = HashBiMap.create();

	public static final MapNode MAPNODE_UNKNOWN = new MapNode("unknown.png",
			"sys:unknown", "An unknown piece of the world", 1);
	static {
		MAPNODE_UNKNOWN.setNodeId((short) -1);
	}
	
	{
		this.definedNodes.add(this.MAPNODE_UNKNOWN);
	}

	@Override
	public abstract MapNode getNode(short nodeId);

	@Override
	public abstract short putNode(MapNode node) throws MossWorldLoadException;

	@Override
	public abstract void putNodeAlias(String alias, String dst);

	@Override
	public abstract MapNode getNode(String string, boolean isModified);

	@Override
	public abstract MapNode getNode(String string);

	@Override
	public abstract void init();

}
