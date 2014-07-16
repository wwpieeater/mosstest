package net.mosstest.servercore;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;

import com.jme3.material.RenderState;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;
import jme3tools.optimize.GeometryBatchFactory;
import net.mosstest.scripting.*;
import net.mosstest.servercore.FaceRenderer.Face;

import org.apache.log4j.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;


public class RenderProcessor extends SimpleApplication {

    static Logger logger = Logger.getLogger(RenderProcessor.class);
    private final float SPEED = 3f;
    private final float NODE_SIZE = 20f;
    private final float CHUNK_SIZE = 16 * NODE_SIZE;
    private final float ROTATION_SPEED = 1f;
    private final double NODE_OFFSET_FROM_CENTER = 8 * NODE_SIZE;
    private final double CHUNK_OFFSET = 8 * NODE_SIZE;
    private float[] locChanges = {0, 0, 0};
    private double lastTime;
    private boolean invertY = false;
    private Object renderKey;
    private Node worldNode;
    private SpotLight spot;
    private PointLight lamp;
    private DirectionalLight sun;
    private ActivityListener activityListener;
    private RotationListener rotationListener;
    //private HashMap<Position, RenderMapChunk> allChunks = new HashMap<Position, RenderMapChunk>();
    public INodeManager nManager;
    public IRenderPreparator rPreparator;
    public Player player;
    public ArrayBlockingQueue<MossRenderEvent> renderEventQueue = new ArrayBlockingQueue<>(
            24000, false);

    public static RenderProcessor init(INodeManager manager, IRenderPreparator preparator) {
        java.util.logging.Logger.getLogger("").setLevel(Level.WARNING);
        RenderProcessor app = new RenderProcessor();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800, 600);
        settings.setSamples(2);
        settings.setFullscreen(false);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.initNodeManager(manager);
        app.initPreparator(preparator);
        app.initSecurityLock();
        app.start();
        return app;
    }

    private void initNodeManager(INodeManager manager) {
        nManager = manager;
    }

    private void initPreparator(IRenderPreparator prep) {
        rPreparator = prep;
        rPreparator.setRenderProcessor(this);
        rPreparator.start();
    }

    private void initSecurityLock() {
        renderKey = new Object();
    }

    @Override
    public void simpleInitApp() {
        lastTime = 0;
        //acquireLock();
        setupWorldNode();
        setupFlashlight();
        setupSunlight();
        setupLamplight();
        setupPlayer();
        assetManager.registerLocator("scripts", LocalAssetLocator.class);
        setupHud();
        flyCam.setEnabled(false);
        setupListeners(cam.getUp().clone());
        initKeyBindings();
        preparatorChunkTest();
        //blankChunkTest();
    }

    private void setupHud() {
        Picture pic = new Picture("Crosshair");
        pic.setImage(assetManager, "builtins/crosshair.png", true);
        pic.setWidth(32);
        pic.setHeight(32);
        pic.setPosition(settings.getWidth() / 2 - 16, settings.getHeight() / 2 - 16);
        guiNode.attachChild(pic);

    }

    @Override
    public void simpleUpdate(float tpf) {
        if (lastTime + 10 < System.currentTimeMillis()) {
            move(locChanges[0], locChanges[1], locChanges[2]);
            lastTime = System.currentTimeMillis();
        }
        inputManager.setCursorVisible(false);
        MossRenderEvent myEvent = renderEventQueue.poll();
        if (myEvent instanceof MossRenderStopEvent) {
            logger.info("The renderer thread is shutting down.");
        } else if (myEvent instanceof MossRenderChunkEvent) {
            renderChunk(((MossRenderChunkEvent) myEvent).getChk(),
                    ((MossRenderChunkEvent) myEvent).getPos());
            GeometryBatchFactory.optimize(worldNode);
        }

        drawMouseOver();


    }

    private Spatial mouseoverSpatial;

    private void drawMouseOver() {
        if (mouseoverSpatial != null)
            worldNode.detachChild(mouseoverSpatial);
        NodePosition rough = new NodePosition(0, player.xchk,
                player.ychk,
                player.zchk,
                (byte) 0, (byte) 0, (byte) 0);
        Vector3f origin = new Vector3f((float) player.xoffset, (float) player.yoffset, (float) player.zoffset);
        NodePosition np = raycast(rough, origin, cam.getDirection(), 8);
        if(np==null) return;
        Mesh completeMesh = new Mesh();
        synchronized (FaceRenderer.class) {
            FaceRenderer.initialize();
        }
        Position pos = np.chunk;
        float x = (float) ((pos.x + (CHUNK_SIZE * pos.x)) - NODE_OFFSET_FROM_CENTER + CHUNK_OFFSET + (np.xl * NODE_SIZE));
        float z = (float) ((pos.y - (CHUNK_SIZE * pos.y)) - NODE_OFFSET_FROM_CENTER + CHUNK_OFFSET + (np.yl * NODE_SIZE));
        float y = (float) ((pos.z + (CHUNK_SIZE * pos.z)) - NODE_OFFSET_FROM_CENTER + CHUNK_OFFSET + (np.zl * NODE_SIZE));
        for (Face face : Face.values()) {
            FaceRenderer.populateBuffers(face, x, y, z, NODE_SIZE);


        }
        FloatBuffer vertices = FaceRenderer.getVertices();
        FloatBuffer tex = FaceRenderer.getTextureCoordinates();
        FloatBuffer normals = FaceRenderer.getNormals();
        IntBuffer indices = FaceRenderer.getIndices();
        completeMesh.setBuffer(Type.Position, 3, vertices);
        completeMesh.setBuffer(Type.Normal, 3, normals);
        completeMesh.setBuffer(Type.Index, 3, indices);
        completeMesh.setBuffer(Type.TexCoord, 2, tex);
        completeMesh.setLineWidth(8);
        completeMesh.updateBound();
        Geometry geom = new Geometry("mouseoverMesh", completeMesh);
        Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        mat.getAdditionalRenderState().setWireframe(true);
        geom.setMaterial(mat);
        mouseoverSpatial = geom;
        worldNode.attachChild(geom);
        System.out.println("Attached mouseover mesh at " + np);
    }

    //
    public void renderChunk(MapChunk chk, Position pos) {
        synchronized (FaceRenderer.class) {
            Mesh completeMesh = new Mesh();
            FaceRenderer.initialize();
            //RenderNode[][][] renderNodes = new RenderNode[16][16][16];
            for (byte i = 0; i < 16; i++) {
                for (byte j = 0; j < 16; j++) {
                    for (byte k = 0; k < 16; k++) {
                        int[][][] nodes = chk.getNodes();
                        if (isNodeVisible(nodes, i, j, k)) {
                            int nVal = chk.getNodeId(i, j, k);
                            //MapNode node = nManager.getNode((short) nVal);
                            //Material mat = getMaterial((short) nVal);
                            if (nVal == 0) {
                            } else {
                                //z and y are switched on purpose.
                                float x = (float) ((pos.x + (CHUNK_SIZE * pos.x)) - NODE_OFFSET_FROM_CENTER + CHUNK_OFFSET + (i * NODE_SIZE));
                                float z = (float) ((pos.y - (CHUNK_SIZE * pos.y)) - NODE_OFFSET_FROM_CENTER + CHUNK_OFFSET + (j * NODE_SIZE));
                                float y = (float) ((pos.z + (CHUNK_SIZE * pos.z)) - NODE_OFFSET_FROM_CENTER + CHUNK_OFFSET + (k * NODE_SIZE));


                                for (Face face : Face.values()) {
                                    if (FaceRenderer.isFaceVisible(face, nodes, i, j, k)) {
                                        FaceRenderer.populateBuffers(face, x, y, z, NODE_SIZE);
                                    }
                                }
                                //RenderNode geom = new RenderNode(mat, loc, NODE_SIZE, NodeManager.getNode((short)nVal)null);
                                //renderNodes[i][j][k] = geom;
                            }
                        }
                    }
                }
            }
            FloatBuffer vertices = FaceRenderer.getVertices();
            FloatBuffer tex = FaceRenderer.getTextureCoordinates();
            FloatBuffer normals = FaceRenderer.getNormals();
            IntBuffer indices = FaceRenderer.getIndices();
            completeMesh.setBuffer(Type.Position, 3, vertices);
            completeMesh.setBuffer(Type.Normal, 3, normals);
            completeMesh.setBuffer(Type.Index, 3, indices);
            completeMesh.setBuffer(Type.TexCoord, 2, tex);
            completeMesh.updateBound();
            Geometry geom = new Geometry("chunkMesh", completeMesh);
            Material mat = getMaterial((short) 1);
            geom.setMaterial(mat);
            geom.setQueueBucket(RenderQueue.Bucket.Transparent);
            worldNode.attachChild(geom);
            //RenderMapChunk currentChunk = new RenderMapChunk(renderNodes);
            //allChunks.put(pos, currentChunk);
        }
    }

    private Material getMaterial(short nodeType) {
        Material mat = null;
        switch (nodeType) {
            case 1:
                mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                Texture tx = assetManager.loadTexture("default/grass.png");
                //Texture tx = assetManager.loadTexture("default/item_torch.png");
                tx.setMagFilter(Texture.MagFilter.Nearest);
                mat.setTexture("DiffuseMap", tx);
                mat.setBoolean("UseAlpha", true);
                mat.getAdditionalRenderState().setAlphaTest(true);
                mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        return mat;
    }

    public MapChunk getChunk(Position pos) {
        MapChunk prospective = null;
        if (curChunk == null || !(pos.equals(curPos))) {
            synchronized (cacheLock) {
                try {
                    prospective = rPreparator.requestChunk(pos);
                } catch (MapGeneratorException e) {
                    logger.warn("MapGeneratorException getting a chunk: " + e.getLocalizedMessage());
                } catch (InterruptedException e) {
                    logger.warn("InterruptedException getting a chunk: " + e.getLocalizedMessage());
                }
                if (prospective != null) this.curChunk = prospective;
                this.curPos = pos;
                return prospective;
            }
        } else return curChunk;
    }

    private MapChunk curChunk;
    private Position curPos;
    private Object cacheLock = new Object();

    public void getAndRenderChunk(Position pos) {
        MapChunk maybe = null;
        try {
            maybe = rPreparator.requestChunk(pos);
        } catch (MapGeneratorException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (maybe != null) {
            renderChunk(maybe, pos);
        }
    }

    private void preparatorChunkTest() {
        Position p1 = new Position(0, 0, 0, 0);
        Position p2 = new Position(1, 0, 0, 0);
        Position p3 = new Position(0, 1, 0, 0);
        Position p4 = new Position(1, 1, 0, 0);
        Position p5 = new Position(-1, 0, 0, 0);
        Position p6 = new Position(0, -1, 0, 0);
        Position p7 = new Position(-1, -1, 0, 0);

        getAndRenderChunk(p1);
        getAndRenderChunk(p2);
        getAndRenderChunk(p3);
        getAndRenderChunk(p4);
        getAndRenderChunk(p5);
        getAndRenderChunk(p6);
        getAndRenderChunk(p7);
    }

    private void blankChunkTest() {
        Position p1 = new Position(0, 0, 0, 0);
        Position p2 = new Position(1, 1, 1, 0);

        int[][][] n1 = new int[16][16][16];
        int[][][] n2 = new int[16][16][16];
        for (int i = 0; i < n1.length; i++) {
            for (int j = 0; j < n1[i].length; j++) {
                for (int k = 0; k < n1[i][j].length; k++) {
                    n1[i][j][k] = 1;
                    n2[i][j][k] = 1;
                }
            }
        }

        MapChunk c1 = new MapChunk(p1, n1);
        MapChunk c2 = new MapChunk(p2, n2);

        MossRenderEvent e1 = new MossRenderChunkEvent(c1);
        MossRenderEvent e2 = new MossRenderChunkEvent(c2);

        renderEventQueue.add(e1);
        renderEventQueue.add(e2);
        //renderChunk(c2, p2);

    }

    private void setupFlashlight() {
        spot = new SpotLight();
        spot.setSpotRange(300f);
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD);
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);
        spot.setColor(ColorRGBA.White.mult(3f));
        spot.setPosition(cam.getLocation());
        spot.setDirection(cam.getDirection());
        rootNode.addLight(spot);
    }

    private void setupSunlight() {
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        rootNode.addLight(sun);
    }

    private void setupLamplight() {
        lamp = new PointLight();
        lamp.setColor(ColorRGBA.Yellow);
        lamp.setRadius(4f);
        lamp.setPosition(cam.getLocation());
        rootNode.addLight(lamp);
    }

    private void setupWorldNode() {
        worldNode = new Node("world");
        rootNode.attachChild(worldNode);
    }

    private void setupPlayer() {
        player = new Player("Test Guy");
        player.setPositionOffsets(0, 5, 0);
        player.setChunkPosition(0, 0, 0);
        cam.setLocation(new Vector3f(0, 0, 0));
    }

    private void move(float cx, float cy, float cz) {

        Vector2f transVector = new Vector2f(cam.getDirection().x,
                cam.getDirection().z).normalizeLocal();

        worldNode.setLocalTranslation(worldNode
                .getLocalTranslation()
                .addLocal(
                        new Vector3f(-cz * transVector.x, 0f, -cz
                                * transVector.y)
                )
                .addLocal(-cx * transVector.y, 0f, cx * transVector.x)
                .addLocal(0f, -cy, 0f));

        double xpos = -(worldNode.getLocalTranslation().x);
        double ypos = -(worldNode.getLocalTranslation().y);
        double zpos = -(worldNode.getLocalTranslation().z);
        int xchk = (int) Math.floor(xpos / (CHUNK_SIZE));
        int ychk = (int) Math.floor(ypos / (CHUNK_SIZE));
        int zchk = (int) Math.floor(zpos / (CHUNK_SIZE));
        double xoffset = (xpos % CHUNK_SIZE) / NODE_SIZE;
        double yoffset = (ypos % CHUNK_SIZE) / NODE_SIZE;
        double zoffset = (zpos % CHUNK_SIZE) / NODE_SIZE;

        synchronized (player.antiCheatDataLock) {
            player.setChunkPosition(xchk, ychk, zchk);
            player.setPositionOffsets(xoffset, yoffset, zoffset);
        }
    }

    private void setupListeners(Vector3f initialUpVec) {
        rotationListener = new RotationListener(initialUpVec, invertY, cam, ROTATION_SPEED);
        activityListener = new ActivityListener(locChanges, SPEED);
    }

    private boolean isNodeVisible(int[][][] chunk, int i, int j, int k) {
        if (i == 0 || j == 0 || k == 0 || i == chunk.length - 1 || j == chunk[0].length - 1 || k == chunk[0][0].length - 1) {
            return true;
        }
        return (chunk[i + 1][j][k] == 0 || chunk[i][j + 1][k] == 0 || chunk[i][j][k + 1] == 0 ||
                chunk[i - 1][j][k] == 0 || chunk[i][j - 1][k] == 0 || chunk[i][j][k - 1] == 0);
    }

    private void acquireLock() {
        MosstestSecurityManager.instance.lock(renderKey, null);
    }

    private void releaseLock() {
        MosstestSecurityManager.instance.unlock(renderKey);
    }

    private void initKeyBindings() {
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("TestFeature", new KeyTrigger(KeyInput.KEY_P));

        inputManager.addMapping("CAM_Left", new MouseAxisTrigger(
                MouseInput.AXIS_X, true), new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping("CAM_Right", new MouseAxisTrigger(
                MouseInput.AXIS_X, false), new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping("CAM_Up", new MouseAxisTrigger(
                MouseInput.AXIS_Y, false), new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping("CAM_Down", new MouseAxisTrigger(
                MouseInput.AXIS_Y, true), new KeyTrigger(KeyInput.KEY_DOWN));

        inputManager.addListener(activityListener, "Jump");
        inputManager.addListener(activityListener, "Down");
        inputManager.addListener(activityListener, "Left");
        inputManager.addListener(activityListener, "Right");
        inputManager.addListener(activityListener, "Forward");
        inputManager.addListener(activityListener, "Back");
        inputManager.addListener(rotationListener, "CAM_Left");
        inputManager.addListener(rotationListener, "CAM_Right");
        inputManager.addListener(rotationListener, "CAM_Up");
        inputManager.addListener(rotationListener, "CAM_Down");
        inputManager.addListener(activityListener, "TestFeature");
    }

    private double intbound(double s, double ds) {
        // Find the smallest positive t such that s+t*ds is an integer.
        if (ds < 0) {
            return intbound(-s, -ds);
        } else {
            s = raycasterMod(s, 1);
            // problem is now s+t*ds = 1
            return (1 - s) / ds;
        }
    }


    double raycasterMod(double value, double modulus) {
        return ((value % modulus) + modulus) % modulus;
    }

    FaceNodePosition raycast(NodePosition worldPos, Vector3f traceOrigin, Vector3f camDirection, int radius) {
        // From "A Fast Voxel Traversal Algorithm for Ray Tracing"
        // by John Amanatides and Andrew Woo, 1987
        // <http://www.cse.yorku.ca/~amana/research/grid.pdf>
        // <http://citeseer.ist.psu.edu/viewdoc/summary?doi=10.1.1.42.3443>
        // Extensions to the described algorithm:
        //   • Imposed a distance limit.
        //   • The face passed through to reach the current cube is provided to
        //     the callback.

        // The foundation of this algorithm is a parameterized representation of
        // the provided ray,
        //                    origin + t * direction,
        // except that t is not actually stored; rather, at any given point in the
        // traversal, we keep track of the *greater* t values which we would have
        // if we took a step sufficient to cross a cube boundary along that axis
        // (i.e. change the integer part of the coordinate) in the variables
        // tMaxX, tMaxY, and tMaxZ.

        // Cube containing origin point.
        double x = Math.floor(traceOrigin.getX());
        double y = Math.floor(traceOrigin.getY());
        double z = Math.floor(traceOrigin.getZ());
        Vector3f direction = camDirection.normalize();
        // Break out direction vector.
        double dx = direction.getX();
        double dy = direction.getY();
        double dz = direction.getZ();
        // Direction to increment x,y,z when stepping.
        double stepX = Math.signum(dx);
        double stepY = Math.signum(dy);
        double stepZ = Math.signum(dz);
        // See description above. The initial values depend on the fractional
        // part of the origin.
        double tMaxX = intbound(x, dx);
        double tMaxY = intbound(y, dy);
        double tMaxZ = intbound(z, dz);
        // The change in t when taking a step (always positive).
        double tDeltaX = stepX / dx;
        double tDeltaY = stepY / dy;
        double tDeltaZ = stepZ / dz;

        Vector3f face = new Vector3f();
        // Avoids an infinite loop.
        if (dx == 0 && dy == 0 && dz == 0)
            throw new IllegalArgumentException("Raycast in zero direction!");

        // Rescale from units of 1 cube-edge to units of 'direction' so we can
        // compare with 't'.
        radius /= Math.sqrt(dx * dx + dy * dy + dz * dz);

        for (int i = 0; i < 20; i++) {

            // Invoke the callback, unless we are not *yet* within the bounds of the
            // world.

            if (nodeAcceptable(worldPos, x, y, z, face)) {
                long combinedX = ((long) x) + (worldPos.chunk.x * 16 + worldPos.xl);
                long combinedY = ((long) y) + (worldPos.chunk.y * 16 + worldPos.yl);
                long combinedZ = ((long) z) + (worldPos.chunk.z * 16 + worldPos.zl);
                return new FaceNodePosition(worldPos.chunk.realm,
                        (int) (combinedX / 16),
                        (int) (combinedY / 16),
                        (int) (combinedZ / 16),
                        (byte) (combinedX % 16),
                        (byte) (combinedY % 16),
                        (byte) (combinedZ % 16),
                        face);
            }

            // tMaxX stores the t-value at which we cross a cube boundary along the
            // X axis, and similarly for Y and Z. Therefore, choosing the least tMax
            // chooses the closest cube boundary. Only the first case of the four
            // has been commented in detail.
            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    if (tMaxX > radius) break;
                    // Update which cube we are now in.
                    x += stepX;
                    // Adjust tMaxX to the next X-oriented boundary crossing.
                    tMaxX += tDeltaX;
                    // Record the normal vector of the cube face we entered.
                    face.setX((float) -stepX);
                    face.setY(0);
                    face.setZ(0);
                } else {
                    if (tMaxZ > radius) break;
                    z += stepZ;
                    tMaxZ += tDeltaZ;
                    face.setX(0);
                    face.setY(0);
                    face.setZ((float) -stepZ);
                }
            } else {
                if (tMaxY < tMaxZ) {
                    if (tMaxY > radius) break;
                    y += stepY;
                    tMaxY += tDeltaY;
                    face.setX(0);
                    face.setY((float) -stepY);
                    face.setZ(0);
                } else {
                    // Identical to the second case, repeated for simplicity in
                    // the conditionals.
                    if (tMaxZ > radius) break;
                    z += stepZ;
                    tMaxZ += tDeltaZ;
                    face.setX(0);
                    face.setY(0);
                    face.setZ((float) -stepZ);
                }
            }
        }
        return null;
    }


    private boolean nodeAcceptable(NodePosition worldPos, double x, double y, double z, Vector3f face) {
        int xC = worldPos.chunk.x + (int) (x / 16);
        int yC = worldPos.chunk.y + (int) (y / 16);
        int zC = worldPos.chunk.z + (int) (z / 16);
        MapChunk chk = getChunk(new Position(worldPos.chunk.realm, xC, yC, zC));
        if (chk==null) return false;
        return chk.getNodeId((byte) (Math.abs(x % 16)), (byte) (Math.abs(y % 16)), (byte) Math.abs(z % 16)) != 0;
    }
}