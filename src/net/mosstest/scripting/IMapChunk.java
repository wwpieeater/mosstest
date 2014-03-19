package net.mosstest.scripting;

public interface IMapChunk {
    int CHUNK_DIMENSION = 16;

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    int getNodeId(byte x, byte y, byte z);

    void setNode(byte x, byte y, byte z, short node);

    int[][][] getNodes();

    byte[] writeLight(boolean compressed);

    void compact();
}
