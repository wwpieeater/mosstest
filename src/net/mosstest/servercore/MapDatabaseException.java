package net.mosstest.servercore;

// TODO: Auto-generated Javadoc
/**
 * The Class MapDatabaseException.
 */
public class MapDatabaseException extends Exception{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1261363696203853384L;
	
	/** The Constant SEVERITY_CORRUPT. */
	public static final int SEVERITY_CORRUPT = 0;
	
	/** The Constant SEVERITY_CORRUPT_REPARABLE. */
	public static final int SEVERITY_CORRUPT_REPARABLE = 1;
	
	/** The Constant SEVERITY_FATAL_TRANSIENT. */
	public static final int SEVERITY_FATAL_TRANSIENT = 2;
	
	/** The Constant SEVERITY_WARNING. */
	public static final int SEVERITY_WARNING = 4;
	
	/** The Constant SEVERITY_NOTFOUND. */
	public static final int SEVERITY_NOTFOUND = 8;
	
	/** The Constant SEVERITY_UNKNOWN. */
	public static final int SEVERITY_UNKNOWN = 16;
	
	/** The desc. */
	public String desc;
	
	/** The severity. */
	public int severity;
	
	/**
	 * Instantiates a new map database exception.
	 *
	 * @param severity the severity
	 * @param desc the desc
	 */
	public MapDatabaseException(int severity, String desc) {
		super();
		this.desc = desc;
		this.severity = severity;
	}
}
