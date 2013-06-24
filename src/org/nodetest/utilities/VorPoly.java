package org.nodetest.utilities;

import java.util.ArrayList;

public class VorPoly {
	ArrayList<VorPoint> vertices;
	public VorPoly() {
		vertices=new ArrayList<>();
	}
	public void toMathematica() {
		System.out.print("Graphics[ {Opacity[0.4], Polygon[{");
		for(VorPoint pt:vertices){
			System.out.print("{"+pt.x+","+pt.y+"},");
		}
		System.out.print("}]}],");
	}
}
