package net.mosstest.scripting;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import net.mosstest.servercore.serialization.IByteArrayWriteable;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

// TODO: Auto-generated Javadoc
/**
 * The Class ScriptableDatabase.
 */
public class ScriptableDatabase {

	/** The base dir. */
	File baseDir;
	
	/**
	 * Instantiates a new scriptable database.
	 *
	 * @param baseDir the base dir
	 */
	public ScriptableDatabase(File baseDir) {
		this.baseDir = baseDir;

	}

	/**
	 * Gets the db.
	 *
	 * @param name the name
	 * @return the db
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public DBase getDb(String name) throws IOException {
		if (!name.matches("[a-zA-Z]{1,32}")) { //$NON-NLS-1$
			throw new IllegalArgumentException(Messages.getString("ScriptableDatabase.DB_NAME_INVALID")); //$NON-NLS-1$
		}
		Options options = new Options();
		options.createIfMissing(true);
		return new DBase(factory.open(new File(this.baseDir, "sc_" + name //$NON-NLS-1$
				+ ".db"), options), name); //$NON-NLS-1$

	}


	public class DBase {
		// this class will contain a database that scripts may access.
		/** The inner db. */
		private final DB innerDb;

		/**
		 * Instantiates a new database.
		 *
		 * @param innerDb the inner db
		 * @param name the name
		 */
		DBase(DB innerDb, String name) {
			this.innerDb = innerDb;
		}



	}

    public static class DBKey implements IByteArrayWriteable {
        private List<IByteArrayWriteable> qualifiers;

        @Override
        public byte[] toBytes() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            return null;
        }




    }
}
