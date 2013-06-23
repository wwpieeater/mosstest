package org.nodetest.utilities;

public class VorEdge {
	VorPoint north;
	VorPoint south;
	VorEdge chiralNorthLeft;
	VorEdge chiralNorthRight;
	VorEdge chiralSouthLeft;
	VorEdge chiralSouthRight;
	VorEdge tempEdgeNorth = null;
	VorEdge tempEdgeSouth=null;
	double thetaNorth;
	double tempNorthTheta;
	double tempSouthTheta;
	boolean traversedEast;
	boolean traversedWest;

	static double thetaFacingNorth(VorEdge ed) {
		return Math.toDegrees(Math.atan2(ed.north.x - ed.south.x, ed.north.y
				- ed.south.y));
	}
	static double thetaFacingSouth(VorEdge ed) {
		return Math.toDegrees(Math.atan2(ed.south.x - ed.north.x, ed.south.y
				- ed.north.y));
	}
	public VorEdge(VorPoint p1, VorPoint p2) {
		if (p1.y == p2.y) {
			if (p1.x == p2.x) {
				throw new ArithmeticException();
			} else if (p2.x > p1.x) {
				north = p2;
				south = p1;
			} else {
				north = p1;
				south = p2;
			}
		} else if (p1.y > p2.y) {
			north = p1;
			south = p2;
		} else {
			north = p2;
			south = p1;
		}
		thetaNorth = Math.atan2(north.x - south.x, north.y - south.y);
		north.register(this);
		south.register(this);
	}
	public void addTmpNorth(VorEdge eIn, VorEdgeContact properties){
		if(this.tempEdgeNorth==null) {this.tempEdgeNorth=eIn; this.tempNorthTheta=properties.thetaOnInput;}
		else{
			if((properties.thetaOnInput-properties.thetaOnThis)>(tempNorthTheta-properties.thetaOnThis)){
				chiralNorthLeft=eIn;
				chiralNorthRight=tempEdgeNorth;
			}else{
				chiralNorthLeft=tempEdgeNorth;
				chiralNorthRight=eIn;
			}
		}
	}
	
	public void addTmpSouth(VorEdge eIn, VorEdgeContact properties){
		if(this.tempEdgeSouth==null) {this.tempEdgeSouth=eIn; this.tempSouthTheta=properties.thetaOnInput;}
		else{
			if((properties.thetaOnInput-properties.thetaOnThis)>(tempSouthTheta-properties.thetaOnThis)){
				chiralSouthLeft=eIn;
				chiralSouthRight=tempEdgeNorth;
			}else{
				chiralSouthLeft=tempEdgeNorth;
				chiralSouthRight=eIn;
			}
			
		}
	}
	
	public void addTouchingEdges(VorEdge e1) {
		try {
			new VorEdgeContact(e1);
		} catch (Exception e) {

		} finally {

		}
	}
	
	boolean dirIsNorth(VorPoint pointFrom){
		return pointFrom.equals(this.south);
	}

	 class VorEdgeContact {
		boolean isNorthOnThis;
		boolean isNorthOnInput;
		double thetaOnThis;
		double thetaOnInput;

		

		public VorEdgeContact(VorEdge eIn) throws Exception {
			if (eIn.north.equals(VorEdge.this.north)) {
				this.isNorthOnInput = true;
				this.isNorthOnThis = true;
				thetaOnThis = thetaFacingNorth(VorEdge.this);
				thetaOnInput = thetaFacingNorth(eIn);
				addTmpNorth(eIn, this);
				return;
			}
			if (eIn.north.equals(VorEdge.this.south)) {
				this.isNorthOnInput = true;
				this.isNorthOnThis = false;
				thetaOnThis = thetaFacingSouth(VorEdge.this);
				thetaOnInput = thetaFacingNorth(eIn);
				addTmpSouth(eIn, this);
				return;
			}
			if (eIn.south.equals(VorEdge.this.north)) {
				this.isNorthOnInput = false;
				this.isNorthOnThis = true;
				thetaOnThis = thetaFacingNorth(VorEdge.this);
				thetaOnInput = thetaFacingSouth(eIn);
				addTmpNorth(eIn, this);
				return;
			}
			if (eIn.south.equals(VorEdge.this.south)) {
				this.isNorthOnInput = false;
				this.isNorthOnThis = false;
				thetaOnThis = thetaFacingSouth(VorEdge.this);
				thetaOnInput = thetaFacingSouth(eIn);
				addTmpSouth(eIn, this);
				return;
			}
			throw new Exception();			
		}

	}

}
