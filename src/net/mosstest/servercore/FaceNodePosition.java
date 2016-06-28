package net.mosstest.servercore;

import com.jme3.math.Vector3f;
import net.mosstest.scripting.NodePosition;

/**
 * Created by hexafraction on 7/16/14.
 */
public class FaceNodePosition extends NodePosition {
    Vector3f face;

    public FaceNodePosition(int realm, int x, int y, int z, byte xl, byte yl, byte zl, Vector3f face) {
        super(realm, x, y, z, xl, yl, zl);
        this.face = face;
    }
}
