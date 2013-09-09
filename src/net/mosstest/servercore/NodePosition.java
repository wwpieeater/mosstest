package net.mosstest.servercore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NodePosition extends Position {
	byte xl, yl, zl;

	public NodePosition(int x, int y, int z, int realm, byte xl, byte yl,
			byte zl) {
		super(x, y, z, realm);
		this.xl = xl;
		this.yl = yl;
		this.zl = zl;
	}

	public NodePosition(byte[] bytes) throws IOException {
		super();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bis);
		this.x = dis.readInt();
		this.y = dis.readInt();
		this.z = dis.readInt();
		this.realm = dis.readInt();
		this.xl = dis.readByte();
		this.yl = dis.readByte();
		this.zl = dis.readByte();
		this.isValid = true;
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
		
		if (this.realm != other.realm) {
			return false;
		}
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		if (this.z != other.z) {
			return false;
		}
		if (this.xl != other.xl) {
			return false;
		}
		if (this.yl != other.yl) {
			return false;
		}
		if (this.zl != other.zl) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Integer.reverseBytes(this.x) ^ Integer.reverseBytes(this.y)
				^ Integer.reverseBytes(this.z);
		// Needs to be better

	}

	public byte[] toBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(this.x);
			dos.writeInt(this.y);
			dos.writeInt(this.z);
			dos.writeInt(this.realm);
			dos.writeByte(this.xl);
			dos.writeByte(this.yl);
			dos.writeByte(this.zl);
			dos.flush();
			bos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.isValid = false;
		}
		return new byte[] {};
	}

}
