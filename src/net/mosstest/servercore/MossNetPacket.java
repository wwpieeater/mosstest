package net.mosstest.servercore;

public class MossNetPacket {
	int commandId;
	byte[] payload;
	boolean needsFast;
	boolean needsAck;
	boolean isImportant;
	ServerSession sess;
	/**
	 * Constructs a packet, for either sending or from receiving.
	 * @param commandId The command ID
	 * @param payload The payload, as a byte array.
	 */
	public MossNetPacket(int commandId, byte[] payload) {
		this.commandId = commandId;
		this.payload = payload;
		this.needsAck = true;
		this.needsFast = false;
		this.isImportant = true;
	}
	public MossNetPacket(int commandId, byte[] payload, boolean needsFast,
			boolean needsAck, boolean isImportant) {
		this.commandId = commandId;
		this.payload = payload;
		this.needsFast = needsFast;
		this.needsAck = needsAck;
		this.isImportant = true;
	}

}
