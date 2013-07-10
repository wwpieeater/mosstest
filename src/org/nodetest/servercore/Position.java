package org.nodetest.servercore;


public class Position {
	int x;
	int y;
	int z;
	int realm;

	public Position(int x, int y, int z, int realm) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public byte[] toBytes() {
		return new byte[] { (byte) (x >>> 24), (byte) (x >>> 16),
				(byte) (x >>> 8), (byte) x, (byte) (y >>> 24),
				(byte) (y >>> 16), (byte) (y >>> 8), (byte) y,
				(byte) (z >>> 24), (byte) (z >>> 16), (byte) (z >>> 8),
				(byte) z, (byte) (realm >>> 24), (byte) (realm >>> 16),
				(byte) (realm >>> 8), (byte) realm, };

	}

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

}
