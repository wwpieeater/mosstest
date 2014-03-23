package net.mosstest.servercore;

import java.io.IOException;

public abstract class AbstractByteArrayStorable<M> {



    public AbstractByteArrayStorable() {

    }

    public abstract byte[] toBytes();

    public AbstractByteArrayStorable(byte[] buf) throws IOException{
        this.loadBytes(buf);
    }

    public AbstractByteArrayStorable(byte[] buf, M manager) throws IOException{
        this.setManager(manager);
        this.loadBytes(buf);
    }

    protected abstract void setManager(M manager);


    public abstract void loadBytes(byte[] buf) throws IOException;

}
