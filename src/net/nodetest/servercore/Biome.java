package org.nodetest.servercore;

public enum Biome {
	MG_LAKE, MG_ICE, MG_OCEAN, MG_SWAMP, MG_FOREST, MG_PLAIN, MG_DELTA, MG_DESERT, MG_RAINFOREST, MG_JUNGLE, MG_VOLCANIC, MG_DEFAULT;
	public Biome select(double humidity, double elevation, double geo_age, double temperature){
		return MG_DEFAULT;
	}
}
