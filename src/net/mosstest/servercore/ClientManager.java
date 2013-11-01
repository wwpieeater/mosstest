package net.mosstest.servercore;

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

	public static ApplicationLevelNetworkingManager getApplicationLevelNetworkingManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Show an error screen on the client
	 * @param title The title to show
	 * @param msg The message to show
	 * @param disconnect Whether to disconnect from the current server, if any.
	 */
	public static void showErrorScreen(String title, String msg, boolean disconnect) {
		// TODO Auto-generated method stub
		
	}
}
