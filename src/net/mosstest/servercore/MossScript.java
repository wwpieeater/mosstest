package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;


public class MossScript {
	File file;
	public MossScript(File file_, String scName) throws MossWorldLoadException{
		file=new File(file_, scName);
		if(!file.isFile()) throw new MossWorldLoadException("Script named "+scName+" not found!");
	}
	Reader getReader() throws MossWorldLoadException {
		try {
			return new FileReader(file);
		} catch (FileNotFoundException e) {
			//whoa there, something REALLY bad happened
			throw new MossWorldLoadException("Extremely unexpected situation on our hands");
		}
		
	}
}
