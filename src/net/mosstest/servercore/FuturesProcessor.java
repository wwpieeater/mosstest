package net.mosstest.servercore;

import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

// TODO: Auto-generated Javadoc

public class FuturesProcessor {
    // tasks for running
    private DelayQueue<Task> queue = new DelayQueue<>();
    private static final Logger logger = Logger.getLogger(FuturesProcessor.class);
    // tasks for addition and removal.
    private final ConcurrentHashMap<Object, Task> tasks = new ConcurrentHashMap<>();

    /**
     * Adds a future for immediate potential execution. If this task already exists, nothing will occur.
     * @param task The future task to add. Its RequeuePolicy will be used to determine when to requeue it.
     * @param key Any object to associate with this task (such as a higher-level ABM object), to be used when removing this task
     */
    public synchronized void addFuture(Task task, Object key){
        if(this.tasks.containsValue(task)) return;
        this.tasks.put(key, task);
        this.queue.put(task);
    }

    public synchronized void removeFuture(Object key){
        this.tasks.remove(key);
    }

    protected void runLoop() {
        rLoop:
        while (true) {
            Task task;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                logger.error("InterruptedException in futures processor trying to take from queue.");
                Thread.currentThread().interrupt(); // in case caller requires detecting interrupts
                continue rLoop;
            }
            try {

                task.getCallback().run();
                if (task.getRequeuePolicy() == RequeuePolicy.REQUEUE_ON_SUCCESS) {
                    task.requeue();
                    continue rLoop;
                }
                if (task.getRequeuePolicy() == RequeuePolicy.REQUEUE_ON_FAILURE) {
                   task.removeSelf();

                }
            } catch (Exception e) {
                if (task.getRequeuePolicy() == RequeuePolicy.REQUEUE_ON_FAILURE) {
                    task.requeue();
                    continue rLoop;
                }
                if (task.getRequeuePolicy() == RequeuePolicy.REQUEUE_ON_SUCCESS) {
                    task.removeSelf();

                }
            }
            if (task.getRequeuePolicy() == RequeuePolicy.REQUEUE_ALWAYS) {
                task.requeue();
                continue rLoop;
            }
            if (task.getRequeuePolicy() == RequeuePolicy.REQUEUE_NONE) {
                task.removeSelf();

            }

        }
    }

    public FuturesProcessor(int threads) {
        for (int i = 0; i < threads; i++) {
            Thread th = new Thread(new QueueRunnable(), "FuturesProcessorThread" + i);
            th.start();
        }


    }

    public class Task implements Delayed {

        private final long delayMsec;

        private final long jitterMsec;

        private ExceptableRunnable callback;

        private RequeuePolicy requeuePolicy;
        private volatile long jitterThisTime;
        private final Object key;

        public Task(long delayMsec, long jitterMsec, ExceptableRunnable callback, RequeuePolicy requeuePolicy, Object key) {
            this.delayMsec = delayMsec;
            this.jitterMsec = jitterMsec;
            this.callback = callback;
            this.requeuePolicy = requeuePolicy;
            this.key = key;
        }

        public long getDelayMsec() {
            return delayMsec;
        }


        public long getJitterMsec() {
            return jitterMsec;
        }


        public ExceptableRunnable getCallback() {
            return callback;
        }

        public void setCallback(ExceptableRunnable callback) {
            this.callback = callback;
        }

        public RequeuePolicy getRequeuePolicy() {
            return requeuePolicy;
        }

        public void setRequeuePolicy(RequeuePolicy requeuePolicy) {
            this.requeuePolicy = requeuePolicy;
        }

        @Override
        public long getDelay(TimeUnit unit) {

            return unit.convert(delayMsec + jitterThisTime, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.delayMsec, o.getDelay(TimeUnit.MILLISECONDS));
        }


        public void requeue() {
            this.jitterThisTime = (long) (this.delayMsec + ((2.0 * Math.random()) - 1) * this.jitterMsec);
            FuturesProcessor.this.queue.put(this);
        }

        public void removeSelf() {
            FuturesProcessor.this.removeFuture(this.key);
        }
    }

    public enum RequeuePolicy {
        REQUEUE_NONE,
        REQUEUE_ON_SUCCESS,
        REQUEUE_ON_FAILURE,
        REQUEUE_ALWAYS
    }

    public interface ExceptableRunnable {
        public void run() throws Exception;
    }

    private class QueueRunnable implements Runnable {
        @Override
        public void run() {
            FuturesProcessor.this.runLoop();
        }
    }
}
