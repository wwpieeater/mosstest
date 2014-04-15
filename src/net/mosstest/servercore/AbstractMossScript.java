package net.mosstest.servercore;

import java.io.IOException;
import java.util.List;

public abstract class AbstractMossScript {

    protected AbstractMossScript(String name) {
        this.name = name;
    }

    final String name;
    abstract void exec(ScriptEnv sEnv) throws IOException, MossWorldLoadException;

    abstract IMossFile getInitFile() throws IOException;

    abstract List<AbstractMossScript> getDependencies();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractMossScript)) return false;

        AbstractMossScript that = (AbstractMossScript) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }
}
