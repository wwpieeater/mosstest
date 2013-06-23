package org.nodetest.utilities;

public class VorPoint{
	double x; double y; VorEdge e1, e2, e3;
	public VorPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	void register(VorEdge eIn){
		if(e1==null){
			e1=eIn; return;
		}
		if(e2==null){
			e2=eIn; return;
		}
		if(e3==null){
			e3=eIn; return;
		}
		throw new AssertionError();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof VorPoint)) {
			return false;
		}
		VorPoint other = (VorPoint) obj;
		
		if (Math.abs(x-other.x)>0.01) {
			return false;
		}
		if (Math.abs(y-other.y)>0.01) {
			return false;
		}
		return true;
	}
}