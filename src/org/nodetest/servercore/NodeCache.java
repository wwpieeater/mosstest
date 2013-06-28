package org.nodetest.servercore;

import java.io.IOException;

import com.spaceprogram.kittycache.KittyCache;

public class NodeCache {
	static KittyCache<Position, MapChunk> chunkData = new KittyCache<>(
			EngineSettings.getInt("maxCachedChunks", 256));

	public static MapChunk getChunk(Position pos, boolean generate)  {
		MapChunk ourChunk=null;
		try {
			ourChunk = chunkData.get(pos);
		} catch (Exception e) {
			ourChunk = MapDatabase.getChunk(pos, generate);
		}
		if(ourChunk==null){
			ourChunk = MapDatabase.getChunk(pos, generate);
		}
		return ourChunk;
		
	}
	public static void setChunk(Position pos, MapChunk chunk){
		chunkData.put(pos, chunk, 3600);
		MapDatabase.addMapChunk(chunk);
	}
}
