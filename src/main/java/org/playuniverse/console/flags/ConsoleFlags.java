package org.playuniverse.console.flags;

import java.util.function.Consumer;

import org.playuniverse.console.Console;
import org.playuniverse.console.ConsoleFlag;

public final class ConsoleFlags {

    public static ConsoleFlag shutdownNoResponse(int seconds) {
        return new ShutdownWithoutResponseFlag(seconds);
    }

    public static ConsoleFlag exitSystemOnShutdown() {
        return new ExitSystemOnShutdown();
    }

    public static ConsoleFlag executeOnShutdown(Consumer<Console> execute) {
        return new ExecuteOnShutdown(execute);
    }

    public static ConsoleFlag executeOnShutdown(Runnable execute) {
        return executeOnShutdown(console -> execute.run());
    }

}
