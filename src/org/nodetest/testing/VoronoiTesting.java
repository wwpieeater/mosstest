package org.nodetest.testing;

import java.util.Iterator;
import java.util.List;

import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

public class VoronoiTesting {
public static void main(String[] args){
	Voronoi ourVoronoi=new Voronoi(1);
	List<GraphEdge> edgeList=ourVoronoi.generateVoronoi(new double[]{2, 25, 84, 63, 119}, new double[]{4, 50, 140, 120, 240}, 0, 256, 0, 256);
	for (GraphEdge graphEdge : edgeList) {
		System.out.println("("+graphEdge.x1+","+graphEdge.y1+"),("+graphEdge.x2+","+graphEdge.y2+")");
	}
}
}
