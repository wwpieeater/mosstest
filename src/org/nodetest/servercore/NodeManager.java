package org.nodetest.servercore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NodeManager {
	private static HashMap<Integer, MapNode> definedNodes=new HashMap<>();
	
	static MapNode getNode(int nodeId){
		return definedNodes.get(new Integer(nodeId));
	}
	static void putNode(MapNode node) throws DuplicateNodedefException{
		if(!definedNodes.containsKey(new Integer(node.nodeId))){
			definedNodes.put(new Integer(node.nodeId), node);
		} else throw new DuplicateNodedefException();
	}
}
