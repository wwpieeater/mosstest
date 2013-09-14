package net.mosstest.servercore;

public class MossNodeAddEvent extends MossRenderEvent {
	private int x, y, z; //position in chunk
	private MapNode definition;
	private Position parentChunkPosition;
	public MossNodeAddEvent (int x, int y, int z, Position parentChunkPosition, MapNode definition) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.definition = definition;
		this.parentChunkPosition = parentChunkPosition;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	public MapNode getDef() {
		return definition;
	}
	public Position getPosition () {
		return parentChunkPosition;
	}
}
