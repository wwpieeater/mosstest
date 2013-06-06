package org.nodetest.servercore;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public final class ThreadManager {
	public static class ThreadQueueManager {
		ArrayBlockingQueue<MossScriptEvent> eventQueue;
		ArrayBlockingQueue<MossNetEventIn> netInQueue;
		ArrayBlockingQueue<MossNetEventOut> netOutQueue;
		ArrayBlockingQueue<MossPlayerEvent> playerEvtQueue;
		ArrayBlockingQueue<EmergeRequest> EmergeQueue;
		ArrayBlockingQueue<MapChangeEvent> MapWriteQueue;
		ArrayBlockingQueue<DatabaseWriteEvent> DBWriteQueue;//Underlying, slower queue for DB writes
		
		
	}

	private static ThreadManager instance = null;

	private ThreadManager() {
		// A singleton to think about
	}

	public static ThreadManager getInstance() {
		if (instance == null) {
			instance = new ThreadManager();
		}
		return instance;
	}
}
