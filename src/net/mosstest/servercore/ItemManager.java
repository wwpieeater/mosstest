package net.mosstest.servercore;

import java.util.HashMap;

import net.mosstest.scripting.MapNode;
import net.mosstest.scripting.MossItem;

public class ItemManager {

	private static HashMap<MapNode, MossItem> itemsForNode = new HashMap<>();

	public static MossItem getForNode(MapNode mapNode) {
		MossItem item = itemsForNode.get(mapNode);
		if (item == null) {
			//item = createForNode(mapNode); //FIXME
			itemsForNode.put(mapNode, item);
			return item;
		} else
			return item;
	}

	/*private static MossItem createForNode(MapNode mapNode) {
		MossItem mi=new MossItem(mapNode.texture, mapNode, 1, true, mapNode.userFacingName, mapNode.nodeName)
		return mi;
		//TODO todo
	}
*/
	//FIXME above
}
