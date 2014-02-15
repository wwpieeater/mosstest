package net.mosstest.servercore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

// TODO: Auto-generated Javadoc
/**
 * The Class FuturesProcessor.
 */
public class FuturesProcessor {
	
	/** The r. */
	Random r = new Random();
	
	/** The jobs. */
	TreeMap<Long, Job> jobs = new TreeMap<>();
	
	/** The next wakeup. */
	volatile long nextWakeup = System.currentTimeMillis();

	/** The futures thread. */
	public Thread futuresThread = new Thread(new FuturesRunnable(), Messages.getString("FuturesProcessor.FUTURES_THREAD")); //$NON-NLS-1$

	/**
	 * Run once.
	 *
	 * @param delayMillis the delay millis
	 * @param runnable the runnable
	 */
	public synchronized void runOnce(long delayMillis, Runnable runnable) {
		Job tJob = new Job(System.currentTimeMillis() + delayMillis, 0, 0, 1.0,
				false, runnable);
		this.nextWakeup = (long) Math.min(this.nextWakeup,
				System.currentTimeMillis() + delayMillis);
		this.jobs.put(System.currentTimeMillis() + delayMillis, tJob);
		this.futuresThread.interrupt();
	}

	/**
	 * Register abm.
	 *
	 * @param delayMillis the delay millis
	 * @param delayJitterMillis the delay jitter millis
	 * @param probability the probability
	 * @param runnable the runnable
	 */
	public synchronized void registerAbm(long delayMillis,
			long delayJitterMillis, double probability, Runnable runnable) {
		Job tJob = new Job(System.currentTimeMillis(), delayMillis,
				delayJitterMillis, probability, true, runnable);
		this.nextWakeup = (long) Math.min(this.nextWakeup,
				System.currentTimeMillis() + delayMillis + Math.random()
						* delayJitterMillis);
		this.jobs.put(System.currentTimeMillis(), tJob);
		this.futuresThread.interrupt(); // interrupt, and step back to release
										// the lock
	}

	/**
	 * Start.
	 */
	public void start() {
		this.futuresThread.start();
	}

	/** The run. */
	private volatile boolean run = true;

	/**
	 * The Class FuturesRunnable.
	 */
	public class FuturesRunnable implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while (FuturesProcessor.this.run) {
				try {
					Thread.sleep(FuturesProcessor.this.nextWakeup
							- System.currentTimeMillis());
				} catch (InterruptedException | IllegalArgumentException e) {
					// pass, got a new job.
				}

				synchronized (FuturesProcessor.this) {
					ArrayList<Job> requeues = new ArrayList<>();
					iterLoop: for (Iterator<Entry<Long, Job>> iterator = FuturesProcessor.this.jobs
							.entrySet().iterator(); iterator.hasNext();) {
						Map.Entry<Long, FuturesProcessor.Job> e = iterator
								.next();

						// ascending key order
						if (e.getKey() <= System.currentTimeMillis()) {
							e.getValue().r.run();
							if (e.getValue().renew) {
								requeues.add(e.getValue());
							}
							iterator.remove();

						} else
							break iterLoop;
					}

					for (Job job : requeues) {
						job.nextInvocation = System.currentTimeMillis()
								+ job.delay + (long) Math.random()
								* job.delayJitter;
						FuturesProcessor.this.jobs.put(job.nextInvocation, job);
					}
				}
			}

		}

	}

	/**
	 * The Class Job.
	 */
	public class Job {
		
		/** The first invocation. */
		long firstInvocation;
		
		/** The delay. */
		long delay;
		
		/** The delay jitter. */
		long delayJitter;
		
		/** The probability. */
		double probability;
		
		/** The renew. */
		boolean renew;
		
		/** The r. */
		Runnable r;
		
		/** The next invocation. */
		volatile long nextInvocation;

		/**
		 * Instantiates a new job.
		 *
		 * @param firstInvocation the first invocation
		 * @param delay the delay
		 * @param delayJitter the delay jitter
		 * @param probability the probability
		 * @param renew the renew
		 * @param r the r
		 */
		public Job(long firstInvocation, long delay, long delayJitter,
				double probability, boolean renew, Runnable r) {
			this.firstInvocation = firstInvocation;
			this.delay = delay;
			this.delayJitter = delayJitter;
			this.probability = probability;
			this.renew = renew;
			this.r = r;
			this.nextInvocation = firstInvocation + delay
					+ (long) Math.random() * delayJitter;
		}

	}
}
