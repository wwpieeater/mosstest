package org.nodetest.servercore;

import java.util.concurrent.ArrayBlockingQueue;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
public class RenderProcessor extends SimpleApplication {
	public static ArrayBlockingQueue<MossRenderEvent> renderEventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false);
	
	private Thread renderThread = new Thread(new Runnable () {
		public void run () {
			boolean run = true;
			System.out.println("Thread initiated");
			while (run) {
				try {
					MossRenderEvent myEvent = renderEventQueue.take();
					if (myEvent instanceof MossRenderStopEvent) {
						System.out.println("Thread shutting down");
						run = false;
					}
					else if (myEvent instanceof MossRenderChunkEvent) {
						Box b = new Box(1, 1, 1); // create cube shape
				        Geometry geom = new Geometry("Box", b);
				        Material mat = new Material(assetManager,
				        "Common/MatDefs/Misc/Unshaded.j3md");
				        mat.setColor("Color", ColorRGBA.Blue);
				        geom.setMaterial(mat);
				        rootNode.attachChild(geom);
					}
					//Add more events
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	});
	static void init () {
		renderEventQueue.add(new MossRenderChunkEvent ());
		RenderProcessor app = new RenderProcessor ();
		app.start();
	}
	public static void main (String [] args) {
		RenderProcessor.init();
	}
	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		renderThread.start ();
	}
}
