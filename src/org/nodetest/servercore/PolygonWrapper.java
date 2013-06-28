package org.nodetest.servercore;

import java.util.TreeMap;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Polygonal;

public class PolygonWrapper implements Polygonal {
	Polygon polygon;
	double elevation;
	TreeMap<Double, PolygonWrapper> neighbors; //angle and polygon wrapper
	double temperature;
	double humidity;
	Biome biome;
}
