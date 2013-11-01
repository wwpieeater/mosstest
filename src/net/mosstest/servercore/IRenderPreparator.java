package net.mosstest.servercore;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.Position;

public interface IRenderPreparator {

	public void setRenderProcessor(RenderProcessor rend);

	public abstract MapChunk requestChunk(Position pos)
			throws MapGeneratorException, InterruptedException;

	public abstract void recvOutstandingChunk(Position pos, MapChunk chk);

	public abstract void shutdown();

	public abstract void start();
}