package net.mosstest.servercore;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Timer;

import org.fusesource.leveldbjni.internal.NativeDB;
import org.iq80.leveldb.*;

import static org.fusesource.leveldbjni.JniDBFactory.*;

import java.io.*;

public class MapDatabase {
	DB map;
	DB entities;
	DB metadata;
	DB mapHeavies;
	DB landclaims;
	public MapDatabase(String name, boolean create)
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

	public void close() throws  MapDatabaseException {
		try {
			map.close();
			entities.close();
			metadata.close();
		} catch (IOException e) {
			throw new MapDatabaseException(MapDatabaseException.SEVERITY_UNKNOWN|MapDatabaseException.SEVERITY_FATAL_TRANSIENT, "Database shutdown failed!");
		}
	}

	public MapChunk getChunk(final Position pos)
			{
		
		byte[] chunk=map.get(pos.toBytes());
		if(chunk==null) return MapGenerator.generateChunk(pos);
		try {
			return new MapChunk(pos, chunk, this);
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

	void addMapChunk(Position pos, MapChunk mapChunk) {
		map.put(pos.toBytes(),mapChunk.writeLight(true));

	}

	public byte[] getHeavy(Position pos) {
		return mapHeavies.get(pos.toBytes());
	}



}
