package org.playuniverse.console;

public final class ConsoleInfo {
	
	private final int lastInputMade;
	private final String inputLine;
	
	/*
	 * 
	 */
	
	ConsoleInfo(int lastInputMade) {
		this(lastInputMade, "");
	}
	
	ConsoleInfo(String inputLine) {
		this(0, inputLine);
	}
	
	ConsoleInfo(int lastInputMade, String inputLine) {
		this.lastInputMade = lastInputMade;
		this.inputLine = inputLine;
	}
	
	/*
	 * 
	 */
	
	public int lastInputInSeconds() {
		return lastInputMade;
	}
	
	public String currentInput() {
		return inputLine;
	}
	
}
