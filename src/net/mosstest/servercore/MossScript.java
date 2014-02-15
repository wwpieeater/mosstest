package net.mosstest.servercore;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

// TODO: Auto-generated Javadoc
/**
 * The Class MossScript.
 */
public class MossScript {
	
	/** The file. */
	MossFile file;

	/**
	 * Instantiates a new moss script.
	 *
	 * @param file the file
	 * @throws MossWorldLoadException the moss world load exception
	 */
	public MossScript(MossFile file) throws MossWorldLoadException {
		this.file = file;
		try {
			if (!this.file.getFile().isFile())
				throw new MossWorldLoadException(Messages.getString("MossScript.MSG_SCRIPT_NAMED_[NAME]") + file.dirName //$NON-NLS-1$
						+ "/" + file.resourceName + Messages.getString("MossScript.[NAME]_NOT_FOUND")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e) {
			throw new MossWorldLoadException(Messages.getString("MossScript.MSG_IO_EXCEPTION") //$NON-NLS-1$
					+ file.dirName + "/" + file.resourceName); //$NON-NLS-1$
		}
	}

	/**
	 * Gets the reader.
	 *
	 * @return the reader
	 * @throws MossWorldLoadException the moss world load exception
	 */
	Reader getReader() throws MossWorldLoadException {
		try {
			return new FileReader(this.file.getFile());
		} catch (IOException e) {
			// whoa there, something REALLY bad happened
			throw new MossWorldLoadException(
					Messages.getString("MossScript.MSG_OOPS")); //$NON-NLS-1$

		}
	}
}
