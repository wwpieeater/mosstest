package org.nodetest.utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

public class VoronoiUtils {
	static int relCounter = 0;
	static int hit1Ctr = 0;
	static int collisionCtr = 0;

	static List<GraphEdge> getVoronoiEdges(long seed, int points) {
		// TODO simple seeding to get points
		return null;
	}

	static void vorToPoints(List<GraphEdge> listIn, double minX,
			double maxX, double minY, double maxY) {
		VorParams ourParams = new VorParams();
		// for (GraphEdge ge : listIn) {
		// VorPoint points[0] = new VorPoint(ge.x1, ge.y1);
		// VorPoint points[1] = new VorPoint(ge.x2, ge.y2);
		// VorEdge ed = new VorEdge(points[0], points[1]);
		// ourParams.edges.add(ed);
		// ourParams.pointMap.add(points[0]);
		// ourParams.pointMap.add(points[1]);
		// }
		// for (VorEdge ed : ourParams.edges) {
		// for (VorEdge ed2 : ourParams.edges) {
		// if (ed.thetaNorth != ed2.thetaNorth) {
		// ed.addTouchingEdges(ed2);
		// hit1Ctr++;
		// } else
		// collisionCtr++;
		// }
		// }
		// System.out.println(relCounter);

		// System.out.println(hit1Ctr);
		// System.out.println(collisionCtr);
		// Ready to traverse! Comment out old attempt

		// Stack<VorEdge> openEdges = new Stack<>();
		// VorPoint currentPoint = ourParams.pointMap.iterator().next();
		// VorEdge currentEdge = currentPoint.e1;
		// boolean currentGoingLeft = true;
		// openEdges.push(currentEdge);
		// boolean currentHeadingNorth;
		// VorPoly currentPoly = new VorPoly();
		// while (!openEdges.isEmpty()) {
		// currentPoly.edges.add(currentEdge);
		// currentPoly.vertices.add(currentPoint);
		// currentHeadingNorth = currentEdge.dirIsNorth(currentPoint);
		// currentPoint = currentHeadingNorth ? currentEdge.north
		// : currentEdge.south;
		// currentEdge.lastHeadNorth = currentHeadingNorth;
		// if (currentHeadingNorth) { // Direction on current segment
		// if (currentGoingLeft) { // Turn wanted to make
		// if (!currentEdge.chiralNorthLeft.traversedWest) {
		// if (!currentEdge.traversedEast)
		//
		// openEdges.push(currentEdge);
		// currentEdge = currentEdge.chiralNorthLeft;
		//
		// } else {
		// if (!currentEdge.traversedEast)
		// openEdges.push(currentEdge);
		// ourParams.polys.add(currentPoly);
		// currentPoly = new VorPoly();
		// currentEdge = openEdges.pop();
		// currentGoingLeft = !currentGoingLeft;
		// }
		// } else {
		// if (!currentEdge.chiralNorthRight.traversedEast) {
		// if (!currentEdge.traversedWest)
		//
		// openEdges.push(currentEdge);
		// currentEdge = currentEdge.chiralNorthRight;
		//
		// } else {
		// if (!currentEdge.traversedWest)
		// openEdges.push(currentEdge);
		// ourParams.polys.add(currentPoly);
		// currentPoly = new VorPoly();
		// currentEdge = openEdges.pop();
		// currentGoingLeft = !currentGoingLeft;
		// }
		// }
		// } else {
		// if (currentGoingLeft) { // Turn wanted to make
		// if (!currentEdge.chiralSouthLeft.traversedWest) {
		// if (!currentEdge.traversedWest)
		//
		// openEdges.push(currentEdge);
		// currentEdge = currentEdge.chiralSouthLeft;
		//
		// } else {
		// if (!currentEdge.traversedWest)
		// openEdges.push(currentEdge);
		// ourParams.polys.add(currentPoly);
		// currentPoly = new VorPoly();
		// currentEdge = openEdges.pop();
		// currentGoingLeft = !currentGoingLeft;
		// }
		// } else {
		// if (!currentEdge.chiralSouthRight.traversedWest) {
		// if (!currentEdge.traversedEast)
		//
		// openEdges.push(currentEdge);
		// currentEdge = currentEdge.chiralSouthRight;
		//
		// } else {
		// if (!currentEdge.traversedEast)
		// openEdges.push(currentEdge);
		// ourParams.polys.add(currentPoly);
		// currentPoly = new VorPoly();
		// currentEdge = openEdges.pop();
		// currentGoingLeft = !currentGoingLeft;
		// }
		// }
		// }
		// }
		// for(VorPoly poly : ourParams.polys){
		// poly.toMathematica();
		// }
		int points = 0;
		int conns = 0;
		TreeMap<VorPoint, VorPoint> allPoints = new TreeMap<>(
				new Comparator<VorPoint>() {

					@Override
					public int compare(VorPoint o1, VorPoint o2) {
						if (o1.x - o2.x > 4.0)
							return 1;
						else if (o1.x - o2.x < -4.0)
							return -1;
						else {
							if (o1.y - o2.y > 4.0)
								return 1;
							else if (o1.y - o2.y < -4.0)
								return -1;
							else
								return 0;
						}

					}
				});
		TreeSet<VorPoint> topEdge = new TreeSet<VorPoint>(
				new Comparator<VorPoint>() {

					@Override
					public int compare(VorPoint o1, VorPoint o2) {
						if (o1.x == o2.x)
							return 0;
						return o1.x < o2.x ? -1 : 1;
					}
				});
		TreeSet<VorPoint> btmEdge = new TreeSet<VorPoint>(
				new Comparator<VorPoint>() {

					@Override
					public int compare(VorPoint o1, VorPoint o2) {
						if (o1.x == o2.x)
							return 0;
						return o1.x < o2.x ? -1 : 1;
					}
				});

		TreeSet<VorPoint> leftEdge = new TreeSet<VorPoint>(
				new Comparator<VorPoint>() {

					@Override
					public int compare(VorPoint o1, VorPoint o2) {
						if (o1.y == o2.y)
							return 0;
						return o1.y < o2.y ? -1 : 1;
					}
				});
		TreeSet<VorPoint> rightEdge = new TreeSet<VorPoint>(
				new Comparator<VorPoint>() {

					@Override
					public int compare(VorPoint o1, VorPoint o2) {
						if (o1.y == o2.y)
							return 0;
						return o1.y < o2.y ? -1 : 1;
					}
				});
		System.out.println("ListLinePlot[{");
		parseInList:for (GraphEdge ge : listIn) {
			points++;
			points++;
			VorPoint p1 = new VorPoint(Math.round(ge.x1), Math.round(ge.y1));
			VorPoint p2 = new VorPoint(Math.round(ge.x2), Math.round(ge.y2));
			if(p1.equals(p2)) continue parseInList;
			System.out.println("{{" + p1.x + "," + p1.y + "},{" + p2.x + ","
					+ p2.y + "}},");
			if (allPoints.containsKey(p1)) {
				p1 = allPoints.get(p1);
				
			}
			if (allPoints.containsKey(p2)) {
				p2 = allPoints.get(p2);
				points--;
			}
			if (Math.abs(p1.x - maxX) < 2) {
				rightEdge.add(p1);
				//System.out.println("R[" + points[0].x + "," + points[0].y + "]");
			}
			if (Math.abs(p1.x - minX) < 2) {
				leftEdge.add(p1);
				//System.out.println("L[" + points[0].x + "," + points[0].y + "]");
			}
			if (Math.abs(p1.y - maxY) < 2) {
				topEdge.add(p1);
				//System.out.println("T[" + points[0].x + "," + points[0].y + "]");
			}
			if (Math.abs(p1.y - minY) < 2) {
				btmEdge.add(p1);
				//System.out.println("B[" + points[0].x + "," + points[0].y + "]");
			}
			if (Math.abs(p2.x - maxX) < 2) {
				rightEdge.add(p2);
				//System.out.println("R[" + points[1].x + "," + points[1].y + "]");
			}
			if (Math.abs(p2.x - minX) < 2) {
				leftEdge.add(p2);
				//System.out.println("L[" + points[1].x + "," + points[1].y + "]");
			}
			if (Math.abs(p2.y - maxY) < 2) {
				topEdge.add(p2);
				//System.out.println("T[" + points[1].x + "," + points[1].y + "]");
			}
			if (Math.abs(p2.y - minY) < 2) {
				btmEdge.add(p2);
				//System.out.println("B[" + points[1].x + "," + points[1].y + "]");
			}
			p1.register(p2);
			p2.register(p1);
			conns += 2;
			allPoints.put(p1,p1);
			allPoints.put(p2,p2);
		}
		System.out.println("},Frame-> True]");
System.out.println("(* entering iterator *), ");
System.out.println("ListLinePlot[{");
		//System.out.println(topEdge.size() + "," + rightEdge.size() + ","
		//		+ btmEdge.size() + "," + leftEdge.size());
		Iterator<VorPoint> topEdgeIterator = topEdge.iterator();
		Iterator<VorPoint> rightEdgeIterator = rightEdge.descendingIterator();
		Iterator<VorPoint> btmEdgeIterator = btmEdge.descendingIterator();
		Iterator<VorPoint> leftEdgeIterator = leftEdge.iterator();
		VorPoint tmp, startPoint;
		// try {
		tmp = topEdgeIterator.next();
		startPoint = tmp;
		VorPoint currentPoint;
		while (topEdgeIterator.hasNext()) {
			currentPoint = (VorPoint) topEdgeIterator.next();
			currentPoint.register(tmp);

			tmp.register(currentPoint);
			conns++;
			tmp = currentPoint;

		}

		while (rightEdgeIterator.hasNext()) {
			currentPoint = (VorPoint) rightEdgeIterator.next();
			currentPoint.register(tmp);
			tmp.register(currentPoint);
			conns++;
			tmp = currentPoint;
		}
		// } catch (Exception e) {
		// tmp = btmEdgeIterator.next();
		// startPoint = tmp;
		// }
		// try {
		while (btmEdgeIterator.hasNext()) {
			currentPoint = (VorPoint) btmEdgeIterator.next();
			currentPoint.register(tmp);
			tmp.register(currentPoint);
			tmp = currentPoint;
		}
		// } catch (Exception e) {
		// tmp = leftEdgeIterator.next();
		// startPoint = tmp;
		// }
		while (leftEdgeIterator.hasNext()) {
			currentPoint = (VorPoint) leftEdgeIterator.next();
			currentPoint.register(tmp);
			tmp.register(currentPoint);
			conns++;
			tmp = currentPoint;
		}
		// try {
		tmp.register(startPoint);
		startPoint.register(tmp);
		conns++;
		// } catch (Exception e) {
		// System.out.println("NO edge points found :/");
		// e.printStackTrace();
		// }
		System.out.println("},{Frame-> True, Dashed} ]");
		System.out.println(points + "," + conns);
		for (VorPoint vorPoint : allPoints.keySet()) {
			vorPoint.finalizeRegistration();
		}
		// Let's begin iterating!
		boolean headingLeft = true;
		System.out.println(allPoints.size());
		VorPoint temp = allPoints.firstKey();
		currentPoint = temp;
		Stack<VorPoint> openStack = new Stack<>();
		ArrayList<VorPoly> polys = new ArrayList<>();
		VorPoly currentPoly = new VorPoly();
		currentPoly.vertices.add(temp);
		openStack.push(temp);
		currentPoint = temp.points[0];
		int lastOutput = 0; // p<n> maps to n-1 for this
		FindPolysLoop: while (true) {
			temp.directionsTaken[lastOutput][headingLeft ? 0 : 1] = true;
			//System.out.println(currentPoint.findInput(temp)+">>>>>");
			currentPoint.lastInput = currentPoint.findInput(temp);
			currentPoly.vertices.add(currentPoint);
			if (!currentPoint.directionsTaken[currentPoint.outputs[currentPoint
					.findInput(temp)][headingLeft ? 1 : 0]][headingLeft ? 1 : 0])
				{openStack.push(currentPoint);
				System.out.print("o");
				}
			if (currentPoint.directionsTaken[currentPoint.outputs[currentPoint
					.findInput(temp)][headingLeft ? 0 : 1]][headingLeft ? 0 : 1]) {
				System.out.print("s");
				headingLeft = !headingLeft;
				polys.add(currentPoly);
				//currentPoly.toMathematica();
				currentPoly = new VorPoly();
				if (openStack.isEmpty()) break FindPolysLoop;
				currentPoint=openStack.pop(); continue FindPolysLoop;
			}else{
				System.out.print("p");
				currentPoint.directionsTaken[currentPoint.outputs[currentPoint.lastInput][headingLeft?0:1]][headingLeft?0:1]=true;
				temp=currentPoint;
//				switch (temp.outputs[temp.lastInput][headingLeft?0:1]){
//				case 0:
//					currentPoint=currentPoint.points[0];
//					break;
//				case 1:
//					currentPoint=currentPoint.points[1];
//					break;
//				case 2: 
//					currentPoint=currentPoint.points[2];
//					break;
//				default: throw new NullPointerException(Byte.toString(temp.outputs[temp.lastInput][headingLeft?0:1]));
//				}
			}
			currentPoint=currentPoint.points[currentPoint.outputs[currentPoint.lastInput][headingLeft?0:1]];
				
		}
		System.out.println(polys.size()+"]]]]]]]");
		for (VorPoly poly : polys) {
			poly.toMathematica();
		}
	
	}

	public static void main(String[] args) {
		Random rand = new Random();
		rand.setSeed(0);
		double[] xDoubles = new double[40];
		for (int i = 0; i < xDoubles.length; i++) {
			xDoubles[i] = rand.nextInt(131000) - 65500;
		}
		double[] yDoubles = new double[40];
		for (int i = 0; i < yDoubles.length; i++) {
			yDoubles[i] = rand.nextInt(131000) - 65500;
		}
		vorToPoints(new Voronoi(1).generateVoronoi(xDoubles, yDoubles, -65536,
				65536, -65535, 65535), -65536, 65536, -65536, 65536);
	}
}