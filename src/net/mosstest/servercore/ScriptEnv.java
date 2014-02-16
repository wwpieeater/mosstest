package net.mosstest.servercore;

import java.io.IOException;

import net.mosstest.scripting.MossScriptEnv;

import org.apache.log4j.Logger;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

// TODO: Auto-generated Javadoc
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
	
	/** The Constant sandboxerScript. */
	public static final String sandboxerScript = "java = undefined;\r\n" + 
			"Packages = undefined;\r\n" + 
			"org = undefined;";
	
	/** The logger. */
	static Logger logger = Logger.getLogger(MossDebugUtils.class);
	
	/** The global scope. */
	ScriptableObject globalScope;

	/**
	 * The Class ScriptClassShutter.
	 */
	private static class ScriptClassShutter implements ClassShutter {
		
		/**
		 * Instantiates a new script class shutter.
		 */
		public ScriptClassShutter() {

		}

		/* (non-Javadoc)
		 * @see org.mozilla.javascript.ClassShutter#visibleToScripts(java.lang.String)
		 */
		public boolean visibleToScripts(String className) {
			if (className.startsWith("adapter") //$NON-NLS-1$
					|| className.startsWith("net.mosstest.scripting")) //$NON-NLS-1$
				return true;
			else
				return false;
		}
	}

	/**
	 * The Enum ScriptResult.
	 */
	public enum ScriptResult {
		
		/** The result executed. */
		RESULT_EXECUTED, 
 /** The result executng background. */
 RESULT_EXECUTNG_BACKGROUND, 
 /** The result error. */
 RESULT_ERROR, 
 /** The result security exception. */
 RESULT_SECURITY_EXCEPTION, 
 /** The result security elevatable. */
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
	 * @param script            A string representing the script to run
	 * @return A {@link ScriptEnv.ScriptResult} constant representing the
	 *         result.
	 * @throws MossWorldLoadException the moss world load exception
	 */
	public ScriptResult runScript(MossScript script)
			throws MossWorldLoadException {
		try {
			Script sc = this.cx.compileReader(script.getReader(),
					script.file.toString(), 0, null);
		} catch (IOException e) {
			return ScriptResult.RESULT_ERROR;
		} catch (RhinoException e) {
			logger.error("A script error has occured: "+e.getMessage());
			throw new MossWorldLoadException(
					Messages.getString("ScriptEnv.ERR_SCRIPT_ERR") + e.getMessage() + "\r\n" + e.getScriptStackTrace()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return ScriptResult.RESULT_EXECUTED;
	}

	/**
	 * A factory for creating SandboxWrap objects.
	 */
	protected static class SandboxWrapFactory extends WrapFactory {
		
		/* (non-Javadoc)
		 * @see org.mozilla.javascript.WrapFactory#wrapAsJavaObject(org.mozilla.javascript.Context, org.mozilla.javascript.Scriptable, java.lang.Object, java.lang.Class)
		 */
		@Override
		public Scriptable wrapAsJavaObject(Context cx, Scriptable scope,
				Object javaObject, Class<?> staticType) {
			return new SandboxNativeJavaObject(scope, javaObject, staticType);
		}
	}

	/**
	 * A factory for creating SandboxContext objects.
	 */
	protected static class SandboxContextFactory extends ContextFactory {

		/* (non-Javadoc)
		 * @see org.mozilla.javascript.ContextFactory#makeContext()
		 */
		@Override
		protected Context makeContext() {
			Context cx = super.makeContext();
			cx.setClassShutter(new ScriptClassShutter());
			cx.setWrapFactory(new SandboxWrapFactory());
			return cx;
		}
	}

	/** The cx. */
	private Context cx;

	/**
	 * Instantiates a new script env.
	 *
	 * @param ev the ev
	 */
	public ScriptEnv(MossScriptEnv ev) {
		ContextFactory.initGlobal(new SandboxContextFactory());
		this.cx = ContextFactory.getGlobal().enterContext();
		this.globalScope = this.cx.initStandardObjects();
		this.globalScope.put("moss", this.globalScope, ev); //$NON-NLS-1$
	}

	/**
	 * The Class SandboxNativeJavaObject.
	 */
	public static class SandboxNativeJavaObject extends NativeJavaObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 4829780635666396547L;

		/**
		 * Instantiates a new sandbox native java object.
		 *
		 * @param scope the scope
		 * @param javaObject the java object
		 * @param staticType the static type
		 */
		public SandboxNativeJavaObject(Scriptable scope, Object javaObject,
				Class<?> staticType) {
			super(scope, javaObject, staticType);
		}

		/* (non-Javadoc)
		 * @see org.mozilla.javascript.NativeJavaObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		@Override
		public Object get(String name, Scriptable start) {
			if (name.equals("getClass")) { //$NON-NLS-1$
				return NOT_FOUND;
			}

			return super.get(name, start);
		}
	}

}
