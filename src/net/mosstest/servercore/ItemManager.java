package net.mosstest.servercore;

import java.util.ArrayList;

import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;

import java.util.HashMap;
import java.util.Map.Entry;

import net.mosstest.scripting.MapNode;
import net.mosstest.scripting.MossItem;
import net.mosstest.scripting.MossItem;

import org.iq80.leveldb.DB;

import com.google.common.collect.HashBiMap;

public class ItemManager {
	private ArrayList<MossItem> defItems = new ArrayList<>();
	private HashMap<String, MossItem> defItemsByName = new HashMap<>();
	private HashBiMap<Short, String> pending = HashBiMap.create();
	private DB itemDb;

	private final MossItem unknownFallbackItem = new MossItem(
			"item_unknown.png", "sys_hand.png", 1, true,
			"An unidentifiable item", "sys:unknown");
	{
		this.unknownFallbackItem.setItemId((short) -1);
		defItems.add(this.unknownFallbackItem);
		itemsForNode.put(NodeManager.getUnknownFallbackNode(),
				this.unknownFallbackItem);
	}

	public MossItem getNode(short nodeId) {
		return this.defItems.get(nodeId);
	}

	public short putNode(MossItem item) throws MossWorldLoadException {
		if (this.pending.containsValue(item.getTechnicalName())) {
			item.setItemId(this.pending.inverse().get(item.getTechnicalName()));
			this.defItems.set(
					this.pending.inverse().get(item.getTechnicalName()), item);
			this.defItemsByName.put(item.getTechnicalName(), item);
		} else {
			if (this.defItems.size() > 32000)
				throw new MossWorldLoadException("Too many itemdefs"); //$NON-NLS-1$

			item.setItemId((short) this.defItems.size());

			this.defItems.add(item);
			this.defItemsByName.put(item.getTechnicalName(), item);
			this.itemDb.put(new byte[] { (byte) (item.getItemId() >>> 8),
					(byte) (item.getItemId() & 0xFF) },
					bytes(item.getTechnicalName()));
		}

		return item.getItemId();
	}

	public void putNodeAlias(String alias, String dst) {
		MossItem dstItem = this.defItemsByName.get(dst);
		this.defItemsByName.put(alias, dstItem);
	}

	public MossItem getItem(String string, boolean isModified) {
		MossItem r = this.defItemsByName.get(string);
		return r == null ? this.unknownFallbackItem : r;
	}

	public MossItem getItem(String string) {

		MossItem r = this.defItemsByName.get(string);
		return r == null ? this.unknownFallbackItem : r;
	}

	/**
	 * 
	 */
	public ItemManager(DB itemdb) {
		this.itemDb = itemdb;
		for (Entry<byte[], byte[]> entry : itemdb) {
			short parsedId = (short) (entry.getKey()[0] * 256 + entry.getKey()[1]);
			String parsedString = asString(entry.getValue());
			this.pending.put(parsedId, parsedString);
		}
	}

	private static HashMap<MapNode, MossItem> itemsForNode = new HashMap<>();

	public static MossItem getForNode(MapNode nd) {
		MossItem item = itemsForNode.get(nd);
		if (item == null) {
			// item = createForNode(MossItem); //FIXME
			itemsForNode.put(nd, item);
			return item;
		} else
			return item;
	}

	/*
	 * private static MossItem createForNode(MossItem MossItem) { MossItem
	 * mi=new MossItem(MossItem.texture, MossItem, 1, true,
	 * MossItem.userFacingName, MossItem.nodeName) return mi; //TODO todo }
	 */
	// FIXME above
}
