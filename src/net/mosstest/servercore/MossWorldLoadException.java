package net.mosstest.servercore;

// TODO: Auto-generated Javadoc
/**
 * The Class MossWorldLoadException.
 */
public class MossWorldLoadException extends Exception {
	
	/** The reason. */
	String reason;

	/**
	 * Instantiates a new moss world load exception.
	 *
	 * @param string the string
	 */
	public MossWorldLoadException(String string) {
		reason = string;
	}

}
