package org.nodetest.servercore;

import java.util.HashMap;
import java.util.TreeMap;

public class NodeManager {
	private static TreeMap<Integer, MapNode> definedNodes=new TreeMap<>();
	private static HashMap<String, Integer> defNodeByName=new HashMap<>();
	static MapNode getNode(int nodeId){
		return definedNodes.get(new Integer(nodeId));
	}
	static void putNode(MapNode node){
		if(!definedNodes.containsKey(new Integer(node.nodeId))){
			definedNodes.put(new Integer(node.nodeId), node);
			defNodeByName.put(node.nodeName, new Integer(node.nodeId));
		} else ExceptionHandler.registerException(new DuplicateNodedefException());
	}
	public static int getNode(String string, boolean isModified) {
		return defNodeByName.get(string)+isModified;
	}
}
