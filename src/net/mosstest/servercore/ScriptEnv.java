package net.mosstest.servercore;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.script.Invocable;

import net.mosstest.scripting.MossScriptEnv;
import net.mosstest.scripting.ScriptableDatabase;

import org.mozilla.javascript.*;

/**
 * Static environment for executing scripts. Call {@link ScriptEnv.runScript()}
 * to call a script.
 * 
 * This class is made possible by the guidance given by blogger Jani Hartikainen
 * in <a href="http://codeutopia.net/blog/2009/01/02/sandboxing-rhino-in-java/">
 * Sandboxing Rhino in Java</a>.
 * 
 * @author rarkenin
 */
public class ScriptEnv {
	ScriptableObject globalScope;
	private static class ScriptClassShutter implements ClassShutter {
		public ScriptClassShutter() {

		}

		public boolean visibleToScripts(String className) {
			if (className.startsWith("adapter") //$NON-NLS-1$
					|| className.startsWith("net.mosstest.scripting")) //$NON-NLS-1$
				return true;
			else
				return false;
		}
	}


	private ScriptableDatabase localDb;

	public enum ScriptResult {
		RESULT_EXECUTED, RESULT_EXECUTNG_BACKGROUND, RESULT_ERROR, RESULT_SECURITY_EXCEPTION, RESULT_SECURITY_ELEVATABLE
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
	 * feature is incomplete and will use a limited ACL.
	 * 
	 * @param script
	 *            A string representing the script to run
	 * @return A {@link ScriptEnv.ScriptResult} constant representing the
	 *         result.
	 */
	public ScriptResult runScript(MossScript script) {
		try {
			this.cx.evaluateReader(globalScope, script.getReader(), script.file.toString(), 0, null);
		} catch (IOException e) {
			return ScriptResult.RESULT_ERROR;
		} catch (MossWorldLoadException e) {
			return ScriptResult.RESULT_ERROR;
		}
		return ScriptResult.RESULT_EXECUTED;
	}

	public Future<ScriptResult> runScriptAsync(MossScript script) {
		return new Future<ScriptEnv.ScriptResult>() {

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ScriptResult get() throws InterruptedException,
					ExecutionException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ScriptResult get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException,
					TimeoutException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isDone() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}

	public ScriptResult runScriptSuper(MossScript script) {
		return null;
	}

	protected static class SandboxWrapFactory extends WrapFactory {
		@Override
		public Scriptable wrapAsJavaObject(Context cx, Scriptable scope,
				Object javaObject, Class<?> staticType) {
			return new SandboxNativeJavaObject(scope, javaObject, staticType);
		}
	}

	protected static class SandboxContextFactory extends ContextFactory {
		
		@Override
		protected Context makeContext() {
			Context cx = super.makeContext();
			cx.setClassShutter(new ScriptClassShutter());
			cx.setWrapFactory(new SandboxWrapFactory());
			return cx;
		}
	}

	private Context cx;

	public ScriptEnv(MossScriptEnv ev) {
		ContextFactory.initGlobal(new SandboxContextFactory());
		this.cx = ContextFactory.getGlobal().enterContext();
		globalScope=this.cx.initStandardObjects();
		globalScope.put("moss", globalScope, ev);
	}

	public static class SandboxNativeJavaObject extends NativeJavaObject {

		private static final long serialVersionUID = 4829780635666396547L;

		public SandboxNativeJavaObject(Scriptable scope, Object javaObject,
				Class<?> staticType) {
			super(scope, javaObject, staticType);
		}

		@Override
		public Object get(String name, Scriptable start) {
			if (name.equals("getClass")) { //$NON-NLS-1$
				return NOT_FOUND;
			}

			return super.get(name, start);
		}
	}

}
