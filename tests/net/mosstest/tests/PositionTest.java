package net.mosstest.tests;

import net.mosstest.scripting.Position;

import org.junit.Assert;
import org.junit.Test;

public class PositionTest {

	public static final int[] coords = { 0, 1, -1, 16, -16, 67, -66, 269, -267,
			65601, -65601, Integer.MAX_VALUE, Integer.MIN_VALUE };

	@Test
	public void testHashCode() {
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords.length; j++) {
				for (int k = 0; k < coords.length; k++) {
					Position pos1 = new Position(coords[i], coords[j],
							coords[k], 0);
					Position pos2 = new Position(coords[i], coords[j],
							coords[k], 0);
					Assert.assertEquals(
							"Mismatched hashCodes for value-identical Position objects",
							pos1.hashCode(), pos2.hashCode());
				}
			}
		}
	}

	@Test
	public void testByteArrayReadWrite() {
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords.length; j++) {
				for (int k = 0; k < coords.length; k++) {
					Position pos1 = new Position(coords[i], coords[j],
							coords[k], 0);
					byte[] bytes = pos1.toBytes();
					Position pos2;

					pos2 = new Position(bytes);

					Assert.assertTrue(
							"Position unmarshaled from byte[] fails equals() check with original position.",
							pos1.equals(pos2));

				}
			}
		}
	}

	@Test
	public void testEqualsObject() {
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords.length; j++) {
				for (int k = 0; k < coords.length; k++) {
					Position pos1 = new Position(coords[i], coords[j],
							coords[k], 0);
					Position pos2 = new Position(coords[i], coords[j],
							coords[k], 0);
					Assert.assertTrue(
							"Value-equal objects fail equals() check",
							pos1.equals(pos2));
					Assert.assertTrue(
							"Value-equal objects fail equals() check",
							pos2.equals(pos1));
					Position pos3 = new Position(coords[i] + 1, coords[j],
							coords[k], 0);
					Position pos4 = new Position(coords[i], coords[j],
							coords[k], 0);
					Assert.assertFalse(
							"Value-unequal objects erroneously pass equals() check for x",
							pos3.equals(pos4));
					Assert.assertFalse(
							"Value-unequal objects erroneously pass equals() check for x",
							pos4.equals(pos3));
					Position pos5 = new Position(coords[i], coords[j] + 1,
							coords[k], 0);
					Position pos6 = new Position(coords[i], coords[j],
							coords[k], 0);
					Assert.assertFalse(
							"Value-unequal objects erroneously pass equals() check for y",
							pos5.equals(pos6));
					Assert.assertFalse(
							"Value-unequal objects erroneously pass equals() check for y",
							pos6.equals(pos5));
					Position pos7 = new Position(coords[i], coords[j],
							coords[k] + 1, 0);
					Position pos8 = new Position(coords[i], coords[j],
							coords[k], 0);
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

	@Test
	public void testToBytes() {
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords.length; j++) {
				for (int k = 0; k < coords.length; k++) {
					Position pos1 = new Position(coords[i], coords[j],
							coords[k], 0);
					Position pos2 = new Position(coords[i], coords[j],
							coords[k], 0);
					org.junit.Assert.assertArrayEquals(pos1.toBytes(),
							pos2.toBytes());
				}
			}
		}
	}

	@Test
	public void testGetX() {
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords.length; j++) {
				for (int k = 0; k < coords.length; k++) {
					Position pos1 = new Position(coords[i], coords[j],
							coords[k], 0);
					Assert.assertEquals(
							"Mismatched x value compared to constructor args",
							pos1.getX(), coords[i]);
				}
			}
		}
	}

	@Test
	public void testGetY() {
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords.length; j++) {
				for (int k = 0; k < coords.length; k++) {
					Position pos1 = new Position(coords[i], coords[j],
							coords[k], 0);
					Assert.assertEquals(
							"Mismatched y value compared to constructor args",
							pos1.getY(), coords[j]);
				}
			}
		}
	}

	@Test
	public void testGetZ() {
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords.length; j++) {
				for (int k = 0; k < coords.length; k++) {
					Position pos1 = new Position(coords[i], coords[j],
							coords[k], 0);
					Assert.assertEquals(
							"Mismatched z value compared to constructor args",
							pos1.getZ(), coords[k]);
				}
			}
		}
	}

}
