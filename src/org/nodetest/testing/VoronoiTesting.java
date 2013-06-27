package org.nodetest.testing;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.ui.RectangleEdge;

import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

public class VoronoiTesting {
	public static void main(String[] args) {
		Random random=new Random();
		Voronoi ourVoronoi = new Voronoi(1);
		double[] ourXdoubles=new double[400];
		for (int i = 0; i < ourXdoubles.length; i++) {
			ourXdoubles[i]=random.nextInt(131072)-65536;
		}
		
		double[] ourYdoubles=new double[400];
		for (int i = 0; i < ourYdoubles.length; i++) {
			ourYdoubles[i]=random.nextInt(131072)-65536;
		}
		List<GraphEdge> edgeList = ourVoronoi.generateVoronoi(ourXdoubles, ourYdoubles, -65536, 65536, -65536, 65536);
		System.out.print("ListLinePlot[{");
		ArrayList<double[]> ourArray = new ArrayList<>();
		for (GraphEdge graphEdge : edgeList) {

			System.out.print("{{"+graphEdge.x1+","+graphEdge.y1+"},{"+graphEdge.x2+","+graphEdge.y2+"}},");

		}
		System.out.print("}, Frame -> True]");
		FastScatterPlot ourPlot = new FastScatterPlot(new float[][] {
				new float[] { 1, 1 }, new float[] { 2, 4 },
				new float[] { 3, 9 }, new float[] { 4, 16 }, }, new NumberAxis(
				"X"), new NumberAxis("Y"));
		JFreeChart chart = new JFreeChart("VORONOI TEsT", ourPlot);

	}

	static double[] calculateOurDot(GraphEdge ge, int i) {
		return new double[] { (i * ge.x1 + (256 - i) * ge.x2) / 256,
				(i * ge.y1 + (256 - i) * ge.y2) / 256 };
	}
}
