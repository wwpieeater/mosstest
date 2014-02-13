package net.mosstest.tests;

import net.mosstest.scripting.AirNodeParams;
import net.mosstest.scripting.DefaultNodeParams;
import net.mosstest.scripting.MapNode;
import net.mosstest.servercore.INodeManager;
import net.mosstest.servercore.MossWorldLoadException;

public class MockNodeManager implements INodeManager {
	private static final MapNode MOCK_SOLID_MAPNODE = new MapNode(
			new DefaultNodeParams(), "mock", "!mock:mock", "Mock node", 1);

	@Override
	public MapNode getNode(short nodeId) {
		return MOCK_SOLID_MAPNODE;
	}

	@Override
	public short putNode(MapNode node) throws MossWorldLoadException {
		// TODO Auto-generated method stub
		return 1337;
	}

	@Override
	public void putNodeAlias(String alias, String dst) {
	}

	@Override
	public MapNode getNode(String string, boolean isModified) {
		return MOCK_SOLID_MAPNODE;
	}

	@Override
	public MapNode getNode(String string) {
		return MOCK_SOLID_MAPNODE;
	}

	@Override
	public void init() {
	}

}
