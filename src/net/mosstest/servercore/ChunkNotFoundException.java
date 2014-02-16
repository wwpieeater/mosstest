package net.mosstest.servercore;

import net.mosstest.scripting.Position;

// TODO: Auto-generated Javadoc
/**
 * The Class ChunkNotFoundException.
 */
public class ChunkNotFoundException extends Exception {

/** The z. */
long x, y, z;

/**
 * Instantiates a new chunk not found exception.
 *
 * @param x the x
 * @param y the y
 * @param z the z
 */
public ChunkNotFoundException(long x, long y, long z) {
	this.x = x;
	this.y = y;
	this.z = z;
}

/**
 * Instantiates a new chunk not found exception.
 *
 * @param pos the pos
 */
public ChunkNotFoundException(Position pos) {
	// TODO Auto-generated constructor stub
}

}
