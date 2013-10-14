package net.mosstest.servercore;

import java.util.ArrayList;
import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import java.util.HashMap;
import java.util.Map.Entry;

import org.iq80.leveldb.DB;

import com.google.common.collect.HashBiMap;

public class NodeManager {
	private ArrayList<MapNode> definedNodes = new ArrayList<>();
	private HashMap<String, MapNode> defNodeByName = new HashMap<>();
	private HashBiMap<Short, String> pending = HashBiMap.create();
	private DB nodeDb;

	public MapNode getNode(short nodeId) {
		return this.definedNodes.get(nodeId);
	}

	public short putNode(MapNode node) throws MossWorldLoadException {
		if (this.pending.containsValue(node.nodeName)) {
			node.setNodeId(this.pending.inverse().get(node.nodeName));
			this.definedNodes.set(this.pending.inverse().get(node.nodeName),
					node);
		} else {
			if (this.definedNodes.size() > 16384)
				throw new MossWorldLoadException("Too many nodedefs"); //$NON-NLS-1$

			node.setNodeId((short) this.definedNodes.size());

			this.definedNodes.add(node);
			this.nodeDb.put(new byte[] { (byte) (node.getNodeId() >>> 8),
					(byte) (node.getNodeId() & 0xFF) }, bytes(node.nodeName));
		}
		this.defNodeByName.put(node.nodeName, node);

		return node.getNodeId();
	}

	public void putNodeAlias(String alias, String dst) {
		MapNode dstNode = this.defNodeByName.get(dst);
		this.defNodeByName.put(alias, dstNode);
	}

	public MapNode getNode(String string, boolean isModified) {
		return this.defNodeByName.get(string);
	}

	public MapNode getNode(String string) {
		return this.defNodeByName.get(string);
	}

	/**
	 * 
	 */
	public NodeManager(DB nodedb) {
		this.nodeDb = nodedb;
		for (Entry<byte[], byte[]> entry : nodedb) {
			short parsedId = (short) (entry.getKey()[0] * 256 + entry.getKey()[1]);
			String parsedString = asString(entry.getValue());
			this.pending.put(parsedId, parsedString);
		}
	}
}
