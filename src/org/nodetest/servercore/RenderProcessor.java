package org.nodetest.servercore;

import java.util.concurrent.ArrayBlockingQueue;

public class RenderProcessor {
	static ArrayBlockingQueue<MossRenderEvent> renderEventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false);
	
	private static Thread renderThread = new Thread(new Runnable () {
		public void run () {
			boolean run = true;
			System.out.println("Thread initiated");
			while (run) {
				try {
					MossEvent myEvent = renderEventQueue.take();
					if (myEvent instanceof MossRenderStopEvent) {
						System.out.println("Thread shutting down");
						run = false;
					}
					//Add more events
				}
				catch (InterruptedException e) {
					
				}
			}
			
		}
	});
	static void init () {
		renderThread.start ();
	}
	public static void main (String [] args) {
		RenderProcessor.init();
	}
}
