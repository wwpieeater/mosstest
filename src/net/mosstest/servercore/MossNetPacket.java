package net.mosstest.servercore;

// TODO: Auto-generated Javadoc

import java.util.Arrays;

public class MossNetPacket {

    public int commandId;

    public byte[] payload;

    public boolean needsFast;

    public boolean needsAck;

    public boolean isImportant;

    public ServerSession sess;

    /**
     * Constructs a packet, for either sending or from receiving.
     *
     * @param commandId The command ID
     * @param payload   The payload, as a byte array.
     */
    public MossNetPacket(int commandId, byte[] payload) {
        this.commandId = commandId;
        this.payload = Arrays.copyOf(payload, payload.length);
        this.needsAck = true;
        this.needsFast = false;
        this.isImportant = true;
    }

    /**
     * Instantiates a new moss net packet.
     *
     * @param commandId   the command id
     * @param payload     the payload
     * @param needsFast   the needs fast
     * @param needsAck    the needs ack
     * @param isImportant the is important
     */
    public MossNetPacket(int commandId, byte[] payload, boolean needsFast,
                         boolean needsAck, boolean isImportant) {
        this.commandId = commandId;
        this.payload = payload;
        this.needsFast = needsFast;
        this.needsAck = needsAck;
        this.isImportant = isImportant;
    }

}
