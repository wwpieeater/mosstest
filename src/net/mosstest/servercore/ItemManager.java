package net.mosstest.servercore;

import net.mosstest.scripting.MapNode;
import net.mosstest.scripting.MossItem;
import net.mosstest.scripting.MossItemBuilder;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: Auto-generated Javadoc

/**
 * The Class ItemManager.
 */
public class ItemManager {

    public static final int MAX_ITEMDEF = 32000;
    public static final int BYTE_CAST_MASK = 0xFF;

    private ArrayList<MossItem> defItems = new ArrayList<>();

    private HashMap<String, MossItem> defItemsByName = new HashMap<>();



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
    public MossItem getItem(short itemId) {
        return this.defItems.get(itemId);
    }

    /**
     * Put node.
     *
     * @param item the item
     * @return the short
     * @throws MossWorldLoadException the moss world load exception
     */
    public short putNode(MossItem item) throws MossWorldLoadException {

        if (this.defItems.size() > MAX_ITEMDEF)
                throw new MossWorldLoadException("Too many itemdefs"); //$NON-NLS-1$

            item.setItemId((short) this.defItems.size());

            this.defItems.add(item);
            this.defItemsByName.put(item.getTechnicalName(), item);


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

    public ItemManager() {

    }

    private HashMap<MapNode, MossItem> itemsForNode = new HashMap<>();


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
