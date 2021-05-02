package org.playuniverse.console;

public interface ConsoleFlag {
	
	default WatchAction onWatchDog(Console console, ConsoleInfo info) {
		return WatchAction.CONTINUE;
	}
	
	default QueueAction onQueue(Console console, ConsoleInfo info) {
		return QueueAction.CONTINUE;
	}
	
	default void onShutdown(Console console) {
		return;
	}

}
