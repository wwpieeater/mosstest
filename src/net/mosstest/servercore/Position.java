package net.mosstest.servercore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Position {

	int x;
	int y;
	int z;
	int realm;
	transient boolean isValid = true;

	public Position(int x, int y, int z, int realm) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.realm = realm;
		isValid = true;
	}

	public Position(byte[] bytes) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bis);
		x = dis.readInt();
		y = dis.readInt();
		z = dis.readInt();
		realm = dis.readInt();
		isValid = true;
	}

	public Position() {
		isValid = false;
	}

	static final long serialVersionUID = 1128980133700001337L;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		if (realm != other.realm) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		if (z != other.z) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + realm;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	public byte[] toBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(realm);
			dos.flush();
			bos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isValid = false;
		}
		return new byte[] {};
	}

}
