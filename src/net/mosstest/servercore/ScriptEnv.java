package net.mosstest.servercore;

import net.mosstest.scripting.MossScriptEnv;
import net.mosstest.servercore.MosstestSecurityManager.ThreadContext;
import org.apache.log4j.Logger;
import org.mozilla.javascript.*;

import java.io.IOException;

// TODO: Auto-generated Javadoc

/**
 * Static environment for executing scripts. Call {@link ScriptEnv.runScript()}
 * to call a script.
 *
 * @author rarkenin
 */
public class ScriptEnv {

    static Logger logger = Logger.getLogger(MossDebugUtils.class);

    ScriptableObject globalScope;


    /**
     * The Enum ScriptResult.
     */
    public enum ScriptResult {

        /**
         * The result executed.
         */
        RESULT_EXECUTED,
        /**
         * The result executng background.
         */
        RESULT_EXECUTNG_BACKGROUND,
        /**
         * The result error.
         */
        RESULT_ERROR,
        /**
         * The result security exception.
         */
        RESULT_SECURITY_EXCEPTION,
        /**
         * The result security elevatable.
         */
        RESULT_SECURITY_ELEVATABLE
    }

    /**
     * Executes a script with default permissions. This will allow sandboxed
     * access to the database, and gameplay data, without access to
     * authentication data. These scripts have access to
     * <code>Hashmap&lt;String, Object&gt;</code>-style maps used for extended
     * custom attributes, and may access various API classes via
     * {@link net.mosstest.scripting.JavaApi JavaApi}(which internally uses
     * reflection to obtain classes). Via an ACL, certain classes may be blocked
     * or replaced with limited versions thereof. At the time of writing, this
     * feature is incomplete and will not allow any access to the Java(tm) SE
     * API.
     *
     * @param script A string representing the script to run
     * @return A {@link ScriptEnv.ScriptResult} constant representing the
     * result.
     * @throws MossWorldLoadException the moss world load exception
     */
    public ScriptResult runScript(IMossFile script)
            throws MossWorldLoadException {

        try {
            Script sc = this.cx.compileReader(script.getReader(),
                    script.toString(), 0, null);

            Object lock = new Object();
            MosstestSecurityManager.instance.lock(lock,
                    ThreadContext.CONTEXT_SCRIPT);

            sc.exec(this.cx, this.globalScope);

            MosstestSecurityManager.instance.unlock(lock);
        } catch (IOException e) {
            return ScriptResult.RESULT_ERROR;
        } catch (RhinoException e) {
            logger.error("A script error has occured: " + e.getMessage());
            throw new MossWorldLoadException(
                    Messages.getString("ScriptEnv.ERR_SCRIPT_ERR") + e.getMessage() + "\r\n" + e.getScriptStackTrace()); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (Error e) {
            // We are really screwed with classloading if we reach this block
            logger.fatal("Caught error of type " + e.getClass().getName() + " with toString() of: " + e.toString() + ". This should not happen and implies a severe classloading error.");
        }
        return ScriptResult.RESULT_EXECUTED;
    }

    private Context cx;

    /**
     * Instantiates a new script env.
     *
     * @param ev the ev
     */
    public ScriptEnv(MossScriptEnv ev) {

        this.cx = ContextFactory.getGlobal().enterContext();
        this.globalScope = this.cx.initStandardObjects();
        this.globalScope.put("moss", this.globalScope, ev); //$NON-NLS-1$


    }


}
