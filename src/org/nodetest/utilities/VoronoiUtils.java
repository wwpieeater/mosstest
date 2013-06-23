package org.nodetest.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import be.humphreys.simplevoronoi.*;

public class VoronoiUtils {

	static List<GraphEdge> getVoronoiEdges(long seed, int points) {
		// TODO simple seeding to get points
		return null;
	}

	static VorParams vorToPoints(List<GraphEdge> listIn) {
		VorParams ourParams = new VorParams();
		for (GraphEdge ge : listIn) {
			VorPoint p1 = new VorPoint(ge.x1, ge.y1);
			VorPoint p2 = new VorPoint(ge.x2, ge.y2);
			VorEdge ed = new VorEdge(p1, p2);
			ourParams.edges.add(ed);
			ourParams.pointMap.add(p1);
			ourParams.pointMap.add(p2);
		}
		for (VorEdge ed : ourParams.edges) {
			for (VorEdge ed2 : ourParams.edges) {
				if (!(ed.equals(ed2))) {
					ed.addTouchingEdges(ed2);
				}
			}
		}

		// Ready to traverse!
		Stack<VorPoint> openPoints = new Stack<>();
		VorPoint currentPoint = ourParams.pointMap.iterator().next();
		VorEdge currentEdge = currentPoint.e1;
		boolean currentGoingLeft = true;
		openPoints.push(currentPoint);
		boolean currentHeadingNorth;
		VorPoly currentPoly = new VorPoly();
		while (!openPoints.isEmpty()) {
			currentPoly.edges.add(currentEdge);
			currentPoly.vertices.add(currentPoint);
			currentHeadingNorth = currentEdge.dirIsNorth(currentPoint);
			currentPoint = currentHeadingNorth ? currentEdge.north
					: currentEdge.south;
			currentEdge = currentHeadingNorth ? (currentGoingLeft ? currentEdge.chiralNorthLeft
					: currentEdge.chiralNorthRight)
					: (currentGoingLeft ? currentEdge.chiralSouthLeft
							: currentEdge.chiralSouthRight);
		}

		return ourParams;
	}
}