package org.nodetest.servercore;

public final class ThreadManager {
	 private static ThreadManager instance = null;
	 static ThreadQueueManager queuemanager;
	   private ThreadManager() {
		   //A singleton to think about
	   }
	   public static ThreadManager getInstance() {
	      if(instance == null) {
	         instance = new ThreadManager();
	      }
	      return instance;
	   }
}
