package net.mosstest.servercore;

import com.google.common.collect.HashBiMap;
import net.mosstest.scripting.MapNode;
import net.mosstest.scripting.MossItem;
import net.mosstest.scripting.MossItemBuilder;
import org.iq80.leveldb.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;

// TODO: Auto-generated Javadoc

/**
 * The Class ItemManager.
 */
public class ItemManager {

    public static final int MAX_ITEMDEF = 32000;
    public static final int BYTE_CAST_MASK = 0xFF;
    /**
     * The def items.
     */
    private ArrayList<MossItem> defItems = new ArrayList<>();

    /**
     * The def items by name.
     */
    private HashMap<String, MossItem> defItemsByName = new HashMap<>();

    /**
     * The pending.
     */
    private HashBiMap<Short, String> pending = HashBiMap.create();

    /**
     * The item db.
     */
    private DB itemDb;

    /**
     * The unknown fallback item.
     */
    private final MossItem unknownFallbackItem = new MossItemBuilder().setInvTex("item_unknown.png").setWieldTex("sys_hand.png").setInvWeight(1).setMayStack(true).setDisplayName("An unidentifiable item").setTechnicalName("sys:unknown").createMossItem();

    /**
     * Gets the node.
     *
     * @param nodeId the node id
     * @return the node
     */
    public MossItem getNode(short nodeId) {
        return this.defItems.get(nodeId);
    }

    /**
     * Put node.
     *
     * @param item the item
     * @return the short
     * @throws MossWorldLoadException the moss world load exception
     */
    public short putNode(MossItem item) throws MossWorldLoadException {
        if (this.pending.containsValue(item.getTechnicalName())) {
            item.setItemId(this.pending.inverse().get(item.getTechnicalName()));
            this.defItems.set(
                    this.pending.inverse().get(item.getTechnicalName()), item);
            this.defItemsByName.put(item.getTechnicalName(), item);
        } else {
            if (this.defItems.size() > MAX_ITEMDEF)
                throw new MossWorldLoadException("Too many itemdefs"); //$NON-NLS-1$

            item.setItemId((short) this.defItems.size());

            this.defItems.add(item);
            this.defItemsByName.put(item.getTechnicalName(), item);
            this.itemDb.put(new byte[]{(byte) (item.getItemId() >>> 8),
                    (byte) (item.getItemId() & BYTE_CAST_MASK)},
                    bytes(item.getTechnicalName()));
        }

        return item.getItemId();
    }

    /**
     * Put node alias.
     *
     * @param alias the alias
     * @param dst   the dst
     */
    public void putNodeAlias(String alias, String dst) {
        MossItem dstItem = this.defItemsByName.get(dst);
        this.defItemsByName.put(alias, dstItem);
    }

    /**
     * Gets the item.
     *
     * @param string     the string
     * @param isModified the is modified
     * @return the item
     */
    public MossItem getItem(String string, boolean isModified) {
        MossItem r = this.defItemsByName.get(string);
        return r == null ? this.unknownFallbackItem : r;
    }

    /**
     * Gets the item.
     *
     * @param string the string
     * @return the item
     */
    public MossItem getItem(String string) {

        MossItem r = this.defItemsByName.get(string);
        return r == null ? this.unknownFallbackItem : r;
    }

    /**
     * Instantiates a new item manager.
     *
     * @param itemdb the itemdb
     */
    public ItemManager(DB itemdb) {
        this.itemDb = itemdb;
        for (Entry<byte[], byte[]> entry : itemdb) {
            short parsedId = (short) (entry.getKey()[0] * 256 + entry.getKey()[1]);
            String parsedString = asString(entry.getValue());
            this.pending.put(parsedId, parsedString);
        }
    }

    /**
     * The items for node.
     */
    private HashMap<MapNode, MossItem> itemsForNode = new HashMap<>();

    /**
     * Gets the for node.
     *
     * @param nd the nd
     * @return the for node
     */
    public MossItem getForNode(MapNode nd) {
        MossItem item = itemsForNode.get(nd);
        if (item == null) {
            // item = createForNode(MossItem); //FIXME
            itemsForNode.put(nd, item);
            return item;
        } else
            return item;
    }

    {
        this.unknownFallbackItem.setItemId((short) -1);
        defItems.add(this.unknownFallbackItem);
        itemsForNode.put(NodeManager.getUnknownFallbackNode(),
                this.unknownFallbackItem);
    }

	/*
     * private static MossItem createForNode(MossItem MossItem) { MossItem
	 * mi=new MossItem(MossItem.texture, MossItem, 1, true,
	 * MossItem.userFacingName, MossItem.nodeName) return mi; //TODO todo }
	 */
    // FIXME above
}
