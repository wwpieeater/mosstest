package org.nodetest.servercore;

import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;

public final class ThreadManager {
	public static class ThreadQueueManager {
		HashMap<Script, PriorityBlockingQueue<MossScriptEvent>> scriptQueues;
		PriorityBlockingQueue<MossNetEventIn> netInQueue;
		PriorityBlockingQueue<MossNetEventOut> netOutQueue;
		PriorityBlockingQueue<MossPlayerEvent> playerEvtQueue;
		PriorityBlockingQueue<EmergeRequest> EmergeQueue;
		PriorityBlockingQueue<MapChangeEvent> MapWriteQueue;
		PriorityBlockingQueue<DatabaseWriteEvent> DBWriteQueue;//Underlying, slower queue for DB writes
		
		
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
