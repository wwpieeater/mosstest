package org.nodetest.servercore;

import java.io.IOException;


public class ClientManager {
	volatile static int state=0;
	static final int STATE_DISCONNECTED=0;
	static final int STATE_FETCHING_SCRIPTS=1;
	static final int STATE_FETCHING_TEXTURES=2;
	static final int STATE_FETCHING_MESHES=3;
	static final int STATE_PLAYABLE=4;
	static final int STATE_INVALID=-1;
	static ClientNetworkingManager net;
	static void init(String host, int port, String username, char[] password) throws IOException{
		net=new ClientNetworkingManager(host, port, EngineSettings.getBool("client_udp", true));
		net.beginConnectHandshake();
	}

	public static ClientNetworkingManager getNetworkingManager() {
		return net;
	}
}
