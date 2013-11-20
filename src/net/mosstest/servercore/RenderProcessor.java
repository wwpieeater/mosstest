package net.mosstest.servercore;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import jme3tools.optimize.GeometryBatchFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.system.AppSettings;
import com.jme3.math.ColorRGBA;

import java.util.Arrays;

import net.mosstest.scripting.INodeParams;
import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.MapNode;
import net.mosstest.scripting.Position;

public class RenderProcessor extends SimpleApplication {

	private final float SPEED = 3f;
	private final float PLAYER_HEIGHT = 25;
	private final float BLOCK_SIZE = 20f;
	private final float ROTATION_SPEED = 1f;
	private final double BLOCK_OFFSET_FROM_CENTER = 8 * BLOCK_SIZE;
	private final double CHUNK_OFFSET = 16 * BLOCK_SIZE;
	private float[] locChanges = { 0, 0, 0 };
	private double lastTime;
	private boolean invertY = false;
	
	private Vector3f initialUpVec;
	private Node worldNode;
	private SpotLight spot;
	private PointLight lamp;
	private DirectionalLight sun;
	private HashMap<Position, RenderMapChunk> allChunks = new HashMap<Position, RenderMapChunk>();

	public INodeManager nManager;
	public IRenderPreparator rPreparator;
	public ArrayBlockingQueue<MossRenderEvent> renderEventQueue = new ArrayBlockingQueue<>(
			24000, false);

	public static RenderProcessor init(INodeManager manager, IRenderPreparator prep) {
		RenderProcessor app = new RenderProcessor();
		AppSettings settings = new AppSettings(true);
		settings.setResolution(800, 600);
		settings.setSamples(2);
		app.setSettings(settings);
		app.setShowSettings(false);
		app.initNodeThings(manager, prep);
		app.start();
		return app;
	}

	private void initNodeThings(INodeManager manager, IRenderPreparator prep) {
		nManager = manager;
		rPreparator = prep;
	}

	@Override
	public void simpleInitApp() {
		lastTime = 0;
		
		setupWorldNode ();
		setupFlashlight();
		setupSunlight();
		//setupLamplight();
		
		
		//localChunkTest();
		preparatorChunkTest();
		flyCam.setEnabled(false);
		initialUpVec = cam.getUp().clone();
		initKeyBindings();
	}

	@Override
	/**
	 * Constant running loop that's built into SimpleApplication.
	 * Looks for new events in the renderEventQueue, moves if necessary.
	 */
	public void simpleUpdate(float tpf) {
		if (lastTime + 10 < System.currentTimeMillis()) {
			moveWorld(locChanges[0], locChanges[1], locChanges[2]);
			lastTime = System.currentTimeMillis();
		}

		inputManager.setCursorVisible(false);
		MossRenderEvent myEvent = renderEventQueue.poll();
		if (myEvent instanceof MossRenderStopEvent) {
			System.out.println("Thread shutting down");
		}
		else if (myEvent instanceof MossRenderChunkEvent) {
			System.out.println("GOT AN EVENT TO RENDER A CHUNK");
			renderChunk(((MossRenderChunkEvent) myEvent).getChk(),
					((MossRenderChunkEvent) myEvent).getPos());
		}
	}
	
	public void getChunk (Position pos) {
		MapChunk maybe = null;
		try {maybe = rPreparator.requestChunk(pos);} 
		catch (MapGeneratorException e) {e.printStackTrace();} 
		catch (InterruptedException e) {e.printStackTrace();}
		if (maybe != null) {renderChunk(maybe, pos);}
	}
	
	public void renderChunk(MapChunk chk, Position pos) {
		int vertexIndexCounter = 0;
		
		Mesh completeMesh = new Mesh ();
		FloatBuffer vertices = FloatBuffer.allocate(500000);
		FloatBuffer normals = FloatBuffer.allocate(500000);
		IntBuffer indices = IntBuffer.allocate(500000);
		//RenderNode[][][] nodesInChunk = new RenderNode[16][16][16];

		for (byte i = 0; i < 16; i++) {
			for (byte j = 0; j < 16; j++) {
				for (byte k = 0; k < 16; k++) {
					int nVal = chk.getNodeId(i, j, k);
					//MapNode node = nManager.getNode((short) nVal);
					//Material mat = getMaterial((short) nVal);
					if (nVal == 0) {return;}
					
					else {
						
						float x = (float) ((pos.x + (CHUNK_OFFSET * pos.x)) - BLOCK_OFFSET_FROM_CENTER + (i * BLOCK_SIZE));
						float y = (float) ((pos.y - PLAYER_HEIGHT) - (j * BLOCK_SIZE));
						float z = (float) ((pos.z + (CHUNK_OFFSET * pos.z)) - BLOCK_OFFSET_FROM_CENTER  + (k * BLOCK_SIZE));

						vertices.put(x).put(y).put(z); //Front face
						vertices.put(x).put(y - BLOCK_SIZE).put(z);
						vertices.put(x + BLOCK_SIZE).put(y).put(z);
						vertices.put(x + BLOCK_SIZE).put(y - BLOCK_SIZE).put(z); //Top Face
						vertices.put(x).put(y).put(z + BLOCK_SIZE);
						vertices.put(x + BLOCK_SIZE).put(y).put(z + BLOCK_SIZE);
						vertices.put(x + BLOCK_SIZE).put(y - BLOCK_SIZE).put(z + BLOCK_SIZE); //right face
						vertices.put(x).put(y - BLOCK_SIZE).put(z + BLOCK_SIZE); //left face
						for(int m=0; m<8; m++) {
							normals.put(0).put(0).put(10);
						}
						
						indices.put(vertexIndexCounter + 3).put(vertexIndexCounter + 1).put(vertexIndexCounter + 0);//front
						indices.put(vertexIndexCounter + 3).put(vertexIndexCounter + 0).put(vertexIndexCounter + 2);
						indices.put(vertexIndexCounter + 4).put(vertexIndexCounter + 2).put(vertexIndexCounter + 0);//top
						indices.put(vertexIndexCounter + 4).put(vertexIndexCounter + 5).put(vertexIndexCounter + 2);
						indices.put(vertexIndexCounter + 3).put(vertexIndexCounter + 2).put(vertexIndexCounter + 6);//right
						indices.put(vertexIndexCounter + 2).put(vertexIndexCounter + 5).put(vertexIndexCounter + 6);
						indices.put(vertexIndexCounter + 0).put(vertexIndexCounter + 1).put(vertexIndexCounter + 7);//left
						indices.put(vertexIndexCounter + 0).put(vertexIndexCounter + 7).put(vertexIndexCounter + 4);
						indices.put(vertexIndexCounter + 4).put(vertexIndexCounter + 6).put(vertexIndexCounter + 5);//back
						indices.put(vertexIndexCounter + 4).put(vertexIndexCounter + 7).put(vertexIndexCounter + 6);
						indices.put(vertexIndexCounter + 1).put(vertexIndexCounter + 6).put(vertexIndexCounter + 7);//bottom
						indices.put(vertexIndexCounter + 1).put(vertexIndexCounter + 3).put(vertexIndexCounter + 6);
						
						
						
						//RenderNode geom = new RenderNode(mat, loc, BLOCK_SIZE, NodeManager.getNode((short)nVal)null);
						//nodesInChunk[i][j][k] = geom;
						vertexIndexCounter += 8;
					}

				}
			}
		}
		Material mat = getMaterial((short) 1);
		completeMesh.setBuffer(Type.Position, 3, vertices);
		completeMesh.setBuffer(Type.Normal, 3, normals);
		completeMesh.setBuffer(Type.Index, 3, indices);
		completeMesh.updateBound();
		Geometry geom = new Geometry("chunkMesh", completeMesh);
		geom.setMaterial(mat);
		worldNode.attachChild(geom);
		/*RenderMapChunk thisChunk = new RenderMapChunk(nodesInChunk, x, y, z);
		allChunks.put(pos, thisChunk);*/
	}
	
	private void calculateAndStoreSurfaceNormal (float x, float y, float z, FloatBuffer normals) {
		
	}
	
	private void preparatorChunkTest() {
		Position p1 = new Position(0, 0, 0, 0);
		Position p2 = new Position(1, 0, 0, 0);
		Position p3 = new Position(0, 0, 1, 0);
		Position p4 = new Position(1, 0, 1, 0);
		// Position p5 = new Position(-1,0,0,0);
		// Position p6 = new Position(0,0,-1,0);
		// Position p7 = new Position(-1,0,-1,0);

		getChunk(p1);
		System.out.println("SENT REQUEST");
		getChunk(p2);
		getChunk(p3);
		getChunk(p4);
		// getChunk(p5);
		// getChunk(p6);
		// getChunk(p7);
	}

	private void localChunkTest() {
		for(int i=0; i<1; i++) {
			for(int j=0; j<1; j++) {
				for(int k=0; k<1; k++) {
					Position pos = new Position(i, j, k, 0);
					boolean[][][] modifiedNodes = new boolean[16][16][16];
					for(boolean[][] m : modifiedNodes) {
						for(boolean[] n : m) {
							Arrays.fill(n, false);
						}
					}
					
					int[][][] nodeIds = new int[16][16][16];
					for(int[][] m : nodeIds) {
						for (int[] n : m) {
							Arrays.fill(n, 1);
						}
					}
					
					MapChunk chunk = new MapChunk (pos, nodeIds, modifiedNodes);
					renderChunk(chunk, pos);
				}
			}
		}
		GeometryBatchFactory.optimize(worldNode);
	}
	
	private void setupFlashlight () {
		spot = new SpotLight();
		spot.setSpotRange(300f);
		spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD);
		spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);
		spot.setColor(ColorRGBA.White.mult(3f));
		spot.setPosition(cam.getLocation());
		spot.setDirection(cam.getDirection());
		rootNode.addLight(spot);
	}
	
	private void setupSunlight () {
		sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White);
		sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
		rootNode.addLight(sun);
	}
	
	private void setupLamplight () {
		lamp = new PointLight();
		lamp.setColor(ColorRGBA.Yellow);
		lamp.setRadius(4f);
		lamp.setPosition(cam.getLocation());
		rootNode.addLight(lamp);
	}
	
	private void setupWorldNode () {
		worldNode = new Node("world");
		rootNode.attachChild(worldNode);
	}

	
	
	public Material getMaterial(short nVal) {
		Material mat = null;
		switch (nVal) {
		case 1:
			mat = new Material(assetManager,
					"Common/MatDefs/Light/Lighting.j3md");
			mat.setBoolean("UseMaterialColors", true);
			mat.setColor("Ambient", ColorRGBA.Green);
			mat.setColor("Diffuse", ColorRGBA.Green);
			
			/*mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", ColorRGBA.Green);
			*/
		}
		return mat;
	}
	
	private void moveWorld(float cx, float cy, float cz) {

		Vector2f transVector = new Vector2f(cam.getDirection().x,
				cam.getDirection().z);

		worldNode.setLocalTranslation(worldNode
				.getLocalTranslation()
				.addLocal(
						new Vector3f(-cz * transVector.x, 0f, -cz
								* transVector.y))
				.addLocal(-cx * transVector.y, 0f, cx * transVector.x)
				.addLocal(0f, -cy, 0f));
	}

	private void rotateCamera(float value, Vector3f axis) {

		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(ROTATION_SPEED * value, axis);

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
		
		spot.setDirection(cam.getDirection());
	}

	private void initKeyBindings() {
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));

		inputManager.addMapping("CAM_Left", new MouseAxisTrigger(
				MouseInput.AXIS_X, true), new KeyTrigger(KeyInput.KEY_LEFT));

		inputManager.addMapping("CAM_Right", new MouseAxisTrigger(
				MouseInput.AXIS_X, false), new KeyTrigger(KeyInput.KEY_RIGHT));

		inputManager.addMapping("CAM_Up", new MouseAxisTrigger(
				MouseInput.AXIS_Y, false), new KeyTrigger(KeyInput.KEY_UP));

		inputManager.addMapping("CAM_Down", new MouseAxisTrigger(
				MouseInput.AXIS_Y, true), new KeyTrigger(KeyInput.KEY_DOWN));

		inputManager.addListener(actionListener, "Jump");
		inputManager.addListener(actionListener, "Down");
		inputManager.addListener(actionListener, "Left");
		inputManager.addListener(actionListener, "Right");
		inputManager.addListener(actionListener, "Forward");
		inputManager.addListener(actionListener, "Back");
		inputManager.addListener(analogListener, "CAM_Left");
		inputManager.addListener(analogListener, "CAM_Right");
		inputManager.addListener(analogListener, "CAM_Up");
		inputManager.addListener(analogListener, "CAM_Down");
	}

	private AnalogListener analogListener = new AnalogListener() {

		public void onAnalog(String name, float value, float tpf) {
			if (name.equals("CAM_Left")) {
				rotateCamera(value, initialUpVec);
			} else if (name.equals("CAM_Right")) {
				rotateCamera(-value, initialUpVec);
			} else if (name.equals("CAM_Up")) {
				rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
			} else if (name.equals("CAM_Down")) {
				rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
			}
		}
	};
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals("Jump") && keyPressed/* && jumpSPEED == 0 */) {
				locChanges[1] = 2f;
			} else if (name.equals("Jump") && !keyPressed) {
				locChanges[1] = 0f;
			}

			if (name.equals("Down") && keyPressed) {
				locChanges[1] = -2f;
			} else if (name.equals("Down") && !keyPressed) {
				locChanges[1] = 0f;
			}

			if (name.equals("Left") && keyPressed) {
				locChanges[0] = SPEED;
			} else if (name.equals("Left") && !keyPressed
					&& locChanges[0] == SPEED) {
				locChanges[0] = 0;
			}

			if (name.equals("Right") && keyPressed) {
				locChanges[0] = -SPEED;
			} else if (name.equals("Right") && !keyPressed
					&& locChanges[0] == -SPEED) {
				locChanges[0] = 0;
			}

			if (name.equals("Forward") && keyPressed) {
				locChanges[2] = SPEED;
			} else if (name.equals("Forward") && !keyPressed
					&& locChanges[2] == SPEED) {
				locChanges[2] = 0;
			}

			if (name.equals("Back") && keyPressed) {
				locChanges[2] = -SPEED;
			} else if (name.equals("Back") && !keyPressed
					&& locChanges[2] == -SPEED) {
				locChanges[2] = 0;
			}
		}
	};
}
