package org.nodetest.servercore;

import java.util.HashMap;

import javax.script.Invocable;

import org.mozilla.javascript.*;

public class ScriptEnv {

	HashMap<String, Script> scriptMap = new HashMap<>();
	boolean allowDb;
	private HashMap<String, Entity> entities;
	private GameMap map;
	private HashMap<String, Player> players;

	@Deprecated
	public ScriptEnv(boolean allowDb, GameWorld game) {
		super();
		// this.scriptMap = scriptMap;
		this.allowDb = allowDb;
	}

	public ScriptEnv(boolean isServer, HashMap<String, Player> players,
			GameMap map, HashMap<String, Entity> entities) {
		super();
		ContextFactory.initGlobal(new SandboxContextFactory());
		this.scriptMap = new HashMap<>();
		this.allowDb = isServer;
		this.map = map;
		this.entities = entities;
		this.players = players;
		Context cx = Context.enter();

		cx.setClassShutter(new ClassShutter() {

			public boolean visibleToScripts(String className) {
				if (className.startsWith("adapter")
						|| className.equals("MossScriptEnv")
						|| className.equals("MossScriptEnv.MossEventHandler")
						|| className.equals("EventProcessingCompletedSignal")
						|| className.equals("MossScriptException"))
					return true;

				else
					return false;
			}
		});
		Scriptable scope = cx.initStandardObjects();
		cx.evaluateString(scope, "function run(){print(\"foo123\");}", "foo",
				1, null);
		Invocable inv = (Invocable) cx;
		Runnable r = inv.getInterface(Runnable.class);
		r.run();

	}

	public static class SandboxWrapFactory extends WrapFactory {
		@Override
		public Scriptable wrapAsJavaObject(Context cx, Scriptable scope,
				Object javaObject, Class staticType) {
			return new SandboxNativeJavaObject(scope, javaObject, staticType);
		}
	}

	public class SandboxContextFactory extends ContextFactory {
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
