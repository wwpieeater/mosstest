package net.mosstest.servercore;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class MossScript {
	MossFile file;

	public MossScript(MossFile file) throws MossWorldLoadException {
		this.file = file;
		try {
			if (!this.file.getFile().isFile())
				throw new MossWorldLoadException("Script named " + file.dirName
						+ "/" + file.resourceName + " not found!");
		} catch (IOException e) {
			throw new MossWorldLoadException("IOException with script named "
					+ file.dirName + "/" + file.resourceName);
		}
	}

	Reader getReader() throws MossWorldLoadException {
		try {
			return new FileReader(this.file.getFile());
		} catch (IOException e) {
			// whoa there, something REALLY bad happened
			throw new MossWorldLoadException(
					"Extremely unexpected situation on our hands");

		}
	}
}
