package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.model.util.CSharpCompat;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public class ShowDialogScript extends ScriptCommand<RCompound> {

	private int speakerId;
	private String dialog;
	
	public ShowDialogScript() {
		super(ScriptType.SHOW_DIALOG, RType.COMPOUND);
	}
	
	public int getSpeakerId() {
		return speakerId;
	}
	
	public String getDialog() {
		return dialog;
	}
	
	@Override
	protected boolean loadDataFromArguments(String[] arguments) {
		if(arguments.length < 1) {
			return false;
		}
		this.speakerId = CSharpCompat.parse(arguments[0], 0, Integer::parseInt);
		this.dialog = arguments.length < 2 ? "" : arguments[1];
		return true;
	}
	
	@Override
	protected String[] dataAsArguments() {
		return new String[] {
				Integer.toString(speakerId),
				dialog
		};
	}
	
	@Override
	protected void loadDataFromModel(RCompound model) {
		this.speakerId = (int) model.get("speaker").getValue();
		this.dialog = (String) model.get("dialog").getValue();
	}
	
	@Override
	protected RCompound dataAsModel() {
		RCompound compound = new RCompound();
		compound.set("speaker", speakerId);
		compound.set("dialog", dialog);
		return compound;
	}
	
}
