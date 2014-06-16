<<<<<<< HEAD
package net.mosstest.servercore;

import java.io.IOException;
import java.util.List;

public abstract class AbstractMossScript {

    protected AbstractMossScript(String name) {
        this.name = name;
    }

    @org.jetbrains.annotations.NonNls
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
=======
package net.mosstest.servercore;

import java.io.IOException;
import java.util.List;

public abstract class AbstractMossScript {

    protected AbstractMossScript(String name) {
        this.name = name;
    }

    @org.jetbrains.annotations.NonNls
    protected final String name;
    public abstract void exec(ScriptEnv sEnv) throws IOException, MossWorldLoadException;

    public abstract IMossFile getInitFile() throws IOException;

    public abstract List<AbstractMossScript> getDependencies();

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
>>>>>>> netclient
