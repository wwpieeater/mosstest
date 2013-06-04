package org.nodetest.servercore;

import java.io.File;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;

public class MapDatabase {
ObjectContainer mapDb;
	public MapDatabase(String name, boolean create) throws MapDatabaseException, MossWorldLoadException {
		if(!name.matches("^[A-Z[a-z[0-9[ ]]]]+$")){
			throw new MossWorldLoadException("World name contains invalid characters");
		}
		
		try {
			mapDb=Db4oEmbedded.openFile("worlds/"+name+"mapdb.db4o");
		} catch (Db4oIOException | DatabaseFileLockedException
				| IncompatibleFileFormatException | OldFormatException
				| DatabaseReadOnlyException e) {
			
			throw new MossWorldLoadException("Database loading failed.");
		}
		
		
	}

	public void close() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
