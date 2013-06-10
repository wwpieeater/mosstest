package org.nodetest.servercore;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileManager {
	public class Chunk {
		byte[] fileSha512;
		byte[] data;
	}
	AtomicBoolean shouldRun=new AtomicBoolean(true);
	static ArrayBlockingQueue<MossFile> resolutionQueue = new ArrayBlockingQueue<>(
			256);
	private static ArrayBlockingQueue<FileManager.Chunk> chunkInQueue = new ArrayBlockingQueue<>(
			256);
	private Thread resolutionThread = new Thread(new Runnable() {
		public void run(){
			while(shouldRun.get()){
				MossFile fileIn = resolutionQueue.poll();
				if (fileIn!=null){
					// FIXME 
					//NetworkManager.send(NetworkManager.PacketTypes.TOSERVER_INIT, fileIn.sha256);
				}
			}
		}
	}, "FileResolutionThread");
}
