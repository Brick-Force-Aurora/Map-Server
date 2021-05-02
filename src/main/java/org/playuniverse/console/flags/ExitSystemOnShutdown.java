package org.playuniverse.console.flags;

import org.playuniverse.console.Console;
import org.playuniverse.console.ConsoleFlag;

class ExitSystemOnShutdown implements ConsoleFlag {
	
	@Override
	public void onShutdown(Console console) {
		System.exit(0);
	}

}
