package net.mosstest.servercore;

import java.util.HashMap;

import javax.script.Invocable;

import org.mozilla.javascript.*;

public class ScriptEnv {
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

	HashMap<String, Script> scriptMap = new HashMap<>();
	boolean localDb;


	

	

	public static class SandboxWrapFactory extends WrapFactory {
		@Override
		public Scriptable wrapAsJavaObject(Context cx, Scriptable scope,
				Object javaObject, Class staticType) {
			return new SandboxNativeJavaObject(scope, javaObject, staticType);
		}
	}

	public static class SandboxContextFactory extends ContextFactory {
		@Override
		protected Context makeContext() {
			Context cx = super.makeContext();
			cx.setWrapFactory(new SandboxWrapFactory());
			return cx;
		}
	}

	public static class SandboxNativeJavaObject extends NativeJavaObject {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4829780635666396547L;

		public SandboxNativeJavaObject(Scriptable scope, Object javaObject,
				Class staticType) {
			super(scope, javaObject, staticType);
		}

		@Override
		public Object get(String name, Scriptable start) {
			if (name.equals("getClass")) {
				return NOT_FOUND;
			}

			return super.get(name, start);
		}
	}
	

}
