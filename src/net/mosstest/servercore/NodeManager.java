package net.mosstest.servercore;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeManager {
	private static ArrayList<MapNode> definedNodes = new ArrayList<>();
	private static HashMap<String, MapNode> defNodeByName = new HashMap<>();

	static MapNode getNode(short nodeId) {
		return definedNodes.get(nodeId);
	}

	static short putNode(MapNode node) throws MossWorldLoadException {
		if (definedNodes.size() > 16384)
			throw new MossWorldLoadException("Too many nodedefs"); //$NON-NLS-1$
		if (!definedNodes.contains(node)) {
			node.setNodeId((short) definedNodes.size());
			definedNodes.add(node);
			
			defNodeByName.put(node.nodeName, node);
		} else
			ExceptionHandler.registerException(new DuplicateNodedefException());
		return node.getNodeId();
	}
	public static void putNodeAlias(String alias, String dst) {
		MapNode dstNode=defNodeByName.get(dst);
		defNodeByName.put(alias, dstNode);
	}
	public static MapNode getNode(String string, boolean isModified) {
		return defNodeByName.get(string);
	}

	public static MapNode getNode(String string) {
		return defNodeByName.get(string);
	}

}
