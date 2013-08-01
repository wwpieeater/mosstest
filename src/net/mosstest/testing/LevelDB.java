package net.mosstest.testing;

import org.fusesource.leveldbjni.internal.NativeDB;
import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.util.Random;

public class LevelDB {
	public static String generateString(int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	static Random rng=new Random();
	static int iterations=102400;
	static int strLen=1024;
	static String characters="1234567890QWERTYUIOP{}ASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.createIfMissing(true);
		DB db = factory.open(new File("testDBs/levelDB/example"+Long.toHexString(System.nanoTime())), options);
		long startTime = System.nanoTime();
//		try {
//			for(int i=0; i<iterations; i++){
//				db.put(bytes(Integer.toBinaryString(i)), bytes(generateString(128)));
//			}
//		} finally {
//			long endTime = System.nanoTime();

		//	long duration = endTime - startTime;
		//	System.out.println("Write: "+duration/iterations);
		//	System.out.println("Wr/sec: "+1000000000*(double)iterations/(double)duration);
			// Make sure you close the db to shutdown the
			// database and avoid resource leaks.
			
		//}
		String ourString;
		startTime = System.nanoTime();
		try {
			for(int i=0; i<2; i++){
				ourString=asString(db.get(bytes(Integer.toBinaryString(i+101010))));
				System.out.println(ourString==null);
			}
		} finally {
			long endTime = System.nanoTime();

			long duration = endTime - startTime;
			System.out.println("Read: "+duration/iterations);
			System.out.println("Rd/sec: "+1000000000*(double)iterations/(double)duration);
			// Make sure you close the db to shutdown the
			// database and avoid resource leaks.
			
		}db.close();
	}
}