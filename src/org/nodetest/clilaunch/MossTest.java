package org.nodetest.clilaunch;
import org.apache.commons.cli.Options;


public class MossTest {

	public MossTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options options=new Options();
		options.addOption("server", false, "Run as standalone server");
		options.addOption("port", true, "Port number to use");
		options.addOption("disable-udp", false, "Disable use of UDP");
		options.addOption("only-udp", false, "Only use UDP");
		
		
	}

}
