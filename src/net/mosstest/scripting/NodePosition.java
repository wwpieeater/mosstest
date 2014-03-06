package net.mosstest.scripting;

import java.io.*;
import java.util.Arrays;

// TODO: Auto-generated Javadoc

/**
 * The Class NodePosition.
 */
public class NodePosition {

    /**
     * The zl.
     */
    public final byte xl, yl, zl;

    /**
     * The chunk.
     */
    public final Position chunk;

    /**
     * Instantiates a new node position.
     *
     * @param x     the x
     * @param y     the y
     * @param z     the z
     * @param realm the realm
     * @param xl    the xl
     * @param yl    the yl
     * @param zl    the zl
     */
    public NodePosition(int x, int y, int z, int realm, byte xl, byte yl,
                        byte zl) {
        this.chunk = new Position(x, y, z, realm);
        this.xl = xl;
        this.yl = yl;
        this.zl = zl;
    }

    /**
     * Instantiates a new node position.
     *
     * @param bytes the bytes
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public NodePosition(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes, 16, bytes.length - 16);
        DataInputStream dis = new DataInputStream(bis);
        this.chunk = new Position(Arrays.copyOfRange(bytes, 0, 16));
        this.xl = dis.readByte();
        this.yl = dis.readByte();
        this.zl = dis.readByte();
        // this.isValid = true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.chunk.hashCode());
        result = prime * result + this.xl;
        result = prime * result + this.yl;
        result = prime * result + this.zl;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NodePosition)) {
            return false;
        }
        NodePosition other = (NodePosition) obj;
        if (!this.chunk.equals(other.chunk)) {
            return false;
        }
        if (this.xl != other.xl) {
            return false;
        }
        if (this.yl != other.yl) {
            return false;
        }
        return this.zl == other.zl;
    }


    /**
     * To bytes.
     *
     * @return the byte[]
     */
    public byte[] toBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            bos.write(this.chunk.toBytes());
            dos.writeByte(this.xl);
            dos.writeByte(this.yl);
            dos.writeByte(this.zl);
            dos.flush();
            bos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            // Auto-generated catch block

        }
        return new byte[]{};
    }

}
