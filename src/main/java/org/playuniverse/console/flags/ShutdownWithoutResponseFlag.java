package org.playuniverse.console.flags;

import org.playuniverse.console.Console;
import org.playuniverse.console.ConsoleFlag;
import org.playuniverse.console.ConsoleInfo;
import org.playuniverse.console.WatchAction;

import com.syntaxphoenix.syntaxapi.logging.LogTypeId;

final class ShutdownWithoutResponseFlag implements ConsoleFlag {
	
	private final int seconds;
	private boolean timeout = false;

	public ShutdownWithoutResponseFlag(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public WatchAction onWatchDog(Console console, ConsoleInfo info) {
		if(info.lastInputInSeconds() >= seconds) {
			timeout = true;
			return WatchAction.SHUTDOWN;
		}
		return WatchAction.CONTINUE;
	}
	
	@Override
	public void onShutdown(Console console) {
		if(!timeout)
			return;
		console.log(LogTypeId.WARNING, "No input for too long!");
		console.log(LogTypeId.WARNING, "Shutting down...");
	}
	
}
