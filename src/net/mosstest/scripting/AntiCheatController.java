package net.mosstest.scripting;

import net.mosstest.servercore.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: Auto-generated Javadoc
/**
 * The Class AntiCheatController.
 */
public class AntiCheatController {

    protected final List<AntiCheatCheck> checks = new ArrayList<>();

    protected final List<AntiCheatHandler> handlers = new ArrayList<>();

    protected MossScriptEnv env;

    protected SessionManager sess;

    protected volatile boolean run = false;

    protected AtomicInteger needDeathsFrom = new AtomicInteger(0);

    /**
     * Instantiates a new anticheat controller.
     */
    public AntiCheatController(MossScriptEnv env, SessionManager sess) {
		this.env = env;
		this.sess = sess;
	}

	/**
	 * Register check.
	 *
	 * @param chk the chk
	 */
	public void registerCheck(AntiCheatCheck chk) {
		synchronized (this.checks) {
			this.checks.add(chk);
		}
	}

	/**
	 * Register handler.
	 *
	 * @param hdl the hdl
	 */
	public void registerHandler(AntiCheatHandler hdl) {
		synchronized (this.handlers) {
			this.handlers.add(hdl);
		}
	}

	/** The threads. */
    private List<Thread> threads = new ArrayList<>();

    /**
     * Start threads.
	 *
	 * @param threads the threads
	 * @param priority the priority
	 */
	public void startThreads(int threads, int priority) {
		this.run = true;
		for (int i = 0; i < threads; i++) {
			Thread cThread = new Thread(new AntiCheatRunnable(), "anticheat-" //$NON-NLS-1$
					+ new Random().nextInt());
			this.threads.add(cThread);
			cThread.start();
			cThread.setPriority(priority);
		}
	}

	/**
	 * Kill threads.
	 *
	 * @param threads the threads
	 */
	public void killThreads(int threads) {

		this.needDeathsFrom.addAndGet(threads);

	}

	/**
	 * The Interface AntiCheatCheck.
	 */
	public static interface AntiCheatCheck {
		/**
		 * Check that a player is compliant with this anti-cheat check. This
		 * method is not guaranteed to be called, especially if system resources
		 * are low.
		 * 
		 * @param p
		 *            The player to check. The volatile fields xpos, ypos, zpos,
		 *            oldx, oldy, oldz, lastAntiCheatMillis may prove
		 *            particularly useful.
		 * @param env
		 *            The MossScriptEnv that may be used in case
		 *            <code>moss</code> is not available in javascript global
		 *            scope.
		 * @return Value representing a violation level. Recommended values are
		 *         0 for all-OK to 100 for the most severe cheating.
		 */
		public int check(Player p, MossScriptEnv env);
	}

	/**
	 * The Interface AntiCheatHandler.
	 */
	public static interface AntiCheatHandler {

		/**
		 * Called if a violation is detected by any check. This method is
		 * guaranteed to be called upon any violation being detected.
		 * 
		 * @param p
		 *            The player with a violation.
		 * @param severity
		 *            Value representing a violation level. Recommended values
		 *            are 0 for all-OK to 100 for the most severe cheating.
		 * @param env
		 *            The MossScriptEnv that may be used in case
		 *            <code>moss</code> is not available in javascript global
		 *            scope.
		 */
		public void handleViolation(Player p, int severity, MossScriptEnv env);
	}

	/**
	 * The Class AntiCheatRunnable.
	 */
	private class AntiCheatRunnable implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			runLoop: while (AntiCheatController.this.run) {
				int needDeaths;
				if ((needDeaths = AntiCheatController.this.needDeathsFrom.get()) > 0) {
					if (AntiCheatController.this.needDeathsFrom.compareAndSet(
							needDeaths, needDeaths - 1))
						break runLoop;
				}

				try {
					for (Player player : AntiCheatController.this.sess.playerSessions
							.keySet()) {
						for (AntiCheatCheck chk : AntiCheatController.this.checks) {
							int severity;
							if ((severity = chk.check(player,
									AntiCheatController.this.env)) > 0) {
								for (AntiCheatHandler hdl : AntiCheatController.this.handlers) {
									hdl.handleViolation(player, severity,
											AntiCheatController.this.env);
								}
							}
						}
					}
				} catch (Exception e) {
					// pass for possible ConcurrentModificationException
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// pass
				}
			}

		}
	}
}
