package net.mosstest.servercore;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

public class MapDatabase {
	DB map;
	DB entities;
	DB metadata;
	DB mapHeavies;
	DB landclaims;
	DB players;

	@SuppressWarnings("nls")
	public MapDatabase(File basedir) throws MapDatabaseException,
			MossWorldLoadException {
		File dbDir=new File(basedir, "db");
		dbDir.mkdirs();
		try {
			Options options = new Options();
			options.createIfMissing(true);
			this.map = factory.open(new File(dbDir, "map.db"), options);
			this.mapHeavies = factory.open(new File(dbDir, "mapHeavies.db"),
					options);
			this.entities = factory.open(new File(dbDir, "entities.db"),
					options);
			this.metadata = factory.open(new File(dbDir, "metadata.db"),
					options);
			this.players = factory.open(new File(dbDir, "players.db"),
					options);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MossWorldLoadException("Database loading failed.");
		}

	}

	public void close() throws MapDatabaseException {
		try {
			this.map.close();
			this.entities.close();
			this.metadata.close();
		} catch (IOException e) {
			throw new MapDatabaseException(
					MapDatabaseException.SEVERITY_UNKNOWN
							| MapDatabaseException.SEVERITY_FATAL_TRANSIENT,
					"Database shutdown failed!"); //$NON-NLS-1$
		}
	}

	public MapChunk getChunk(final Position pos) {

		byte[] chunk = this.map.get(pos.toBytes());
		if (chunk == null)
			return MapGenerator.generateChunk(pos);
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
		this.map.put(pos.toBytes(), mapChunk.writeLight(true));

	}

	public byte[] getHeavy(Position pos) {
		return this.mapHeavies.get(pos.toBytes());
	}

}
