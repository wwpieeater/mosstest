package net.mosstest.servercore;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import net.mosstest.scripting.EventProcessingCompletedSignal;
import net.mosstest.scripting.MossEvent;
import net.mosstest.scripting.MossEventHandler;
import net.mosstest.scripting.MossScriptEnv;
import net.mosstest.scripting.MossScriptException;
import net.mosstest.scripting.MossEvent.EvtType;

/**
 * 
 * @author rarkenin, hexafraction
 * 
 *         Blargh.
 * 
 *         This is a nasty thread pool. If you don't understand threading or
 *         Java well, you may want to stick to only accessing the queue as
 *         otherwise asphyxiation, drowning, or chlorine poisoning may occur.
 *         USE ACCESS METHODS OTHER THAN THE QUEUE AT YOUR OWN RISK.
 * 
 */
public class EventProcessor {
	ArrayBlockingQueue<MossEvent> eventQueue = new ArrayBlockingQueue<>(
			EngineSettings.getInt("eventQueueCapacity", 40), false); //$NON-NLS-1$
	protected final int maxEventThreads = EngineSettings.getInt(
			"maxEventThreads", 8); //$NON-NLS-1$
	protected final int initialEventThreads = EngineSettings.getInt(
			"initialEventThreads", 8); //$NON-NLS-1$
	ThreadGroup eventProcessorGroup = new ThreadGroup("EventProcessor"); //$NON-NLS-1$
	protected AtomicBoolean runManager = new AtomicBoolean(true);
	protected final int sampleInterval = EngineSettings.getInt(
			"eventQueueTuneSampleInterval", 100); //$NON-NLS-1$
	protected final int upshift = EngineSettings.getInt(
			"eventQueueTuneUpshift", 90); //$NON-NLS-1$
	protected final int downshift = EngineSettings.getInt(
			"eventQueueTuneDownshift", 10); //$NON-NLS-1$
	protected final int samples = EngineSettings.getInt(
			"eventQueueTuneSamples", 100); //$NON-NLS-1$
	protected final AtomicInteger currentThreads = new AtomicInteger(0);
	private Thread manager = new Thread(this.eventProcessorGroup,
			new Runnable() {
				/**
				 * 
				 * The manager. Controls the thread number.
				 */
				@SuppressWarnings("nls")
				@Override
				public void run() {

					System.out.println("manager thread started"); //$NON-NLS-1$
					int ticks = 0;
					int ticksBusy = 0;
					int lSampleInterval = EventProcessor.this.sampleInterval;
					int lUpshift = EventProcessor.this.upshift;
					int lDownshift = EventProcessor.this.downshift;
					int lSamples = EventProcessor.this.samples;
					Thread[] threads = new Thread[EventProcessor.this.maxEventThreads];

					for (int i = 0; i < EventProcessor.this.initialEventThreads; i++) {
						System.out.println("foo"); //$NON-NLS-1$
						int c = EventProcessor.this.currentThreads.get();
						threads[c] = new Thread(
								EventProcessor.this.eventProcessorGroup,
								new Runnable() {

									@Override
									public void run() {
										System.out
												.println("Worker thread starteds"); //$NON-NLS-1$
										processEvents();
									}

								});
						threads[c].start();

						System.out.println("PostRun"); //$NON-NLS-1$
						EventProcessor.this.currentThreads.incrementAndGet();

					}
					while (EventProcessor.this.runManager.get()) {
						ticks++;
						if (!EventProcessor.this.eventQueue.isEmpty()) {
							ticksBusy++;
						}
						if (ticks >= lSamples) {
							if ((EventProcessor.this.currentThreads.get() < EventProcessor.this.maxEventThreads)
									&& (((float) ticksBusy / (float) ticks) > ((float) lUpshift / (float) lSamples))) {
								new Thread(
										EventProcessor.this.eventProcessorGroup,
										new Runnable() {

											@Override
											public void run() {
												System.out
														.println("Dynamically added thread"); //$NON-NLS-1$
												processEvents();
											}

										}).run();
								EventProcessor.this.currentThreads
										.incrementAndGet();

							}
							if (((float) ticksBusy / (float) ticks) < ((float) lDownshift / (float) lSamples)) {
								System.out.println(("Stopping one thread"));
								EventProcessor.this.eventQueue
										.add(new MossEvent(
												MossEvent.EvtType.EVT_THREADSTOP,
												null, null, null, null, null,
												null, null, 0, null,
												new ScriptSandboxBorderToken(
														84,
														EventProcessor.class)));

							}
							ticks = 0;
							ticksBusy = 0;
						}
						try {
							Thread.sleep(lSampleInterval);
						} catch (InterruptedException e) {
							// manager awoke, no problem
						}
					}
				}
			}, "EventProcessorManager"); //$NON-NLS-1$
	private MossScriptEnv ev;

	void processEvents() {
		System.out.println("Worker thread entered"); //$NON-NLS-1$
		boolean run = true; // Not synchronized as only used locally
		queueLoop: while (run) {
			try {
				MossEvent myEvent = this.eventQueue.take();
				{// Section for actually handling the events
					if (myEvent.type == EvtType.EVT_THREADSTOP) {
						this.currentThreads.decrementAndGet();
						return;
					}
					ArrayList<MossEventHandler> evtHandlerList = this.ev
							.getHandlers(myEvent.type,
									new ScriptSandboxBorderToken(84,
											EventProcessor.class));
					try {
						for (MossEventHandler ourHandler : evtHandlerList) {
							if (ourHandler.processEvent(myEvent))
								continue queueLoop;
						}
						DefaultEventHandlers.processEvent(myEvent, this.ev);
					} catch (MossScriptException e) {
						// Event processing complete, pass
					}
				}

			} catch (InterruptedException e) {
				// thread struck, shut down the operation.
			}
			System.out.println("Reached end of thread code"); //$NON-NLS-1$
			this.currentThreads.decrementAndGet();
		}

	}

	/**
	 * Constructs a new event processor.
	 * 
	 * @param ev
	 *            A script environment populated with event handlers.
	 */
	public EventProcessor(MossScriptEnv ev) {
		this.ev = ev;
		this.manager.start();
	}

}
