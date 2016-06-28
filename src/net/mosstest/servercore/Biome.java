package net.mosstest.servercore;

// TODO: Auto-generated Javadoc
/**
 * The Enum Biome.
 */
public enum Biome {
	
	/** The mg lake. */
	MG_LAKE, 
 /** The mg ice. */
 MG_ICE, 
 /** The mg ocean. */
 MG_OCEAN, 
 /** The mg swamp. */
 MG_SWAMP, 
 /** The mg forest. */
 MG_FOREST, 
 /** The mg plain. */
 MG_PLAIN, 
 /** The mg delta. */
 MG_DELTA, 
 /** The mg desert. */
 MG_DESERT, 
 /** The mg rainforest. */
 MG_RAINFOREST, 
 /** The mg jungle. */
 MG_JUNGLE, 
 /** The mg volcanic. */
 MG_VOLCANIC, 
 /** The mg default. */
 MG_DEFAULT;
	
	/**
	 * Select.
	 *
	 * @param humidity the humidity
	 * @param elevation the elevation
	 * @param geo_age the geo_age
	 * @param temperature the temperature
	 * @return the biome
	 */
	public Biome select(double humidity, double elevation, double geo_age, double temperature){
		return MG_DEFAULT;
	}
}
