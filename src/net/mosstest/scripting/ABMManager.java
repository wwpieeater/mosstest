package net.mosstest.scripting;

import net.mosstest.servercore.EngineSettings;
import net.mosstest.servercore.FuturesProcessor;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ABMManager {
    public static Logger logger = Logger.getLogger(ABMManager.class);

    // We'll need this to make calls to nearly any game function from scripts. Set in constructor.
    public final MossScriptEnv env;

    public ABMManager(MossScriptEnv env) {
        this.env = env;
    }

    private final FuturesProcessor fp = new FuturesProcessor(EngineSettings.getInt("abmThreads", 2));

    // no references kept to map, only to the set
    private final Set<MapChunk> loadedChunks = Collections.newSetFromMap(new ConcurrentHashMap<MapChunk, Boolean>());


    /**
     * Adds a chunk processor.
     * @param abm The ABM to add
     * @param msecDelay How many milliseconds should pass between executions
     * @param msecJitter How many milliseconds to jitter randomly off the specified delay
     * @param requeueOnException Whether the ABM should remain active if it throws any sort of exception.
     */
    public void addProcessor(MapChunkABM abm, long msecDelay, long msecJitter, boolean requeueOnException) {
        FuturesProcessor.Task task = fp.new Task(msecDelay, msecJitter, getRunnable(abm), requeueOnException ? FuturesProcessor.RequeuePolicy.REQUEUE_ALWAYS : FuturesProcessor.RequeuePolicy.REQUEUE_ON_SUCCESS);
    }


    private FuturesProcessor.ExceptableRunnable getRunnable(final MapChunkABM abm) {
        return new FuturesProcessor.ExceptableRunnable() {
            @Override
            public void run() throws Exception {
                for (MapChunk chk : ABMManager.this.loadedChunks) {
                    abm.process(chk, ABMManager.this.env);
                }
            }
        };
    }

}
