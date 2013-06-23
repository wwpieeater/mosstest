package org.nodetest.utilities;

public class VorPoint {
	public VorPoint(long x, long y) {
		this.x = x;
		this.y = y;
	}

	long x, y;
	VorPoint p1, p2, p3;
	/**
	 * 3x2 boolean array. First dimension is p1, p2, p3(outputs) Second
	 * dimension is l and r;
	 */
	boolean[][] directionsTaken = new boolean[3][2];
	byte[][] outputs = new byte[3][2];
	double theta1, theta2, theta3;

	public void register(VorPoint ptIn) {
		if (this.p1 == null) {
			p1 = ptIn;
			return;
		}
		if (this.p2 == null) {
			p2 = ptIn;
			return;
		}
		if (this.p3 == null) {
			p3 = ptIn;
			return;
		}
		throw new OutOfMemoryError("Invalid point mapping");
	}
	byte lastInput=-1;
	public void finalizeRegistration() {
		//if(p1==null||p2==null||p3==null) throw new NullPointerException();
		theta1 = Math.atan2(p1.y - this.y, p1.x - this.x);
		theta2 = Math.atan2(p2.y - this.y, p2.x - this.x);
		theta3 = Math.atan2(p3.y - this.y, p3.x - this.x);
		outputs[0][0] = (byte) (((theta2 - (180 + theta1) % 360) > (theta3 - (180 + theta1) % 360)) ? 1
				: 2);
		outputs[0][1] = (byte) (((theta2 - (180 + theta1) % 360) < (theta3 - (180 + theta1) % 360)) ? 1
				: 2);
		outputs[1][0] = (byte) (((theta3 - (180 + theta2) % 360) > (theta1 - (180 + theta2) % 360)) ? 2
				: 0);
		outputs[1][1] = (byte) (((theta3 - (180 + theta2) % 360) < (theta1 - (180 + theta2) % 360)) ? 2
				: 0);
		outputs[2][0] = (byte) (((theta1 - (180 + theta3) % 360) > (theta2 - (180 + theta3) % 360)) ? 0
				: 1);
		outputs[2][1] = (byte) (((theta1 - (180 + theta3) % 360) < (theta2 - (180 + theta3) % 360)) ? 0
				: 1);
	}

	public byte findInput(VorPoint temp) {
		if(temp.equals(p1)) return 0;
		if(temp.equals(p2)) return 1;
		if(temp.equals(p3)) return 2;
		return -1;
	
	}

}