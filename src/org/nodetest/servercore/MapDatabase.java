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
	private static ObjectContainer mapDb;
	private static ObjectContainer nodeDb;
	private static ObjectContainer entityDb;
	private static ObjectContainer banDb;

	public static void init(String name, boolean create)
			throws MapDatabaseException, MossWorldLoadException {
		if (!name.matches("^[A-Z[a-z[0-9[ ]]]]+$")) {
			throw new MossWorldLoadException(
					"World name contains invalid characters");
		}

		try {
			mapDb = Db4oEmbedded.openFile("worlds/" + name + "mapdb");
			entityDb = Db4oEmbedded.openFile("worlds/" + name + "entities");
			banDb = Db4oEmbedded.openFile("worlds/" + name + "nodes");
		} catch (Db4oIOException | DatabaseFileLockedException
				| IncompatibleFileFormatException | OldFormatException
				| DatabaseReadOnlyException e) {
			e.printStackTrace();
			throw new MossWorldLoadException("Database loading failed.");
		}

	}

	public static void close() {
		mapDb.commit();
		mapDb.close();
	}

	public static MapChunk getChunk(final Position pos, final boolean generate)
			throws ChunkNotFoundException, MapDatabaseException {
		// comment out slower crap
		/*
		 * List<MapChunkPacked> list = mapDb.query(new
		 * Predicate<MapChunkPacked>() { public boolean match(MapChunkPacked
		 * candidate) { return (candidate.x==x&&candidate.y==y&&candidate.z==z);
		 * } });
		 */
		Query query = mapDb.query();
		query.constrain(MapChunkPacked.class);
		query.descend("pos").constrain(pos);
		List<MapChunkPacked> list = query.execute();
		if (list.size() == 0){
			if(generate){
				return /* Mapgen.generate(pos);*/ null;
			}
		}
		if (list.size() > 1)
			throw new MapDatabaseException(
					MapDatabaseException.SEVERITY_CORRUPT_REPARABLE,
					"Duplicate chunk " + pos.toString() + ".");
		return list.get(0).unpack();

	}

	/**
	 * @param args
	 * @throws MossWorldLoadException
	 * @throws MapDatabaseException
	 */


	static void addMapChunk(MapChunkPacked mapChunkPacked) {
		mapDb.store(mapChunkPacked);

	}

	static void addMapChunk(MapChunk mapChunk) {
		mapDb.store(mapChunk.pack());

	}

}
