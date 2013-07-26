package org.nodetest.servercore;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
public class RenderProcessor extends SimpleApplication {
	
	private Random rand = new Random();
	private double speed = 0.1;
	public static ArrayBlockingQueue<MossRenderEvent> renderEventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false);
	
	private ActionListener actionListener = new ActionListener() {
	    public void onAction(String name, boolean keyPressed, float tpf) {
	    	if (name.equals("Test") && !keyPressed) {
	    		renderEventQueue.add(new MossRenderChunkEvent());
	    	}
	    	if (name.equals("Left")) {
	    		renderEventQueue.add(new MossRenderPlayerMoveEvent(cam.getLocation().setX((float)(cam.getLocation().getX() - speed))));
	    	}
	    	if (name.equals("Forward") && !keyPressed) {
	    		renderEventQueue.add(new MossRenderChunkEvent());
	    	}
	    	if (name.equals("Right") && !keyPressed) {
	    		renderEventQueue.add(new MossRenderChunkEvent());
	    	}
	    	if (name.equals("Back") && !keyPressed) {
	    		renderEventQueue.add(new MossRenderChunkEvent());
	    	}
	    }
	};
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
						float x = (float)rand.nextInt(2);
						float y = (float)rand.nextInt(2);
						float z = (float)rand.nextInt(2);
						Vector3f loc = new Vector3f (x, y, z);
						
						Box b = new Box(loc, 1, 1, 1); // create cube shape
				        Geometry geom = new Geometry("Box", b);
				        Material mat = new Material(assetManager,
				        "Common/MatDefs/Misc/Unshaded.j3md");
				        mat.setColor("Color", ColorRGBA.Blue);
				        geom.setMaterial(mat);
				        rootNode.attachChild(geom);
					}
					else if (myEvent instanceof MossRenderPlayerMoveEvent) {
						cam.setLocation(((MossRenderPlayerMoveEvent) myEvent).getPos());
					}
					//Add more events
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	});
	static void init () { //This and the main method are simply temporary.  Need to keep a testing environment in here.
		RenderProcessor app = new RenderProcessor ();
		app.start();
	}
	public static void main (String [] args) {
		//Temporary
		RenderProcessor.init();
	}
	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		renderThread.start ();
		flyCam.setEnabled(false);
		initKeyBindings();
	}
	
	private void initKeyBindings () {
		inputManager.addMapping("Test", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(actionListener, "Test");
		inputManager.addListener(actionListener, "Left");
		inputManager.addListener(actionListener, "Right");
		inputManager.addListener(actionListener, "Up");
		inputManager.addListener(actionListener, "Down");
	}
}
