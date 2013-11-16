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
		options.addOption("server", false, Messages.getString("MossTest.RUN_STANDALONE")); //$NON-NLS-1$ //$NON-NLS-2$
		options.addOption("port", true, Messages.getString("MossTest.PORT_DESC")); //$NON-NLS-1$ //$NON-NLS-2$

		
		
	}

}
