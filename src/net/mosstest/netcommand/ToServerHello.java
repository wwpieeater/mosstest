package net.mosstest.netcommand;

import java.io.*;

/**
 * Created by hexafraction on 4/27/14.
 */
public class ToServerHello {
    private final String username;
    private final int protocolVersion;
    private final int minScriptApi;
    private final int maxScriptApi;

    public ToServerHello(byte[] buf) throws IOException,
            MalformedPacketException {

        // constructor from byte[] is parsing
        // Keep lines below for all of these tasks
        ByteArrayInputStream bs = new ByteArrayInputStream(buf);
        DataInputStream ds = new DataInputStream(bs);
        this.protocolVersion = ds.readUnsignedShort();
        this.minScriptApi = ds.readUnsignedShort();
        this.maxScriptApi = ds.readUnsignedShort();
        this.username = ds.readUTF();
    }

    public ToServerHello(String username, int protocolVersion, int minScriptApi, int maxScriptApi) {
        this.username = username;
        this.protocolVersion = protocolVersion;
        this.minScriptApi = minScriptApi;
        this.maxScriptApi = maxScriptApi;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeShort(this.protocolVersion);
        dos.writeShort(this.minScriptApi);
        dos.writeShort(this.maxScriptApi);
        dos.writeUTF(this.username);
        return bos.toByteArray();
    }
}