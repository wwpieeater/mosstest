package com.mosstest.servercore;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class MapChunk {
	@PrimaryKey
	private long chunkId;
	private String chunkLightStorage;
	private boolean hasHeavies;
	
}
