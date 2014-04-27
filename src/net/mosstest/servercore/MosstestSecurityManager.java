package net.mosstest.servercore;

import net.mosstest.scripting.MossScriptEnv;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FilePermission;
import java.io.IOException;
import java.net.InetAddress;
import java.security.AccessController;
import java.security.Permission;
import java.text.MessageFormat;

// TODO: Auto-generated Javadoc

/**
 * The Class MosstestSecurityManager.
 */
public class MosstestSecurityManager extends SecurityManager {


    public static final int PORT_HTTP = 80;
    public static final int PORT_HTTPS = 443;
    public static final int MIN_SCRIPT_PORT = 16600;
    public static final int MAX_SCRIPT_PORT = 16512;
    /**
     * The logger.
     */
    static Logger logger = Logger.getLogger(MosstestSecurityManager.class);
    private final boolean testMode;

    public MosstestSecurityManager(boolean testMode) {
        this.testMode = testMode;
        if (testMode) logger.warn(Messages.getString("SECURITY_MANAGER_TESTMODE"));
        File classDir = null;
        try {
            classDir = new File(MossScriptEnv.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath()) // net.mosstest.scripting.MossScriptEnv
                    .getParentFile() // net.mosstest.scripting
                    .getCanonicalFile();
        } catch (IOException e) {
            logger.warn(Messages.getString("NO_CLASSDIR"));
        } finally {
            this.classDirectory = classDir;
        }
    }


    @Override
    public void checkPermission(Permission perm) {

        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {
            logger.fatal(MessageFormat.format(Messages.getString("PERMISSION_REQUESTED"), perm));
            if (testMode && perm.getName().equals("setIO")) return; //NON-NLS
            logger.warn(Messages.getString("ARBITRARY_PERMISSIONS"));

            throw new SecurityException(
                    Messages.getString("ARBITRARY_PERMISSIONS"));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkPermission(java.security.Permission,
     * java.lang.Object)
     */
    @Override
    public void checkPermission(Permission perm, Object context) {
        checkPermission(perm);
    }

    /**
     * The Constant instance.
     */
    public static final MosstestSecurityManager instance = new MosstestSecurityManager();

    /**
     * Gets the connected peer.
     *
     * @return the connected peer
     */
    public String getConnectedPeer() {
        return this.connectedPeer;
    }

    /**
     * The lock.
     */
    private InheritableThreadLocal<Object> lock = new InheritableThreadLocal<>();
    private File baseDirectory;
    // Class dir corresponding to "net.mosstest"
    private final File classDirectory;

    public void setTrustedBasedir(File basedir) throws SecurityException,
            IOException {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {
            logger.error(Messages.getString("SECURITY_TRUSTED_BASEDIR"));
            throw new SecurityException(Messages.getString("NO_SET_BASEDIR"));
        }
        this.baseDirectory = basedir.getCanonicalFile();
    }

    /**
     * Lock.
     *
     * @param key the key
     * @param tc  the tc
     */
    public void lock(Object key, ThreadContext tc) {
        if ((this.lock.get() != null)
                || this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {
            logger.error(Messages.getString("THREAD_LOCKED"));
            throw new SecurityException(
                    Messages.getString("ALREADY_LOCKED"));
        }
        this.lock.set(key);
        this.threadContext.set(tc);

    }

    private Object forceUnlock() {
        Object key = this.lock.get();
        this.lock.set(null);
        this.threadContext.set(ThreadContext.CONTEXT_ENGINE);
        return key;
    }

    /**
     * Unlock.
     *
     * @param key the key
     */
    public void unlock(Object key) {
        if (this.lock.get() != key) {
            logger.error(Messages.getString("SECURITY_MISMATCHED_KEY"));
            throw new SecurityException(
                    Messages.getString("SECURITY_BAD_KEY"));
        }
        this.threadContext.set(ThreadContext.CONTEXT_ENGINE);
    }

    /**
     * Sets the connected peer.
     *
     * @param connectedPeer the new connected peer
     */
    public void setConnectedPeer(String connectedPeer) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {
            logger.warn(Messages.getString("SECURITY_PEER_CHANGE"));
            throw new SecurityException(
                    "Attempted to set the connected peer from outside engine code");
        }

        this.connectedPeer = connectedPeer;
    }

    /**
     * Gets the thread context.
     *
     * @return the thread context
     */
    public ThreadContext getThreadContext() {
        return this.threadContext.get();
    }

    /**
     * Sets the thread context.
     *
     * @param tc the new thread context
     */
    public void setThreadContext(ThreadContext tc) {
        if (this.threadContext.get() == null) {
            logger.warn(Messages.getString("THREAD_ELEVATED"));
            this.threadContext.set(tc);

            return;
        }
        ThreadContext old = this.threadContext.get();
        if (tc == ThreadContext.CONTEXT_LOCKDOWN) {
            this.threadContext.set(tc);
        }
        if (old != ThreadContext.CONTEXT_ENGINE) {
            logger.warn(Messages.getString("THREAD_CONTEXT_NON_ENGINE_CODE"));
            throw new SecurityException(Messages.getString("THREAD_CONTEXT_NON_ENGINE_CODE"));
        }
    }

    /**
     * The Enum ThreadContext.
     */
    public enum ThreadContext {

        /**
         * The context engine.
         */
        CONTEXT_ENGINE,
        /**
         * The context script.
         */
        CONTEXT_SCRIPT,
        /**
         * The context client.
         */
        CONTEXT_CLIENT,
        /**
         * The context lockdown.
         */
        CONTEXT_LOCKDOWN
    }

    /**
     * The connected peer.
     */
    private String connectedPeer = null;

    /**
     * The thread context.
     */
    private InheritableThreadLocal<ThreadContext> threadContext = new InheritableThreadLocal<MosstestSecurityManager.ThreadContext>() {
        @Override
        protected ThreadContext initialValue() {
            logger.warn(Messages.getString("THREAD_ELEVATED"));
            return ThreadContext.CONTEXT_ENGINE;
        }
    };

    {
        this.threadContext.set(ThreadContext.CONTEXT_ENGINE);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkCreateClassLoader()
     */
    @Override
    public void checkCreateClassLoader() {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_CLASSLOADER"));
            throw new SecurityException(
                    Messages.getString("SECURITY_CLASSLOADER"));

        } else {
            super.checkCreateClassLoader();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkAccess(java.lang.Thread)
     */
    @Override
    public void checkAccess(Thread t) {
        if (testMode) return;
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_THREAD"));
            throw new SecurityException(
                    Messages.getString("SECURITY_THREAD"));

        } else {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkAccess(java.lang.ThreadGroup)
     */
    @Override
    public void checkAccess(ThreadGroup g) {
        if (testMode) return;
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_THREADGROUP"));
            throw new SecurityException(
                    Messages.getString("SECURITY_THREADGROUP"));

        } else {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkExit(int)
     */
    @Override
    public void checkExit(int status) {
        if (testMode) return;
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_EXIT"));
            throw new SecurityException(
                    Messages.getString("SECURITY_EXIT"));

        }
        super.checkExit(status);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkExec(java.lang.String)
     */
    @Override
    public void checkExec(String cmd) {

        logger.warn(Messages.getString("SECURITY_SPAWN_PROC"));
        throw new SecurityException(
                Messages.getString("SECURITY_SPAWN_PROC"));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkLink(java.lang.String)
     */
    @Override
    public void checkLink(String lib) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_LINK"));
            throw new SecurityException(
                    Messages.getString("SECURITY_LINK"));

        }

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkRead(java.io.FileDescriptor)
     */
    @Override
    public void checkRead(FileDescriptor fd) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(MessageFormat.format(Messages.getString("SECURITY_READFILE")
                    , fd.toString()));
            throw new SecurityException(
                    MessageFormat.format(Messages.getString("SECURITY_READFILE"), fd.toString())
            );

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkRead(java.lang.String)
     */
    @Override
    public void checkRead(String file) {

        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {
//            List<File> safeDirectories = Arrays.asList(this.baseDirectory, this.classDirectory);
            ThreadContext oldTc = MosstestSecurityManager.this
                    .getThreadContext();
            Object oldLock = MosstestSecurityManager.this.forceUnlock();
            File tested;
//            try {
//
//
//                tested = new File(file).getCanonicalFile();
//                //MosstestSecurityManager.this.lock(oldLock, oldTc);
//            } catch (IOException e1) {
//                throw new SecurityException("The base directory failed to resolve!", e1);
//            }
//
//            do {
//                if (safeDirectories.contains(file)) {
//                    MosstestSecurityManager.this.lock(oldLock, oldTc);
//                    return;
//                }
//                tested = tested.getParentFile();
//          } while (tested != null);
            try {
                AccessController.checkPermission(new FilePermission(file, "read")); //NON-NLS
            } catch (SecurityException e) {
                // lock
                this.lock(oldLock, oldTc);
                // rethrow to bail to a failure
                throw e;
            }

            super.checkRead(file);

            this.lock(oldLock, oldTc);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkRead(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void checkRead(String file, Object context) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(MessageFormat.format(Messages.getString("SECURITY_READ")
                    , file));
            throw new SecurityException(
                    MessageFormat.format(Messages.getString("SECURITY_READ")
                            , file)
            );

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkWrite(java.io.FileDescriptor)
     */
    @Override
    public void checkWrite(FileDescriptor fd) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_WRITE"));
            throw new SecurityException(
                    Messages.getString("SECURITY_WRITE"));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkWrite(java.lang.String)
     */
    @Override
    public void checkWrite(String file) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_WRITE"));
            throw new SecurityException(
                    Messages.getString("SECURITY_WRITE"));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkDelete(java.lang.String)
     */
    @Override
    public void checkDelete(String file) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_DELETE"));
            throw new SecurityException(
                    "MosstestSecurityManager stopped an attempt to delete a file from non-core code");

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkConnect(java.lang.String, int)
     */
    @Override
    public void checkConnect(String host, int port) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SECURITY_CONNECT"));
            throw new SecurityException(Messages.getString("SECURITY_CONNECT"));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkConnect(java.lang.String, int,
     * java.lang.Object)
     */
    @Override
    public void checkConnect(String host, int port, Object context) {
        if (this.threadContext.get() == ThreadContext.CONTEXT_CLIENT) {


            logger.warn(Messages.getString("SECURITY_LISTEN"));
            throw new SecurityException(
                    "MosstestSecurityManager stopped an attempt to listen directly on a network port.");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkListen(int)
     */
    @Override
    public void checkListen(int port) {
        if (this.threadContext.get() == ThreadContext.CONTEXT_SCRIPT) {


            logger.warn(Messages.getString("SECURITY_LISTEN"));
            throw new SecurityException(
                    Messages.getString("SECURITY_LISTEN"));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkAccept(java.lang.String, int)
     */
    @Override
    public void checkAccept(String host, int port) {
        if (this.threadContext.get() == ThreadContext.CONTEXT_SCRIPT) {

            logger.warn(Messages.getString("SECURITY_LISTEN"));
            throw new SecurityException(
                    Messages.getString("SECURITY_LISTEN"));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkMulticast(java.net.InetAddress)
     */
    @Override
    public void checkMulticast(InetAddress maddr) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("MULTICAST"));
            throw new SecurityException(
                    Messages.getString("MULTICAST2"));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkMulticast(java.net.InetAddress, byte)
     */
    @Override
    public void checkMulticast(InetAddress maddr, byte ttl) {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("MULTICAST"));
            throw new SecurityException(
                    Messages.getString("MULTICAST2"));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkPropertiesAccess()
     */
    @Override
    public void checkPropertiesAccess() {
        if (testMode) return;
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SYS_PROPERTIES"));
            throw new SecurityException(
                    Messages.getString("SYS_PROPERTIES_SHORT"));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkPropertyAccess(java.lang.String)
     */
    @Override
    public void checkPropertyAccess(String key) {
        if (testMode) return;
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SYS_PROPERTIES"));
            throw new SecurityException(
                    MessageFormat.format(Messages.getString("SYS_PROPERTIES_SHORT_NAMED"), key));

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkTopLevelWindow(java.lang.Object)
     */
    @Override
    public boolean checkTopLevelWindow(Object window) {
        if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN) {

            logger.warn(Messages.getString("TOP_LEVEL"));
            throw new SecurityException(
                    Messages.getString("TOP_LEVEL_SHORT"));
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkPrintJobAccess()
     */
    @Override
    public void checkPrintJobAccess() {

        logger.warn(Messages.getString("PRINT"));
        throw new SecurityException(
                Messages.getString("PRINT_SHORT"));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkSystemClipboardAccess()
     */
    @Override
    public void checkSystemClipboardAccess() {
        if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN) {

            logger.warn(Messages.getString("CLIPBOARD"));
            throw new SecurityException(
                    Messages.getString("CLIPBOARD_SHORT"));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkAwtEventQueueAccess()
     */
    @Override
    public void checkAwtEventQueueAccess() {
        if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN) {

            logger.warn(Messages.getString("AWT_QUEUE"));
            throw new SecurityException(
                    Messages.getString("AWT_QUEUE_SHORT"));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkPackageAccess(java.lang.String)
     */
    @Override
    public void checkPackageAccess(String pkg) {
        super.checkPackageAccess(pkg);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkPackageDefinition(java.lang.String)
     */
    @Override
    public void checkPackageDefinition(String pkg) {
        super.checkPackageDefinition(pkg);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkSetFactory()
     */
    @Override
    public void checkSetFactory() {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("SYS_FACTORY"));
            throw new SecurityException(
                    Messages.getString("SYS_FACTORY_SHORT"));
        }
        super.checkSetFactory();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkMemberAccess(java.lang.Class, int)
     */
    @Override
    public void checkMemberAccess(Class<?> clazz, int which) {
        super.checkMemberAccess(clazz, which);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#checkSecurityAccess(java.lang.String)
     */
    @Override
    public void checkSecurityAccess(String target) {
        super.checkSecurityAccess(target);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.SecurityManager#getThreadGroup()
     */
    @Override
    public ThreadGroup getThreadGroup() {
        return super.getThreadGroup();
    }

    /**
     * Check mosstest control.
     */
    public void checkMosstestControl() {
        if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

            logger.warn(Messages.getString("PLUGIN_CTRL"));
            throw new SecurityException(
                    Messages.getString("CTRL_SHORT"));
        }
        super.checkSetFactory();
    }

    public MosstestSecurityManager() {
        this(false);
    }

}
