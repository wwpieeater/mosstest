package net.mosstest.servercore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NodePosition {
	public final byte xl, yl, zl;
	public final Position chunk;

	public NodePosition(int x, int y, int z, int realm, byte xl, byte yl,
			byte zl) {
		chunk = new Position(x, y, z, realm);
		this.xl = xl;
		this.yl = yl;
		this.zl = zl;
	}

	public NodePosition(byte[] bytes) throws IOException {
		super();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bis);
		this.chunk = new Position(dis.readInt(), dis.readInt(), dis.readInt(),
				dis.readInt());
		this.xl = dis.readByte();
		this.yl = dis.readByte();
		this.zl = dis.readByte();
		// this.isValid = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chunk == null) ? 0 : chunk.hashCode());
		result = prime * result + xl;
		result = prime * result + yl;
		result = prime * result + zl;
		return result;
	}

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
		if (chunk == null) {
			if (other.chunk != null) {
				return false;
			}
		} else if (!chunk.equals(other.chunk)) {
			return false;
		}
		if (xl != other.xl) {
			return false;
		}
		if (yl != other.yl) {
			return false;
		}
		if (zl != other.zl) {
			return false;
		}
		return true;
	}

	

	public byte[] toBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			bos.write(chunk.toBytes());
			dos.writeByte(this.xl);
			dos.writeByte(this.yl);
			dos.writeByte(this.zl);
			dos.flush();
			bos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			// Auto-generated catch block
			
		}
		return new byte[] {};
	}

}
