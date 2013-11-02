package net.mosstest.servercore;

import net.mosstest.scripting.MapNode;

public interface INodeManager {

	public abstract MapNode getNode(short nodeId);

	public abstract short putNode(MapNode node) throws MossWorldLoadException;

	public abstract void putNodeAlias(String alias, String dst);

	public abstract MapNode getNode(String string, boolean isModified);

	public abstract MapNode getNode(String string);

	public abstract void init();

}