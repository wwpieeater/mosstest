package net.mosstest.scripting;

import net.mosstest.servercore.INodeManager;
import net.mosstest.servercore.MapGeneratorException;

// TODO: Auto-generated Javadoc

/**
 * The Class MapGenerators.
 */
public class MapGenerators {

    public static final int CHUNK_DIMENSION = 16;
    /**
     * The mg.
     */
    private static volatile IMapGenerator mg;

    /**
     * Sets the default map generator.
     *
     * @param g      the g
     * @param nm     the nm
     * @param seed   the seed
     * @param params the params
     * @throws MapGeneratorException the map generator exception
     */
    public static void setDefaultMapGenerator(IMapGenerator g, INodeManager nm, long seed,
                                              Object... params) throws MapGeneratorException {
        synchronized (MapGenerators.class) {
            mg = g;
            mg.init(seed, nm, params);
        }
    }

    /**
     * Gets the default mapgen.
     *
     * @return the default mapgen
     */
    public static IMapGenerator getDefaultMapgen() {
        return mg;
    }


}
