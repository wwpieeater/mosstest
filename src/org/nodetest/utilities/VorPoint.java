package org.nodetest.utilities;

public class VorPoint {
	public VorPoint(long x, long y) {
		this.x = x;
		this.y = y;
	}

	long x, y;
	VorPoint[] points=new VorPoint[3];
	double[][]thetas=new double[3][3];//In then out
	/**
	 * 3x2 boolean array. First dimension is points[0], points[1], points[2](outputs) Second
	 * dimension is l and r;
	 */
	boolean[][] directionsTaken = new boolean[3][2];
	byte[][] outputs = new byte[3][2];
	double theta1, theta2, theta3;

	public void register(VorPoint ptIn) {
		System.out.println("{{   "+(this.x/*+400*/)+","+(this.y/*-400*/)+"},{"+(ptIn.x/*-40 0*/)+","+(ptIn.y+400)+"   }},");
		if (this.points[0] == null) {
			points[0] = ptIn;
			return;
		}
		if (this.points[1] == null) {
			points[1] = ptIn;
			return;
		}
		if (this.points[2] == null) {
			points[2] = ptIn;
			return;
		}
		System.out.println("bad mapping");
		//throw new OutOfMemoryError("Invalid point mapping");
	}
	byte lastInput=-1;
	public void finalizeRegistration() {
		//if(points[0]==null||points[1]==null||points[2]==null) throw new NullPointerException();
//		try{theta1 = Math.atan2(points[0].y - this.y, points[0].x - this.x);}catch (Exception e){theta1=0;}
//		try{theta2 = Math.atan2(points[1].y - this.y, points[1].x - this.x);}catch(Exception e){theta2=0;}
//		try{theta3 = Math.atan2(points[2].y - this.y, points[2].x - this.x);}catch (Exception e){theta3=0;}
		
		for(int i=0;i<3; i++){
			for(int j=0; j<3; j++){
				thetas[i][j]=calcTheta(points[i], this, points[j]);
				runDecision(thetas[i], i);
			}
			
		}
		
	}

	private void runDecision(double[] thetasIn, int i) {
		switch (i){
		case 0:
			outputs[0][0]=(byte) (thetas[0][1]<thetas[0][2]?1:2);
			outputs[0][1]=(byte) (thetas[0][1]>thetas[0][2]?1:2);
			break;
		case 1:
			outputs[1][0]=(byte) (thetas[1][0]<thetas[1][2]?1:2);
			outputs[1][1]=(byte) (thetas[1][0]>thetas[1][2]?1:2);
			break;
		case 2:
			outputs[2][0]=(byte) (thetas[2][0]<thetas[2][1]?1:2);
			outputs[2][1]=(byte) (thetas[2][0]>thetas[2][1]?1:2);
			break;
		}
		
	}

	private double calcTheta(VorPoint ptIn, VorPoint vtx, VorPoint ptOut) {
		//double thetaEntering=Math.toDegrees(Math.atan2(ptIn.y-vtx.y, ptIn.x-vtx.x));
		return (Math.toDegrees(Math.atan2(ptOut.y-vtx.y, ptOut.x-vtx.x))-Math.toDegrees(Math.atan2(ptIn.y-vtx.y, ptIn.x-vtx.x))+1440)%360;
	}

	public byte findInput(VorPoint temp) {
		if(temp.equals(points[0])) return 0;
		if(temp.equals(points[1])) return 1;
		if(temp.equals(points[2])) return 2;
		return 1;
	
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof VorPoint)) {
			return false;
		}
		VorPoint other = (VorPoint) obj;
		if (Math.abs(x-other.x)>2) {
			return false;
		}
		if (Math.abs(y-other.y)>2) {
			return false;
		}
		return true;
	}

}