package org.nodetest.servercore;

public class MossWorldLoadException extends Exception {
	String reason;

	public MossWorldLoadException(String string) {
		reason = string;
	}

}
