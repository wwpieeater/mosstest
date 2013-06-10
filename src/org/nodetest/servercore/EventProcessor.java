package org.nodetest.servercore;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventProcessor {
	static ArrayBlockingQueue<MossEvent> eventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false);
	private static final int maxEventThreads = EngineSettings.getInt(
			"maxEventThreads", 8);
	private static final int initialEventThreads = EngineSettings.getInt(
			"initialEventThreads", 8);
	static ThreadGroup eventProcessorGroup = new ThreadGroup(
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
				@Override
				public void run() {
					System.out.println("manager thread started");
					int ticks = 0;
					int ticksBusy = 0;
					int sampleInterval = EventProcessor.sampleInterval;
					int upshift = EventProcessor.upshift;
					int downshift = EventProcessor.downshift;
					int samples = EventProcessor.samples;
					Thread[] threads = new Thread[maxEventThreads];
					int currentThreads = 0;
					for (int i = 0; i<initialEventThreads; i++) {
						System.out.println("foo");
						threads[currentThreads]=new Thread(EventProcessor.eventProcessorGroup, new Runnable() {

							@Override
							public void run() {
								System.out.println("Worker thread starteds");
								processEvents();
							}

						});
						threads[currentThreads].start();
						
						System.out.println("PostRun");
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

									@Override
									public void run() {
										System.out.println("Dynamically added thread");
										processEvents();
									}

								}).run();
								currentThreads++;

							}
							if (((float) ticksBusy / (float) ticks) < ((float) downshift / (float) samples)) {
								System.out.println(("Stopping one thread"));
								eventQueue.add(new MossEvent(MossEvent.EvtType.EVT_THREADSTOP));

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
		System.out.println("Worker thread entered");
		boolean run=true; //Not synchronized as only used internally
		while(run){try {
			MossEvent myEvent=eventQueue.take();
			{//Section for actually handling the events
				
				
			}
			//Otherwise do some cool scripting stuff!
		} catch (InterruptedException e) {
			
		}
		System.out.println("Reached end of thread code");
		}
		
	}
	static void init(){
		manager.run();
	}
	public static void main(String[] args){
		EventProcessor.init();
	}
}
