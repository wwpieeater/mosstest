package net.mosstest.tests;

import java.io.File;

import net.mosstest.scripting.*;
import net.mosstest.servercore.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MapGeneratorTest {

	private static INodeManager nm;
	private static MapDatabase db;

	@BeforeClass
	public static void oneTimeSetUp() {
		try {
			db = new MapDatabase(new File("data/worlds/junit-test-world"));
			nm = new LocalNodeManager(db.nodes);
			MapGenerators.setDefaultMapGenerator(
					new MapGenerators.SimplexMapGenerator(), nm, 7485787384L);
		} catch (MossWorldLoadException | MapDatabaseException e) {
			assertTrue("exception initializing the database", false);
		} catch (MapGeneratorException e) {
			assertTrue("exception initializing the map generator", false);
		}

	}

	@AfterClass
	public static void oneTimeTearDown() {
		try {
			db.close();
		} catch (MapDatabaseException e) {
			assertTrue("Exception shutting down the database", false);
		}
	}

	@Before
	public void setUp() {

		System.out.println("@Before - setUp");
	}

	@After
	public void tearDown() {
		System.out.println("@After - tearDown");
	}

	@Test
	public void testEmptyCollection() {
		System.out.println("@Test - testEmptyCollection");
	}

	@Test
	public void testOneItemCollection() {
		System.out.println("@Test - testOneItemCollection");
	}
}