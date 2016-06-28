package net.mosstest.netcommand;

import java.io.*;

/**
 * Created by hexafraction on 5/3/14.
 */
public class ToClientAuthDenied extends ToClientCommand {
    public enum DenyReason {
        REASON_UNKNWN,
        REASON_BAD_PASS,
        REASON_BANNED,
        REASON_PLAYER_LIMIT,
        REASON_LOGON_HOUR,
        REASON_NO_NEW_PLAYERS,
        REASON_VERSION_MISMATCH,
        REASON_AUTH_TIMED_OUT,
        REASON_SERVER_MAINT,
        REASON_FAILED_CONNECTION
    }

    public DenyReason getReason() {
        return reason;
    }

    private final DenyReason reason;

    public ToClientAuthDenied(DenyReason reason) {
        this.reason = reason;
    }

    public ToClientAuthDenied(byte[] buf) throws IOException {
        ByteArrayInputStream bs = new ByteArrayInputStream(buf);
        DataInputStream ds = new DataInputStream(bs);
        int reason_ = ds.readUnsignedByte();
        switch(reason_){
            case 0x01:
                this.reason = DenyReason.REASON_BAD_PASS;
                break;
            case 0x02:
                this.reason = DenyReason.REASON_BANNED;
                break;
            case 0x03:
                this.reason = DenyReason.REASON_PLAYER_LIMIT;
                break;
            case 0x04:
                this.reason = DenyReason.REASON_LOGON_HOUR;
                break;
            case 0x05:
                this.reason = DenyReason.REASON_NO_NEW_PLAYERS;
                break;
            case 0x06:
                this.reason = DenyReason.REASON_PLAYER_LIMIT;
                break;
            case 0x07:
                this.reason = DenyReason.REASON_VERSION_MISMATCH;
                break;
            case 0x08:
                this.reason = DenyReason.REASON_AUTH_TIMED_OUT;
                break;
            case 0x09:
                this.reason = DenyReason.REASON_SERVER_MAINT;
                break;
            case 0x0a:
                this.reason = DenyReason.REASON_FAILED_CONNECTION;
                break;
            // fall through for unknwn
            case 0x00:
            default:
                this.reason = DenyReason.REASON_UNKNWN;

        }
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        switch (this.reason) {

            case REASON_UNKNWN:
                dos.writeByte(0x00);
                break;
            case REASON_BAD_PASS:
                dos.writeByte(0x01);
                break;
            case REASON_BANNED:
                dos.writeByte(0x02);
                break;
            case REASON_PLAYER_LIMIT:
                dos.writeByte(0x03);
                break;
            case REASON_LOGON_HOUR:
                dos.writeByte(0x04);
                break;
            case REASON_NO_NEW_PLAYERS:
                dos.writeByte(0x06);
                break;
            case REASON_VERSION_MISMATCH:
                dos.writeByte(0x07);
                break;
            case REASON_AUTH_TIMED_OUT:
                dos.writeByte(0x08);
                break;
            case REASON_SERVER_MAINT:
                dos.writeByte(0x09);
                break;
            case REASON_FAILED_CONNECTION:
                dos.writeByte(0x0a);
                break;
        }
        return bos.toByteArray();
    }
}
