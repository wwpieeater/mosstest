package net.mosstest.servercore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

public class FuturesProcessor {
	Random r = new Random();
	TreeMap<Long, Job> jobs = new TreeMap<>();
	volatile long nextWakeup = System.currentTimeMillis();

	public Thread futuresThread = new Thread(new FuturesRunnable(), Messages.getString("FuturesProcessor.FUTURES_THREAD")); //$NON-NLS-1$

	public synchronized void runOnce(long delayMillis, Runnable runnable) {
		Job tJob = new Job(System.currentTimeMillis() + delayMillis, 0, 0, 1.0,
				false, runnable);
		this.nextWakeup = (long) Math.min(this.nextWakeup,
				System.currentTimeMillis() + delayMillis);
		this.jobs.put(System.currentTimeMillis() + delayMillis, tJob);
		this.futuresThread.interrupt();
	}

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

	public void start() {
		this.futuresThread.start();
	}

	private volatile boolean run = true;

	public class FuturesRunnable implements Runnable {

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

	public class Job {
		long firstInvocation;
		long delay;
		long delayJitter;
		double probability;
		boolean renew;
		Runnable r;
		volatile long nextInvocation;

		/**
		 * @param firstInvocation
		 * @param delay
		 * @param delayJitter
		 * @param probability
		 * @param renew
		 * @param r
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
