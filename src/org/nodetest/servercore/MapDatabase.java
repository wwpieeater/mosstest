package org.nodetest.servercore;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentFailureException;
import com.sleepycat.je.EnvironmentNotFoundException;

import java.io.File;

public class MapDatabase {
	private Environment env;

	public MapDatabase(String name, boolean create) throws MapDatabaseException {
		EnvironmentConfig envconfig = new EnvironmentConfig();
		envconfig.setTransactional(true);
		envconfig.setAllowCreate(create);
		try {
			env = new Environment(new File(name), envconfig);
		} catch (EnvironmentNotFoundException e) {
			throw new MapDatabaseException(
					MapDatabaseException.SEVERITY_NOTFOUND,
					"World does not exist");
		} catch (EnvironmentFailureException e) {
			throw new MapDatabaseException(
					MapDatabaseException.SEVERITY_FATAL_TRANSIENT,
					"Environment Failure Exception");
		} catch (DatabaseException e) {
			throw new MapDatabaseException(
					MapDatabaseException.SEVERITY_UNKNOWN,
					"DatabaseException caught. Please investigate.");
		}

	}

	public void close() throws MapDatabaseException {
		try {
			env.cleanLog();
			env.close();
		} catch (DatabaseException e) {
			throw new MapDatabaseException(
					MapDatabaseException.SEVERITY_UNKNOWN,
					"Error on shutdown. World will be checked for corruption at next start.");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
