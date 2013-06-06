package org.nodetest.servercore;

import java.io.File;
import java.util.List;
import java.util.Timer;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

import java.util.UUID;

public class MapDatabase {
ObjectContainer mapDb;
	public MapDatabase(String name, boolean create) throws MapDatabaseException, MossWorldLoadException {
		if(!name.matches("^[A-Z[a-z[0-9[ ]]]]+$")){
			throw new MossWorldLoadException("World name contains invalid characters");
		}
		
		try {
			//mapDb=Db4oEmbedded.openFile("worlds/"+name+"mapdb.db4o");
			mapDb=Db4oEmbedded.openFile("testDb6.db");
		} catch (Db4oIOException | DatabaseFileLockedException
				| IncompatibleFileFormatException | OldFormatException
				| DatabaseReadOnlyException e) {
			e.printStackTrace();
			throw new MossWorldLoadException("Database loading failed.");
		}
		
		
	}

	public void close() {
		mapDb.commit();
		mapDb.close();
	}
	
	public MapChunk getChunk(final long x, final long y, final long z) throws ChunkNotFoundException, MapDatabaseException{
		//comment out slower crap
		/*		List<MapChunkPacked> list = mapDb.query(new Predicate<MapChunkPacked>() {
			public boolean match(MapChunkPacked candidate) {
				return (candidate.x==x&&candidate.y==y&&candidate.z==z);
			}
		});
 
 */
		Query query=mapDb.query();
		query.constrain(MapChunkPacked.class);
		query.descend("x").constrain(new Long(x));
		query.descend("y").constrain(new Long(y));
		query.descend("z").constrain(new Long(z));
		List<MapChunkPacked> list=query.execute();
		if(list.size()==0) throw new ChunkNotFoundException(x, y, z);
		if(list.size()>1) throw new MapDatabaseException(MapDatabaseException.SEVERITY_CORRUPT_REPARABLE, "Duplicate chunk "+x+", "+y+", "+z+".");
		return list.get(0).unpack();
		
	}

	/**
	 * @param args
	 * @throws MossWorldLoadException 
	 * @throws MapDatabaseException 
	 */
	public static void main(String[] args) throws MapDatabaseException, MossWorldLoadException {
		MapDatabase ourDb=new MapDatabase("test2", true);
		for(int i=1; i<=320; i++){
			//ourDb.add(new MapChunkPacked(i, i, i, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
			System.out.println(i);
		}
		System.out.println("begin prefetch");
		try {
			ourDb.getChunk(1, 1, 1);
		} catch (ChunkNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("end prefetch");
		final long startTime = System.currentTimeMillis();
		int ed=0;
		for(int i=1; i<=64000; i++){
			//System.out.println(i);
			try {
				ourDb.getChunk(i, i, i);
			} catch (ChunkNotFoundException e) {
ed++;				
			}
		}System.out.println("Total execution time: " + (System.currentTimeMillis() - startTime) );
		System.out.println(ed);

	}

	private void add(MapChunkPacked mapChunkPacked) {
		mapDb.store(mapChunkPacked);
		
	}

}
