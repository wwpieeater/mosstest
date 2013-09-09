package net.mosstest.servercore;

public class ApplicationLevelNetworkingManager {
	ClientNetworkingManager net;
	static final int PROTOCOL_VERSION_MAJOR = 1;
	static final int PROTOCOL_VERSION_MINOR = 1; // change for
													// backward-compatible
													// changes such as changes
													// in timing that do not
													// break older versions

	public ApplicationLevelNetworkingManager(ClientNetworkingManager net) {
		this.net = net;
	}

	public void sendChunkRequest(Position pos) {
		// TODO create generated code

	}
}
