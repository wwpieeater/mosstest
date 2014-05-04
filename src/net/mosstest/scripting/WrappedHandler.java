package net.mosstest.scripting;

import net.mosstest.scripting.handlers.MossEventHandler;

/**
 * Created by hexafraction on 5/3/14.
 */
public class WrappedHandler {
    private final MossEventHandler handler;
    private final short requestedApiVer;
    public WrappedHandler(MossEventHandler handler, short requestedApiVer) {
        this.handler = handler;
        this.requestedApiVer = requestedApiVer;
    }

    public MossEventHandler getHandler() {
        return handler;
    }

    public short getRequestedApiVer() {
        return requestedApiVer;
    }
}
