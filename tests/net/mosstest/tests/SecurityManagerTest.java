package net.mosstest.tests;

import net.mosstest.scripting.MapNode;
import net.mosstest.servercore.MosstestSecurityManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class SecurityManagerTest {
    static MosstestSecurityManager m;

    @BeforeClass
    public static void setUp() throws IOException {
        m = new MosstestSecurityManager(true);
        m.setTrustedBasedir(new File(""));
        System.setSecurityManager(m);
    }

    @Test
    public void testAccessUnlocked() {
        m.setThreadContext(MosstestSecurityManager.ThreadContext.CONTEXT_ENGINE);
        m.checkAccept("192.168.1.1", 13);
        m.checkRead("C:\\zoom.png");
        m.checkRead("/c/zoom.png");
        testLocked();
    }


    public void testLocked() {
        Object o = new Object();
        m.lock(o, MosstestSecurityManager.ThreadContext.CONTEXT_SCRIPT);
        try {
            m.checkAccept("192.168.1.1", 13);
            fail("CheckAccept passed wrongly!");
        } catch (SecurityException e) {
        }
        try {
            m.checkRead("C:\\lock.png");
            fail("Allowed an illegal Windows path");
        } catch (SecurityException e) {
        }
        try {
            m.checkRead("/c/linuxlock.png");
            System.out.println("LINUXLOCK");
            fail("Allowed an illegal unix path");
        } catch (SecurityException e) {
        }

        try {
            MapNode n = new MapNode("test", "test", "test", 0);
        } catch (SecurityException e) {
            fail("disallowed a classload");
        }
        m.unlock(o);
    }

}
