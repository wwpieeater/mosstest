package net.mosstest.scripting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class Position.
 */
public class Position {

	/** The x. */
	public final int x;
	
	/** The y. */
	public final int y;
	
	/** The z. */
	public final int z;
	
	/** The realm. */
	public final int realm;
	
	/** The is valid. */
	transient boolean isValid = true;

	/**
	 * Instantiates a new position.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param realm the realm
	 */
	public Position(int x, int y, int z, int realm) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.realm = realm;
		this.isValid = true;
	}

	/**
	 * Instantiates a new position.
	 *
	 * @param bytes the bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Position(byte[] bytes) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bis);
		this.x = dis.readInt();
		this.y = dis.readInt();
		this.z = dis.readInt();
		this.realm = dis.readInt();
		this.isValid = true;
	}



	/** The Constant serialVersionUID. */
	static final long serialVersionUID = 1128980133700001337L;

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
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
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
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.realm;
		result = prime * result + this.x;
		result = prime * result + this.y;
		result = prime * result + this.z;
		return result;
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
			dos.writeInt(this.x);
			dos.writeInt(this.y);
			dos.writeInt(this.z);
			dos.writeInt(this.realm);
			dos.flush();
			bos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.isValid = false;
		}
		return new byte[] {};
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return this.y;
	}


	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public int getZ() {
		return this.z;
	}


}
