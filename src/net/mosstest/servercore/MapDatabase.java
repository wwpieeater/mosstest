package net.mosstest.servercore;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.MapGenerators;
import net.mosstest.scripting.Position;
import org.apache.log4j.Logger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

//import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

// TODO: Auto-generated Javadoc
/**
 * The Class MapDatabase.
 */
public class MapDatabase {
    private static final Logger logger = Logger.getLogger(MapDatabase.class);
    /**
     * The map.
     */
    DB map;

    /** The entities. */
	DB entities;
	
	/** The metadata. */
	DB metadata;
	
	/** The map heavies. */
	DB mapHeavies;
	
	/** The landclaims. */
	DB landclaims;
	
	/** The players. */
	DB players;
	
	/** The nodes. */
	public DB nodes;

	/**
	 * Instantiates a new map database.
	 *
	 * @param basedir the basedir
	 * @throws MapDatabaseException the map database exception
	 * @throws MossWorldLoadException the moss world load exception
	 */
	@SuppressWarnings("nls")
	public MapDatabase(File basedir) throws MapDatabaseException,
			MossWorldLoadException {
		File dbDir = new File(basedir, "db"); //$NON-NLS-1$
		dbDir.mkdirs();
		try {

			Options options = new Options();
			options.comparator(null);
			this.map = factory.open(new File(dbDir, "map"), options); //$NON-NLS-1$
			this.mapHeavies = factory.open(new File(dbDir, "mapHeavies"), //$NON-NLS-1$
					options);
			this.entities = factory.open(new File(dbDir, "entities"), options); //$NON-NLS-1$
			this.metadata = factory.open(new File(dbDir, "metadata"), options); //$NON-NLS-1$
			this.players = factory.open(new File(dbDir, "players"), options); //$NON-NLS-1$
			this.nodes = factory.open(new File(dbDir, "nodes"), options); //$NON-NLS-1$
        } catch (IOException e) {
            logger.error("IOException in database loading: " + e.toString());
            throw new MossWorldLoadException(Messages.getString("MapDatabase.ERR_DB_FAIL"), e); //$NON-NLS-1$
        }

    }

	/**
	 * Close.
	 *
	 * @throws MapDatabaseException the map database exception
	 */
	public void close() throws MapDatabaseException {
		try {
			this.map.close();
			this.entities.close();
			this.metadata.close();
		} catch (IOException e) {
            throw new MapDatabaseException("Database shutdown failed!", e,
                    MapDatabaseException.SEVERITY_UNKNOWN
                            | MapDatabaseException.SEVERITY_FATAL_TRANSIENT); //$NON-NLS-1$
        }
    }

	/**
	 * Gets the chunk.
	 *
	 * @param pos the pos
	 * @return the chunk
	 * @throws MapGeneratorException the map generator exception
	 */
	public MapChunk getChunk(final Position pos) throws MapGeneratorException {

		byte[] chunk = this.map.get(pos.toBytes());
		if (chunk == null) {
			MapChunk gen = MapGenerators.getDefaultMapgen().generateChunk(pos);
            this.map.put(pos.toBytes(), gen.writeLight(true));
            return gen;
        }
		try {
			return new MapChunk(pos, chunk, this);
		} catch (IOException e) {
			ExceptionHandler.registerException(e);
			return null;
		}

	}

	/**
	 * Adds the map chunk.
	 *
	 * @param pos the pos
	 * @param mapChunk the map chunk
	 */

	void addMapChunk(Position pos, MapChunk mapChunk) {
		this.map.put(pos.toBytes(), mapChunk.writeLight(true));

	}

	/**
	 * Gets the heavy.
	 *
	 * @param pos the pos
	 * @return the heavy
	 */
	public byte[] getHeavy(Position pos) {
		return this.mapHeavies.get(pos.toBytes());
	}

}
