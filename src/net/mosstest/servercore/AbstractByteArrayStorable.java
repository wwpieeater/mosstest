package net.mosstest.servercore;

import java.io.IOException;

public abstract class AbstractByteArrayStorable {

    public AbstractByteArrayStorable() {

    }

    public abstract byte[] toBytes();

    public AbstractByteArrayStorable(byte[] buf) throws IOException{
        this.loadBytes(buf);
    }

    public abstract void loadBytes(byte[] buf) throws IOException;

}
