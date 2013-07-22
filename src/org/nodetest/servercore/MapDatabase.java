package org.nodetest.servercore;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;

import org.fusesource.leveldbjni.internal.NativeDB;
import org.iq80.leveldb.*;

import static org.fusesource.leveldbjni.JniDBFactory.*;

import java.io.*;

public class MapDatabase {
	static DB map;
	static DB entities;
	static DB metadata;
	static DB mapHeavies;
	static DB landclaims;
	public static void init(String name, boolean create)
			throws MapDatabaseException, MossWorldLoadException {
		if (!name.matches("^[A-Z[a-z[0-9[ ]]]]+$")) {
			throw new MossWorldLoadException(
					"World name contains invalid characters");
		}

		try {
			Options options = new Options();
			options.createIfMissing(true);
			map = factory.open(new File("worlds/" + name + "/map.db"), options);
			mapHeavies=factory.open(new File("worlds/" + name + "/mapHeavies.db"), options);
			entities = factory.open(new File("worlds/" + name + "/entities.db"),
					options);
			metadata = factory.open(new File("worlds/" + name + "/metadata.db"),
					options);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MossWorldLoadException("Database loading failed.");
		}

	}

	public static void close() throws  MapDatabaseException {
		try {
			map.close();
			entities.close();
			metadata.close();
		} catch (IOException e) {
			throw new MapDatabaseException(MapDatabaseException.SEVERITY_UNKNOWN|MapDatabaseException.SEVERITY_FATAL_TRANSIENT, "Database shutdown failed!");
		}
	}

	public static MapChunk getChunk(final Position pos, final boolean generate)
			{
		
		byte[] chunk=map.get(pos.toBytes());
		if(chunk==null) return MapGenerator.generateChunk(pos);
		try {
			return new MapChunk(pos, chunk);
		} catch (IOException e) {
			ExceptionHandler.registerException(e);
			return null;
		}

	}

	/**
	 * @param args
	 * @throws MossWorldLoadException
	 * @throws MapDatabaseException
	 */

	static void addMapChunk(MapChunk mapChunk) {
		map.put(mapChunk.pos.toBytes(),mapChunk.writeLight(true));

	}

	public static byte[] getHeavy(Position pos) {
		return mapHeavies.get(pos.toBytes());
	}



}
