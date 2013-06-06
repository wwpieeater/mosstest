package org.nodetest.servercore;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventProcessor {
	static ArrayBlockingQueue<MossEvent> eventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false);
	private static final int maxEventThreads = EngineSettings.getInt(
			"maxEventThreads", 8);
	private static final int initialEventThreads = EngineSettings.getInt(
			"initialEventThreads", 8);
	private static ThreadGroup eventProcessorGroup = new ThreadGroup(
			"EventProcessor");
	private static AtomicBoolean runManager = new AtomicBoolean(true);
	protected static final int sampleInterval = EngineSettings.getInt(
			"eventQueueTuneSampleInterval", 100);
	protected static final int upshift = EngineSettings.getInt(
			"eventQueueTuneUpshift", 90);
	protected static final int downshift = EngineSettings.getInt(
			"eventQueueTuneDownshift", 10);
	protected static final int samples = EngineSettings.getInt(
			"eventQueueTuneSamples", 100);
	private static Thread manager = new Thread(eventProcessorGroup,
			new Runnable() {
				public void run() {
					int ticks = 0;
					int ticksBusy = 0;
					int sampleInterval = EventProcessor.sampleInterval;
					int upshift = EventProcessor.upshift;
					int downshift = EventProcessor.downshift;
					int samples = EventProcessor.samples;
					Thread[] threads = new Thread[maxEventThreads];
					int currentThreads = 0;
					for (int i = 0; i < initialEventThreads; i++) {
						new Thread(eventProcessorGroup, new Runnable() {

							public void run() {
								processEvents();
							}

						}).run();
						currentThreads++;

					}
					while (EventProcessor.runManager.get()) {
						ticks++;
						if (!eventQueue.isEmpty()) {
							ticksBusy++;
						}
						if (ticks >= samples) {
							if ((currentThreads < maxEventThreads)
									&& (((float) ticksBusy / (float) ticks) > ((float) upshift / (float) samples))) {
								new Thread(eventProcessorGroup, new Runnable() {

									public void run() {
										processEvents();
									}

								}).run();
								currentThreads++;

							}
							if (((float) ticksBusy / (float) ticks) < ((float) downshift / (float) samples)) {
								eventQueue.add(new MossStopEvent());

							}
							ticks = 0;
							ticksBusy = 0;
						}
						try {
							Thread.sleep(sampleInterval);
						} catch (InterruptedException e) {
							
						}
					}
				};
			}, "EventProcessorManager");

	static void processEvents() {
		//GIANT TODO
		boolean run=true; //Not synchronized as only used internally
		while(run){try {
			MossEvent myEvent=eventQueue.take();
			if(myEvent instanceof MossStopEvent){
				run=false;
				
			}
			//Otherwise do some cool scripting stuff!
		} catch (InterruptedException e) {
			
		}
		}
		
	}
	static{
		manager.start();
	}
}
