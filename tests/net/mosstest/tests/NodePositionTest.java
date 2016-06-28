package net.mosstest.tests;

import net.mosstest.scripting.NodePosition;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

public class NodePositionTest {
    public static final int CHUNK_DIMENSION = 16;
    public static final int[] coords = {0, 1, -1, 16, -16, 67, -66, 269, -267,
            65601, -65601, Integer.MAX_VALUE, Integer.MIN_VALUE};

    @Test
    public void testHashCode() {
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords.length; j++) {
                for (int k = 0; k < coords.length; k++) {
                    for (byte x = 0; x < CHUNK_DIMENSION; x += 8) {
                        for (byte y = 0; y < CHUNK_DIMENSION; y += 8) {
                            for (byte z = 0; z < CHUNK_DIMENSION; z += 8) {
                                NodePosition pos1 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                NodePosition pos2 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                Assert.assertEquals(
                                        "Mismatched hashCodes for value-identical NodePosition objects",
                                        pos1.hashCode(), pos2.hashCode());
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testByteArrayReadWrite() {
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; i < coords.length; i++) {
                for (int k = 0; i < coords.length; i++) {
                    for (byte x = 0; x < CHUNK_DIMENSION; x+=8) {
                        for (byte y = 0; y < CHUNK_DIMENSION; y+=8) {
                            for (byte z = 0; z < CHUNK_DIMENSION; z+=4) {
                                NodePosition pos1 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                byte[] bytes = pos1.toBytes();
                                NodePosition pos2;
                                try {
                                    pos2 = new NodePosition(bytes);
                                    Assert.assertTrue(
                                            "NodePosition nmarshaled from byte[] fails equals() check with original NodePosition.",
                                            pos1.equals(pos2));
                                } catch (IOException e) {
                                    fail("IOException caught in unmarshaling NodePosition from byte[]");
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testEqualsObject() {
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords.length; j++) {
                for (int k = 0; k < coords.length; k++) {
                    for (byte x = 0; x < CHUNK_DIMENSION; x+=8) {
                        for (byte y = 0; y < CHUNK_DIMENSION; y+=8) {
                            for (byte z = 0; z < CHUNK_DIMENSION; z+=4) {
                                NodePosition pos1 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                NodePosition pos2 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                Assert.assertTrue(
                                        "Value-equal objects fail equals() check",
                                        pos1.equals(pos2));
                                Assert.assertTrue(
                                        "Value-equal objects fail equals() check",
                                        pos2.equals(pos1));
                                NodePosition pos3 = new NodePosition(
                                        0, coords[i] + 1, coords[j], coords[k],
                                        x, y, z);
                                NodePosition pos4 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                Assert.assertFalse(
                                        "Value-unequal objects erroneously pass equals() check for x",
                                        pos3.equals(pos4));
                                Assert.assertFalse(
                                        "Value-unequal objects erroneously pass equals() check for x",
                                        pos4.equals(pos3));
                                NodePosition pos5 = new NodePosition(0, coords[i],
                                        coords[j] + 1, coords[k], x, y, z);
                                NodePosition pos6 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                Assert.assertFalse(
                                        "Value-unequal objects erroneously pass equals() check for y",
                                        pos5.equals(pos6));
                                Assert.assertFalse(
                                        "Value-unequal objects erroneously pass equals() check for y",
                                        pos6.equals(pos5));
                                NodePosition pos7 = new NodePosition(0, coords[i],
                                        coords[j], coords[k] + 1, x, y, z);
                                NodePosition pos8 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, z);
                                Assert.assertFalse(
                                        "Value-unequal objects erroneously pass equals() check for z",
                                        pos7.equals(pos8));
                                Assert.assertFalse(
                                        "Value-unequal objects erroneously pass equals() check for z",
                                        pos8.equals(pos7));
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testToBytes() {
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords.length; j++) {
                for (int k = 0; k < coords.length; k++) {
                    for (byte x = 0; x < CHUNK_DIMENSION; x+=4) {
                        for (byte y = 0; y < CHUNK_DIMENSION; y+=8) {

                                NodePosition pos1 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, (byte)0);
                                NodePosition pos2 = new NodePosition(0, coords[i],
                                        coords[j], coords[k], x, y, (byte)0);
                                org.junit.Assert.assertArrayEquals(
                                        pos1.toBytes(), pos2.toBytes());

                        }
                    }
                }
            }
        }
    }


}
