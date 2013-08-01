package org.nodetest.servercore;

public class ChunkNotFoundException extends Exception {
long x, y, z;

public ChunkNotFoundException(long x, long y, long z) {
	this.x = x;
	this.y = y;
	this.z = z;
}

public ChunkNotFoundException(Position pos) {
	// TODO Auto-generated constructor stub
}

}
