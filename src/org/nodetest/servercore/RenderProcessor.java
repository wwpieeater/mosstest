package org.nodetest.servercore;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import com.jme3.app.Application;
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
	private boolean left, right, forward, back;
	private double speed = 0.001;
	public static ArrayBlockingQueue<MossRenderEvent> renderEventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false);
	
	@Override
	public void simpleUpdate (float tpf) {
		MossRenderEvent myEvent = renderEventQueue.poll();
		if (myEvent instanceof MossRenderStopEvent) {
			System.out.println("Thread shutting down");
		}
		else if (myEvent instanceof MossRenderChunkEvent) {
			System.out.println("RENDERING CHUNK");
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
		flyCam.setMoveSpeed(30);
		initKeyBindings();
	}
	
	private ActionListener actionListener = new ActionListener() {
	    public void onAction(String name, boolean keyPressed, float tpf) {
	    	if (name.equals("Test") && !keyPressed) {
	    		renderEventQueue.add(new MossRenderChunkEvent());
	    	}
	    }
	};
	
	private void initKeyBindings () {
		inputManager.addMapping("Test", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addListener(actionListener, "Test");
	}
}
