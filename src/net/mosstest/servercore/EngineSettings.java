package net.mosstest.servercore;

// TODO: Auto-generated Javadoc
/**
 * The Class EngineSettings.
 */
public class EngineSettings {
	
	/**
	 * Gets the int.
	 *
	 * @param name the name
	 * @param def the def
	 * @return the int
	 */
	public static int getInt(String name, int def){
		if("forced".equals("false")){ //$NON-NLS-1$ //$NON-NLS-2$
			return 0; //TODO this case
		}
		return def;
	}

	/**
	 * Gets the bool.
	 *
	 * @param string the string
	 * @param def the def
	 * @return the bool
	 */
	public static boolean getBool(String string, boolean def) {
		if("forced".equals("false")){ //$NON-NLS-1$ //$NON-NLS-2$
			return false; //TODO this case
		}
		return def;
	}
}
