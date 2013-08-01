package net.mosstest.servercore;

public class MossNetPacket {
	int commandId;
	String payload;
	public MossNetPacket(int commandId, String payload) {
		this.commandId = commandId;
		this.payload = payload;
	}

}
