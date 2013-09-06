package net.mosstest.servercore;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import jme3tools.optimize.GeometryBatchFactory;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.math.ColorRGBA;
import java.util.Arrays;
public class RenderProcessor extends SimpleApplication {
	
	private float speed = (float) 0.1;
	private final float blockSize = (float)15;
	private float[] locChanges = {0,0,0};
	private boolean invertY = false;
	private Vector3f initialUpVec;
	protected float rotationSpeed = 1f;
	private Node worldNode;
	public static ArrayBlockingQueue<MossRenderEvent> renderEventQueue = new ArrayBlockingQueue<>(
			24000, false);
	
	
	@Override
	public void simpleUpdate (float tpf) {
		moveWorld(locChanges[0], locChanges[1], locChanges[2]);
		
		inputManager.setCursorVisible(false); 
		MossRenderEvent myEvent = renderEventQueue.poll();
		if (myEvent instanceof MossRenderStopEvent) {
			System.out.println("Thread shutting down");
		}
		else if (myEvent instanceof MossRenderChunkEvent) {
			int x = ((MossRenderChunkEvent) myEvent).getX();
			int y = ((MossRenderChunkEvent) myEvent).getY();
			int z = ((MossRenderChunkEvent) myEvent).getZ();
			
			Vector3f home = new Vector3f (x, y, z);
			
			
			for(byte i=0; i<16; i++) { //x
				for(byte j=0; j<1; j++) { //y
					for(byte k=0; k<16; k++) { //z
						int nVal = ((MossRenderChunkEvent) myEvent).getNodeId(i, j, k);
						
						switch (nVal) {
						case 0: break;
						case 1:
							Vector3f loc = new Vector3f(home.x+i*(blockSize), home.y-j*(blockSize)-40, home.z+k*(blockSize));
							Box b = new Box(loc, blockSize, blockSize, blockSize);
						    Geometry geom = new Geometry("Box", b);
						    Material mat = new Material(assetManager,
						    "Common/MatDefs/Misc/Unshaded.j3md");
						    mat.setColor("Color", ColorRGBA.Blue);
						    geom.setMaterial(mat);
						    worldNode.attachChild(geom);
						    break;
						}
						
					}
				}
			}
			
			System.out.println("FINISHED MAKING BOXES");
			
		}
			//Add more events
	}
	public static void init () { //This and the main method are simply temporary.  Need to keep a testing environment in here.
		RenderProcessor app = new RenderProcessor ();
		AppSettings settings = new AppSettings(true);
		settings.setResolution(800, 600);
		settings.setSamples(2);
		app.setSettings(settings);
		app.setShowSettings(false);
		app.start();
	}
	public static void main (String [] args) {
		//Temporary
		RenderProcessor.init();
	}
	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		worldNode = new Node("world");
		rootNode.attachChild(worldNode);
		testChunkEvents();
		flyCam.setEnabled(false);
		initialUpVec = cam.getUp().clone();
		initKeyBindings();
		
	}
	
	public void testChunkEvents () {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				Position pos = new Position(-16+(i*8), 0, -16+(j*8), 0);
				boolean[][][] testModified = new boolean[16][16][16];
				for(boolean[][] l1 : testModified) {
					for(boolean[] l2 : l1) {
						Arrays.fill(l2, false);
					}
				}
				
				int[][][] testNodes = new int[16][16][16];
				for(int[][] l1 : testNodes) {
					for(int[] l2 : l1) {
						Arrays.fill(l2, 1);
					}
				}
				
				testNodes[0][0][0] = 0;
				testNodes[0][0][1] = 0;
				
				MapChunk ch = new MapChunk(pos, testNodes, testModified);
				MossRenderChunkEvent evt = new MossRenderChunkEvent (ch);
				renderEventQueue.add(evt);
			}
		}
		GeometryBatchFactory.optimize(worldNode);
	}
	
	private void moveWorld(float cx, float cy, float cz) {

		Vector2f transVector = new Vector2f(cam.getDirection().x,
				cam.getDirection().z);
	
		worldNode
				.setLocalTranslation(worldNode
						.getLocalTranslation()
						.addLocal(
								new Vector3f(-cz * transVector.x, 0f, -cz
										* transVector.y))
						.addLocal(-cx * transVector.y, 0, cx * transVector.x));

	}
	
	private void rotateCamera(float value, Vector3f axis){

        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        cam.setAxes(q);
    }
	
	private ActionListener actionListener = new ActionListener() {
	    public void onAction(String name, boolean keyPressed, float tpf) {
	    	if (name.equals("Test") && !keyPressed) {
	    		//renderEventQueue.add(testEvent2);
	    	}
	    	if (name.equals("Left") && keyPressed) {locChanges[0] = speed;} //On key down
	    	else if (name.equals("Left") && !keyPressed && locChanges[0] == speed) {locChanges[0] = 0;} //on key up
	    	
	    	if (name.equals("Right") && keyPressed) {locChanges[0] = -speed;} //On key down
	    	else if (name.equals("Right") && !keyPressed && locChanges[0] == -speed) {locChanges[0] = 0;} //on key up
	    	
	    	if (name.equals("Forward") && keyPressed) {locChanges[2] = speed;} //On key down
	    	else if (name.equals("Forward") && !keyPressed && locChanges[2] == speed) {locChanges[2] = 0;} //on key up
	    	
	    	if (name.equals("Back") && keyPressed) {locChanges[2] = -speed;} //On key down
	    	else if (name.equals("Back") && !keyPressed && locChanges[2] == -speed) {locChanges[2] = 0;} //on key up
	    	
	    	
	    }
	};
	private AnalogListener analogListener = new AnalogListener () {
		public void onAnalog (String name, float value, float tpf) {
			if (name.equals("CAM_Left")){rotateCamera(value, initialUpVec);}
		   	else if (name.equals("CAM_Right")){rotateCamera(-value, initialUpVec);}
		       else if (name.equals("CAM_Up")){rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());}
		   	else if (name.equals("CAM_Down")){rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());}
		}
	};
	private void initKeyBindings () {
		inputManager.addMapping("Test", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
		
		inputManager.addMapping("CAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));

		inputManager.addMapping("CAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false),
		                 new KeyTrigger(KeyInput.KEY_RIGHT));
		
		inputManager.addMapping("CAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false),
		              new KeyTrigger(KeyInput.KEY_UP));
		
		inputManager.addMapping("CAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
		                new KeyTrigger(KeyInput.KEY_DOWN));
		
		inputManager.addListener(actionListener, "Test");
		inputManager.addListener(actionListener, "Left");
		inputManager.addListener(actionListener, "Right");
		inputManager.addListener(actionListener, "Forward");
		inputManager.addListener(actionListener, "Back");
		inputManager.addListener(analogListener, "CAM_Left");
		inputManager.addListener(analogListener, "CAM_Right");
		inputManager.addListener(analogListener, "CAM_Up");
		inputManager.addListener(analogListener, "CAM_Down");
		
	}
}
