package net.mosstest.scripting;

import net.mosstest.servercore.AbstractByteArrayStorable;

import java.io.IOException;

public abstract class AbstractMapChunk extends AbstractByteArrayStorable<Void> {
    int CHUNK_DIMENSION = 16;

    public AbstractMapChunk(byte[] buf) throws IOException{
        super(buf);
    }

    protected AbstractMapChunk() {
        super();
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    public abstract int getNodeId(byte x, byte y, byte z);

    public abstract void setNode(byte x, byte y, byte z, short node);

    public abstract int[][][] getNodes();

    public abstract byte[] writeLight(boolean compressed);

    public abstract void compact();
}
