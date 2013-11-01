package net.mosstest.servercore;
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

		
		
	}

}
