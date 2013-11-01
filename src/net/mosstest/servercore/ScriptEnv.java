package net.mosstest.servercore;

import java.io.IOException;

import net.mosstest.scripting.MossScriptEnv;

import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

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
	 * feature is incomplete and will not allow any access to the Java(tm) SE
	 * API.
	 * 
	 * @param script
	 *            A string representing the script to run
	 * @return A {@link ScriptEnv.ScriptResult} constant representing the
	 *         result.
	 */
	public ScriptResult runScript(MossScript script)
			throws MossWorldLoadException {
		try {
			this.cx.evaluateReader(this.globalScope, script.getReader(),
					script.file.toString(), 0, null);
		} catch (IOException e) {
			return ScriptResult.RESULT_ERROR;
		} catch (RhinoException e) {
			System.out.println(e.getMessage());
			throw new MossWorldLoadException(
					"Script error has occured. Wrapped exception: \r\n" + e.getMessage() + "\r\n" + e.getScriptStackTrace()); //$NON-NLS-1$
		}
		return ScriptResult.RESULT_EXECUTED;
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
		this.globalScope = this.cx.initStandardObjects();
		this.globalScope.put("moss", this.globalScope, ev);
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
