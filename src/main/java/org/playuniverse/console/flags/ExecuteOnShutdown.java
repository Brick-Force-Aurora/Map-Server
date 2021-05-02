package org.playuniverse.console.flags;

import java.util.Objects;
import java.util.function.Consumer;

import org.playuniverse.console.Console;
import org.playuniverse.console.ConsoleFlag;

class ExecuteOnShutdown implements ConsoleFlag {

    private final Consumer<Console> execute;

    public ExecuteOnShutdown(Consumer<Console> execute) {
        this.execute = Objects.requireNonNull(execute);
    }

    @Override
    public void onShutdown(Console console) {
        execute.accept(console);
    }

}
