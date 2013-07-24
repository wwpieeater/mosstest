package org.nodetest.servercore;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.nodetest.scripting.EventProcessingCompletedSignal;
import org.nodetest.scripting.MossEventHandler;
import org.nodetest.scripting.MossScriptEnv;
import org.nodetest.scripting.MossScriptException;
import org.nodetest.servercore.MossEvent.EvtType;

/**
 * 
 * @author rarkenin, hexafraction 
 * 
 * Blargh.
 * 
 *  This is a nasty thread pool. 
 *  If you don't understand threading or Java well,
 *  you may want to stick to only accessing the queue as otherwise asphyxiation,
 *  drowning, or chlorine poisoning may occur. 
 *  USE ACCESS METHODS OTHER THAN THE QUEUE AT YOUR OWN RISK.
 *  Oh, and this code is so fluffy you may be bludgeoned to death by way of a
 *  pillow if not careful.
 * 
 */
public class EventProcessor {
	static ArrayBlockingQueue<MossEvent> eventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false);
	private static final int maxEventThreads = EngineSettings.getInt(
			"maxEventThreads", 8);
	private static final int initialEventThreads = EngineSettings.getInt(
			"initialEventThreads", 8);
	static ThreadGroup eventProcessorGroup = new ThreadGroup("EventProcessor");
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
		/**
		 * 
		 * The manager. Controls the thread number.
		 */
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
					for (int i = 0; i < initialEventThreads; i++) {
						System.out.println("foo");
						threads[currentThreads] = new Thread(
								EventProcessor.eventProcessorGroup,
								new Runnable() {

									@Override
									public void run() {
										System.out
												.println("Worker thread starteds");
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
										System.out
												.println("Dynamically added thread");
										processEvents();
									}

								}).run();
								currentThreads++;

							}
							if (((float) ticksBusy / (float) ticks) < ((float) downshift / (float) samples)) {
								System.out.println(("Stopping one thread"));
								eventQueue
										.add(new MossEvent(
												MossEvent.EvtType.EVT_THREADSTOP,
												null,
												0,
												0,
												0,
												null,
												null,
												null,
												null,
												null,
												null,
												new ScriptSandboxBorderToken(84, EventProcessor.class)));

							}
							ticks = 0;
							ticksBusy = 0;
						}
						try {
							Thread.sleep(sampleInterval);
						} catch (InterruptedException e) {

						}
					}
				}
			}, "EventProcessorManager");

	static void processEvents() {
		// GIANT TODO
		System.out.println("Worker thread entered");
		boolean run = true; // Not synchronized as only used internally
		while (run) {
			try {
				MossEvent myEvent = eventQueue.take();
				{// Section for actually handling the events
					if (myEvent.type == EvtType.EVT_THREADSTOP)
						return;
					ArrayList<MossEventHandler> evtHandlerList = MossScriptEnv
							.getHandlers(myEvent.type,
									new ScriptSandboxBorderToken(84, EventProcessor.class));
					try {
						for (MossEventHandler ourHandler : evtHandlerList) {
							ourHandler.processEvent(myEvent);
						}
						DefaultEventHandlers.processEvent(myEvent);
					} catch (EventProcessingCompletedSignal
							| MossScriptException e) {
						// Event processing complete
					}
				}

				// Otherwise do some cool scripting stuff!
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Reached end of thread code");
		}

	}

	static void init() {
		manager.start();
	}

	public static void main(String[] args) {
		EventProcessor.init();
	}
}
