package org.nodetest.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import be.humphreys.simplevoronoi.GraphEdge;

public class VorParams {
	HashSet<VorEdge> edges= new HashSet<>();
	HashSet<VorPoint> pointMap= new HashSet<>();
	List<GraphEdge> rawEdges= new ArrayList<>();
	ArrayList<VorPoly> polys=new ArrayList<>();
}
