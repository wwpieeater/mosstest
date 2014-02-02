package net.mosstest.servercore;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

import org.apache.log4j.Logger;

public class MosstestSecurityManager extends SecurityManager {
	static Logger logger = Logger.getLogger(MosstestSecurityManager.class);

	@Override
	public void checkPermission(Permission perm) {
		System.err.println(perm.toString());
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager prevented the use of arbitrary permissions outside engine contexts.");
			throw new SecurityException(
					"MosstestSecurityManager prevented the use of arbitrary permissions outside engine contexts.");

		}
	}

	@Override
	public void checkPermission(Permission perm, Object context) {

		checkPermission(perm);
	}

	public static final MosstestSecurityManager instance = new MosstestSecurityManager();

	public String getConnectedPeer() {
		return this.connectedPeer;
	}

	private InheritableThreadLocal<Object> lock;

	public void lock(Object key, ThreadContext tc) {
		if (this.lock.get() != null) {
			logger.error("The security manager prevented an attempt to lock it on an already-locked thread.");
			throw new SecurityException(
					"The security manager is already locked for this thread.");
		}
		this.lock.set(key);
		this.threadContext.set(tc);
	}

	public void unlock(Object key) {
		if (this.lock.get() != key) {
			logger.error("The security manager prevented an attempt to unlock it using a mismatched key.");
			throw new SecurityException(
					"A mismatched key has been used to unlock this thread.");
		}
		this.threadContext.set(ThreadContext.CONTEXT_ENGINE);
	}

	public void setConnectedPeer(String connectedPeer) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {
			logger.warn("MosstestSecurityManager prevented a non-engine-context thread from changing the connected peer.");
			throw new SecurityException(
					"Attempted to set the connected peer from outside engine code");
		}

		this.connectedPeer = connectedPeer;
	}

	public ThreadContext getThreadContext() {
		return this.threadContext.get();
	}

	public void setThreadContext(ThreadContext tc) {
		if (this.threadContext.get() == null) {
			logger.warn("A thread has started without inheriting a thread context and has been elevated");
			this.threadContext.set(tc);

			return;
		}
		ThreadContext old = this.threadContext.get();
		if (tc == ThreadContext.CONTEXT_LOCKDOWN) {
			this.threadContext.set(tc);
		}
		if (old != ThreadContext.CONTEXT_ENGINE) {
			logger.warn("Attempted to set the thread context type from non-engine code");
			throw new SecurityException(
					"Attempted to set the thread context type from non-engine code");
		}
	}

	public enum ThreadContext {
		CONTEXT_ENGINE, CONTEXT_SCRIPT, CONTEXT_CLIENT, CONTEXT_LOCKDOWN
	}

	private String connectedPeer = null;

	private InheritableThreadLocal<ThreadContext> threadContext = new InheritableThreadLocal<MosstestSecurityManager.ThreadContext>() {
		@Override
		protected ThreadContext initialValue() {
			logger.warn("A thread has started without inheriting a thread context and has been elevated");
			return ThreadContext.CONTEXT_ENGINE;
		}
	};

	{
		this.threadContext.set(ThreadContext.CONTEXT_ENGINE);

	}

	@Override
	public void checkCreateClassLoader() {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to create a classloader");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to create a classloader");

		} else {
			super.checkCreateClassLoader();
			return;
		}

	}

	@Override
	public void checkAccess(Thread t) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to stop or modify a thread");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to stop or modify a thread");

		} else {
			return;
		}
	}

	@Override
	public void checkAccess(ThreadGroup g) {
		System.err.println(this.threadContext.get());
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to modify a ThreadGroup");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to modify a ThreadGroup");

		} else {
			return;
		}
	}

	@Override
	public void checkExit(int status) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to exit Mosstest improperly from a script");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to exit Mosstest improperly from a script");

		}
		super.checkExit(status);

	}

	@Override
	public void checkExec(String cmd) {

		logger.warn("MosstestSecurityManager does not allow any script or portion of the engine to start a new process");
		throw new SecurityException(
				"MosstestSecurityManager does not allow any script or portion of the engine to start a new process");
	}

	@Override
	public void checkLink(String lib) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to link to a JN library from insecure code");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to link to a JN library from insecure code");

		}

	}

	@Override
	public void checkRead(FileDescriptor fd) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to read a file from non-core code");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to read a file from non-core code");

		}
	}

	@Override
	public void checkRead(String file) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to read a file from non-core code");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to read a file from non-core code");

		}
	}

	@Override
	public void checkRead(String file, Object context) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to read a file from non-core code");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to read a file from non-core code");

		}
	}

	@Override
	public void checkWrite(FileDescriptor fd) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to write to a file from non-core code");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to write to a file from non-core code");

		}
	}

	@Override
	public void checkWrite(String file) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to write to a file from non-core code");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to write to a file from non-core code");

		}
	}

	@Override
	public void checkDelete(String file) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager stopped an attempt to delete a file from non-core code");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to delete a file from non-core code");

		}
	}

	@Override
	public void checkConnect(String host, int port) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {
			if ((port != 80 && port != 443) && (port > 16600 || port < 16512))

				logger.warn("MosstestSecurityManager stopped an attempt to connect to a network port other than 80, 443, or any port in the range 16512-16600");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to connect to a network port other than 80, 443, or any port in the range 16512-16600");

		}
	}

	@Override
	public void checkConnect(String host, int port, Object context) {
		if (this.threadContext.get() == ThreadContext.CONTEXT_CLIENT) {
			if ((port != 80 && port != 443) && (port > 16600 || port < 16512)) {

				logger.warn("MosstestSecurityManager stopped an attempt to connect to a network port other than 80, 443, or any port in the range 16512-16600");
				throw new SecurityException(
						"MosstestSecurityManager stopped an attempt to connect to a network port other than 80, 443, or any port in the range 16512-16600");
			}
			if (host != this.connectedPeer) {

				throw new SecurityException(
						"MosstestSecurityManager stopped an attempt for the client to connect to a peer other than the server currently played");
			}
		} else if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN) {

			logger.warn("MosstestSecurityManager has denied a thread in lockdown to open a network connection");
			throw new SecurityException(
					"MosstestSecurityManager has denied a thread in lockdown to open a network connection");
		}
	}

	@Override
	public void checkListen(int port) {
		if (this.threadContext.get() == ThreadContext.CONTEXT_SCRIPT) {
			if ((port > 16600 || port < 16512))

				logger.warn("MosstestSecurityManager stopped an attempt to listen on a port not in the range 16512-16600");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to listen on a port not in the range 16512-16600");

		} else if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN
				|| this.threadContext.get() == ThreadContext.CONTEXT_CLIENT) {

			logger.warn("MosstestSecurityManager has denied a thread in lockdown or running a client script to listen on a port");
			throw new SecurityException(
					"MosstestSecurityManager has denied a thread in lockdown or running a client script to listen on a port");
		}
	}

	@Override
	public void checkAccept(String host, int port) {
		if (this.threadContext.get() == ThreadContext.CONTEXT_SCRIPT) {
			if ((port > 16600 || port < 16512))

				logger.warn("MosstestSecurityManager stopped an attempt to listen on a port not in the range 16512-16600");
			throw new SecurityException(
					"MosstestSecurityManager stopped an attempt to listen on a port not in the range 16512-16600");

		} else if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN
				|| this.threadContext.get() == ThreadContext.CONTEXT_CLIENT) {

			logger.warn("MosstestSecurityManager has denied a thread in lockdown or running a client script to listen on a port");
			throw new SecurityException(
					"MosstestSecurityManager has denied a thread in lockdown or running a client script to listen on a port");
		}
	}

	@Override
	public void checkMulticast(InetAddress maddr) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager has denied a script use of multicast directly without use of loadbalancing facilities");
			throw new SecurityException(
					"Scripts may not use mutlicast under any circumstances, except through the engine loadbalancing facilities");

		}
	}

	@Override
	public void checkMulticast(InetAddress maddr, byte ttl) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager has denied a script use of multicast directly without use of loadbalancing facilities");
			throw new SecurityException(
					"Scripts may not use mutlicast under any circumstances, except through the engine loadbalancing facilities");

		}
	}

	@Override
	public void checkPropertiesAccess() {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager has denied a thread running outside the engine context the right to access system properties.");
			throw new SecurityException(
					"Scripts may not access system properties");

		}
	}

	@Override
	public void checkPropertyAccess(String key) {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager has denied a thread running outside the engine context the right to access system properties.");
			throw new SecurityException(
					"Scripts may not access system properties");

		}
	}

	@Override
	public boolean checkTopLevelWindow(Object window) {
		if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN) {

			logger.warn("MosstestSecurityManager has blocked a thread running outside the engine context frop opening a top-level window.");
			throw new SecurityException(
					"Threads in lockdown may not create top-level windows");
		}
		return true;
	}

	@Override
	public void checkPrintJobAccess() {

		logger.warn("MosstestSecurityManager has denied a thread running outside the engine context the right to access system print jobs.");
		throw new SecurityException(
				"Print job access is not allowed for the engine or scripts");
	}

	@Override
	public void checkSystemClipboardAccess() {
		if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN) {

			logger.warn("MosstestSecurityManager has prevented a thread in lockdown from accessing the system clipboard.");
			throw new SecurityException(
					"Threads in lockdown may not access the system clipboard");
		}
	}

	@Override
	public void checkAwtEventQueueAccess() {
		if (this.threadContext.get() == ThreadContext.CONTEXT_LOCKDOWN) {

			logger.warn("MosstestSecurityManager has prevented a thread in lockdown from accessing the AWT queue.");
			throw new SecurityException(
					"Threads in lockdown may not access the AWT queue");
		}
	}

	@Override
	public void checkPackageAccess(String pkg) {
		super.checkPackageAccess(pkg);
	}

	@Override
	public void checkPackageDefinition(String pkg) {
		super.checkPackageDefinition(pkg);
	}

	@Override
	public void checkSetFactory() {
		if (this.threadContext.get() != ThreadContext.CONTEXT_ENGINE) {

			logger.warn("MosstestSecurityManager has prevented a thread in lockdown from setting a Java system factory.");
			throw new SecurityException(
					"Non-engine threads may not set java system factories");
		}
		super.checkSetFactory();
	}

	@Override
	public void checkMemberAccess(Class<?> clazz, int which) {
		super.checkMemberAccess(clazz, which);
	}

	@Override
	public void checkSecurityAccess(String target) {
		super.checkSecurityAccess(target);
	}

	@Override
	public ThreadGroup getThreadGroup() {
		return super.getThreadGroup();
	}

}
