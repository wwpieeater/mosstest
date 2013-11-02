package net.mosstest.servercore;

import java.util.ArrayList;

import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;

import java.util.HashMap;
import java.util.Map.Entry;

import net.mosstest.scripting.MapNode;

import org.iq80.leveldb.DB;

import com.google.common.collect.HashBiMap;

public class LocalNodeManager extends AbstractNodeManager {
	private DB nodeDb;

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.INodeManager#getNode(short)
	 */
	@Override
	public MapNode getNode(short nodeId) {
		return this.definedNodes.get(nodeId);
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.INodeManager#putNode(net.mosstest.scripting.MapNode)
	 */
	@Override
	public short putNode(MapNode node) throws MossWorldLoadException {
		if (this.pending.containsValue(node.nodeName)) {
			node.setNodeId(this.pending.inverse().get(node.nodeName));
			this.definedNodes.set(this.pending.inverse().get(node.nodeName),
					node);
			this.defNodeByName.put(node.nodeName, node);
		} else {
			if (this.definedNodes.size() > 16384)
				throw new MossWorldLoadException("Too many nodedefs"); //$NON-NLS-1$

			node.setNodeId((short) this.definedNodes.size());

			this.definedNodes.add(node);
			this.defNodeByName.put(node.nodeName, node);
			this.nodeDb.put(new byte[] { (byte) (node.getNodeId() >>> 8),
					(byte) (node.getNodeId() & 0xFF) }, bytes(node.nodeName));
		}
		

		return node.getNodeId();
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.INodeManager#putNodeAlias(java.lang.String, java.lang.String)
	 */
	@Override
	public void putNodeAlias(String alias, String dst) {
		MapNode dstNode = this.defNodeByName.get(dst);
		this.defNodeByName.put(alias, dstNode);
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.INodeManager#getNode(java.lang.String, boolean)
	 */
	@Override
	public MapNode getNode(String string, boolean isModified) {
		MapNode r = this.defNodeByName.get(string);
		return (r==null)?AbstractNodeManager.MAPNODE_UNKNOWN:r;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.INodeManager#getNode(java.lang.String)
	 */
	@Override
	public MapNode getNode(String string) {

		MapNode r = this.defNodeByName.get(string);
		return (r==null)?AbstractNodeManager.MAPNODE_UNKNOWN:r;
	}

	/**
	 * 
	 */
	public LocalNodeManager(DB nodedb) {
		this.nodeDb = nodedb;
		this.init();
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.INodeManager#init()
	 */
	@Override
	public void init() {
		for (Entry<byte[], byte[]> entry : this.nodeDb) {
			short parsedId = (short) (entry.getKey()[0] * 256 + entry.getKey()[1]);
			String parsedString = asString(entry.getValue());
			this.pending.put(parsedId, parsedString);
		}
	}
}
