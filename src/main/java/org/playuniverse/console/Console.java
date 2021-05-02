package org.playuniverse.console;

import java.util.Queue;
import java.util.Scanner;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.syntaxphoenix.syntaxapi.command.CommandManager;
import com.syntaxphoenix.syntaxapi.logging.ILogger;
import com.syntaxphoenix.syntaxapi.logging.LogTypeId;
import com.syntaxphoenix.syntaxapi.logging.LoggerState;
import com.syntaxphoenix.syntaxapi.logging.SynLogger;
import com.syntaxphoenix.syntaxapi.logging.color.LogType;
import com.syntaxphoenix.syntaxapi.logging.color.LogTypeMap;
import com.syntaxphoenix.syntaxapi.random.Keys;

public class Console implements ILogger {

    private final HashMap<Integer, Consumer<String>> requests = new HashMap<>();
    private final Queue<Integer> queue = new LinkedList<>();

    private final CommandManager commands = new CommandManager();
    public final SynLogger logger;
    private final Scanner scanner;

    private final Thread watchDogThread;
    private final Thread inputThread;
    private final ConsoleFlag[] flags;

    public Console(InputStream input, PrintStream output, ConsoleFlag... flags) {
        logger = new SynLogger(output).setColored(true).setState(LoggerState.STREAM).setDefaultTypes();
        scanner = new Scanner(input);

        commands.setLogger(logger);
        commands.setPrefix("/");

        inputThread = new Thread(() -> onQueue());
        watchDogThread = new Thread(() -> onWatch());
        inputThread.start();
        watchDogThread.start();

        if (flags != null)
            this.flags = flags;
        else
            this.flags = new ConsoleFlag[0];
    }

    /*
     * 
     */

    private int lastInput = 0;

    /*
     * 
     */

    private void onWatch() {
        Thread.currentThread().setName("Input WatchDog");
        while (true) {
            if (flags.length != 0)
                for (ConsoleFlag flag : flags) {
                    WatchAction action = null;
                    if ((action = flag.onWatchDog(this, new ConsoleInfo(lastInput))) == null)
                        continue;
                    if (action == WatchAction.SHUTDOWN) {
                        shutdown();
                        return;
                    }
                }
            lastInput++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(e);
                shutdown();
                return;
            }
        }
    }

    private void onQueue() {
        Thread.currentThread().setName("Input Request Queue");
        while (true) {
            while (scanner.hasNextLine()) {
                lastInput = 0;
                String line = scanner.nextLine();
                if (!line.startsWith("/")) {
                    if (!queue.isEmpty()) {
                        if (flags.length != 0) {
                            QueueAction action = null;
                            for (ConsoleFlag flag : flags) {
                                if ((action = flag.onQueue(this, new ConsoleInfo(line))) == null)
                                    continue;
                                if (action == QueueAction.ABORT) {
                                    break;
                                }
                            }
                            if (action == QueueAction.ABORT)
                                continue;
                        }
                        int id = queue.poll();
                        if (requests.containsKey(id))
                            requests.remove(id).accept(line);
                    }
                    continue;
                }
                commands.execute(line);
            }
        }
    }

    /*
     * 
     */

    public final void shutdown() {
        if (Thread.currentThread() != watchDogThread)
            watchDogThread.interrupt();
        inputThread.interrupt();
        onShutdown();
        if (flags.length != 0)
            for (ConsoleFlag flag : flags)
                flag.onShutdown(this);
    }

    private void onShutdown() {}

    /*
     * 
     */

    public CommandManager getCommandManager() {
        return commands;
    }
    
    public SynLogger getLogger() {
        return logger;
    }

    public int request(Consumer<String> request) {
        if (request == null)
            return 0;
        int id = Keys.generateInt(9);
        while (requests.containsKey(id) || id == 0) {
            id = Keys.generateInt(9);
        }
        requests.put(id, request);
        queue.add(id);
        return id;
    }

    public boolean isQueued(int id) {
        return queue.contains(id);
    }

    public boolean isRequested(int id) {
        return requests.containsKey(id);
    }

    /*
     * 
     * 
     * 
     */

    @Override
    public ILogger log(String message) {
        return logger.log(message);
    }

    @Override
    public ILogger log(LogTypeId type, String message) {
        return logger.log(type, message);
    }

    @Override
    public ILogger log(String typeId, String message) {
        return logger.log(typeId, message);
    }

    @Override
    public ILogger log(String... messages) {
        return logger.log(messages);
    }

    @Override
    public ILogger log(LogTypeId type, String... messages) {
        return logger.log(type, messages);
    }

    @Override
    public ILogger log(String typeId, String... messages) {
        return logger.log(typeId, messages);
    }

    @Override
    public ILogger log(Throwable throwable) {
        return logger.log(throwable);
    }

    @Override
    public ILogger log(LogTypeId type, Throwable throwable) {
        return logger.log(type, throwable);
    }

    @Override
    public ILogger log(String typeId, Throwable throwable) {
        return logger.log(typeId, throwable);
    }

    @Override
    public ILogger close() {
        shutdown();
        return logger.close();
    }

    @Override
    public BiConsumer<Boolean, String> getCustom() {
        return logger.getCustom();
    }

    @Override
    public LoggerState getState() {
        return logger.getState();
    }

    @Override
    public String getThreadName() {
        return logger.getThreadName();
    }

    @Override
    public LogType getType(String typeId) {
        return logger.getType(typeId);
    }

    @Override
    public LogTypeMap getTypeMap() {
        return logger.getTypeMap();
    }

    @Override
    public boolean isColored() {
        return logger.isColored();
    }

    @Override
    public ILogger setColored(boolean colored) {
        return logger.setColored(colored);
    }

    @Override
    public ILogger setCustom(BiConsumer<Boolean, String> custom) {
        return logger.setCustom(custom);
    }

    @Override
    public ILogger setState(LoggerState state) {
        return logger.setState(state);
    }

    @Override
    public ILogger setThreadName(String name) {
        return logger.setThreadName(name);
    }

    @Override
    public ILogger setType(LogType type) {
        return logger.setType(type);
    }

}
