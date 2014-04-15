package net.mosstest.scripting;

import org.apache.log4j.Logger;

public class ScriptDebugUtils {
    public static boolean primed = false;
    private static final Logger logger = Logger.getLogger(ScriptDebugUtils.class);
    public static void hitBreakpoint(String s){
        logger.warn("Hit breakpoint "+s);
        System.currentTimeMillis(); // dummy for breakpoint
    }

    public static void primeBreakpoint(){
        primed = true;
    }
}
