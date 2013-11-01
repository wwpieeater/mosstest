package net.mosstest.servercore;

public class MapDatabaseException extends Exception{
	private static final long serialVersionUID = -1261363696203853384L;
	public static final int SEVERITY_CORRUPT = 0;
	public static final int SEVERITY_CORRUPT_REPARABLE = 1;
	public static final int SEVERITY_FATAL_TRANSIENT = 2;
	public static final int SEVERITY_WARNING = 4;
	public static final int SEVERITY_NOTFOUND = 8;
	public static final int SEVERITY_UNKNOWN = 16;
	public String desc;
	public int severity;
	public MapDatabaseException(int severity, String desc) {
		super();
		this.desc = desc;
		this.severity = severity;
	}
}
