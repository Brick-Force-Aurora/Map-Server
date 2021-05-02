package org.playuniverse.brickforce.maprepository.commands;

import org.playuniverse.console.Console;

import com.syntaxphoenix.syntaxapi.command.Arguments;
import com.syntaxphoenix.syntaxapi.command.BaseCommand;
import com.syntaxphoenix.syntaxapi.command.BaseInfo;

public class ShutdownCommand extends BaseCommand {
	
	private final Console console;
	
	public ShutdownCommand(Console console) {
		this.console = console;
	}

	@Override
	public void execute(BaseInfo info, Arguments arguments) {
		console.shutdown();
	}

}
