package org.nodetest.servercore;

import java.util.concurrent.atomic.AtomicBoolean;

public class MossFile {
	byte[] sha512;
	byte[] data;
	String name;
	AtomicBoolean isReady=new AtomicBoolean(false);
	public MossFile(byte[] sha512, String name) throws InterruptedException {
		
		this.sha512 = sha512;
		this.name = name;
		FileManager.resolutionQueue.put(this);
	}
	
}
