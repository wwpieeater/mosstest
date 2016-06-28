package net.mosstest.servercore;

import net.mosstest.scripting.MossScriptEnv;
import net.mosstest.servercore.MosstestSecurityManager.ThreadContext;
import org.apache.log4j.Logger;
import org.mozilla.javascript.*;

import java.io.IOException;
import java.text.MessageFormat;

// TODO: Auto-generated Javadoc

/**
 * Static environment for executing scripts. Call {@link ScriptEnv#runScript(IMossFile)}
 * to call a script.
 *
 * @author rarkenin
 */
public class ScriptEnv {

    static Logger logger = Logger.getLogger(ScriptEnv.class);

    ImporterTopLevel globalScope;


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
     * authentication data. Via an ACL, certain classes may be blocked
     * or replaced with limited versions thereof. At the time of writing, this
     * feature uses a security manager to disallow anything that tries to access risky data or sockets.
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
            logger.fatal(MessageFormat.format(Messages.getString("SCRIPT_IOEXCEPTION"), script.getName(), e.getLocalizedMessage()));
            return ScriptResult.RESULT_ERROR;
        } catch (RhinoException e) {
            logger.error(MessageFormat.format(Messages.getString("SCRIPT_ERR"), e.getMessage()));
            throw new MossWorldLoadException(
                    MessageFormat.format(Messages.getString("ScriptEnv.ERR_SCRIPT_ERR"), e.getMessage()) + "\r\n" + e.getScriptStackTrace(), e); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (Error e) {
            // We are really screwed with classloading if we reach this block
            logger.fatal(MessageFormat.format(Messages.getString("CLASSLOADER_FAIL"), e.getClass().getName(), e.getLocalizedMessage()));
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
        this.globalScope = new ImporterTopLevel();
        this.globalScope.initStandardObjects(cx, false);
        cx.setOptimizationLevel(9);
        this.globalScope.put("moss", this.globalScope, ev); //$NON-NLS-1$


    }


}
