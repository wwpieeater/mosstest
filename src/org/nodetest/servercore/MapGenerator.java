package org.nodetest.servercore;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;


public class MapGenerator {
	static Geometry vorDiagram;
	static final int minX=-65536;
	static final int maxX=65536;
	static final int minY=-65536;
	static final int maxY=65536;
	static ArrayList<Coordinate> generatorPoints;
	static void init(long seed, int points){
		Random rand=new Random();
		rand.setSeed(seed);
		generatorPoints=new ArrayList<>();
		for(int i=0; i<points; i++){
			generatorPoints.add(new Coordinate(minX+rand.nextDouble()*(maxX-minX),minY+rand.nextDouble()*(maxY-minY)));

		}
		VoronoiDiagramBuilder vor=new VoronoiDiagramBuilder();
		vor.setSites(generatorPoints);
		vorDiagram=vor.getDiagram(new GeometryFactory());
		System.out.println(vorDiagram.getNumGeometries());
		for (int i = 0; i < vorDiagram.getNumGeometries(); i++) {
			String pat1=Pattern.compile("POLYGON ((", Pattern.LITERAL).matcher(vorDiagram.getGeometryN(i).toText()).replaceFirst("Graphics[{Opacity[0.4], Polygon[{{");
			pat1=Pattern.compile("))", Pattern.LITERAL).matcher(pat1).replaceFirst("}}]}],");
			pat1=Pattern.compile("[0-9],").matcher(pat1).replaceAll("},{");
			pat1=Pattern.compile("([0-9])\\s([0-9-])").matcher(pat1).replaceAll("$1,$2");

			System.out.println(pat1);
			//System.out.println("Graphics[{Opacity[0.4], Polygon[{");
			for (int j = 0; j < vorDiagram.getGeometryN(i).getEnvelope().getCoordinates().length; j++) {
				//System.out.print("{"+vorDiagram.getGeometryN(i).getEnvelope().getCoordinates()[j].x+","+vorDiagram.getGeometryN(i).getEnvelope().getCoordinates()[j].y+"}");
				//if(j < vorDiagram.getGeometryN(i).getEnvelope().getCoordinates().length-1) System.out.println(",");
			}
			//System.out.println("}]}],");
		}
	}
	
	public static void main(String[] args) {
		init(6, 4);
	}
}
