package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public class SetMissionScript extends ScriptCommand<RCompound> {

	private String progress = "";
	private String title = "";
	private String subTitle = "";
	private String tag = "";

	public SetMissionScript() {
		super(ScriptType.SET_MISSION, RType.COMPOUND);
	}

	public String getProgress() {
		return progress;
	}

	public boolean hasProgress() {
		return !progress.isEmpty();
	}

	public String getTitle() {
		return title;
	}

	public boolean hasTitle() {
		return !title.isEmpty();
	}

	public String getSubTitle() {
		return subTitle;
	}

	public boolean hasSubTitle() {
		return !subTitle.isEmpty();
	}

	public String getTag() {
		return tag;
	}

	public boolean hasTag() {
		return !tag.isEmpty();
	}

	@Override
	protected void loadDataFromModel(RCompound model) {
		if (model.has("progress", RType.STRING)) {
			this.progress = (String) model.get("progress").getValue();
		}
		if (model.has("title", RType.STRING)) {
			this.title = (String) model.get("title").getValue();
		}
		if (model.has("subTitle", RType.STRING)) {
			this.subTitle = (String) model.get("subTitle").getValue();
		}
		if (model.has("tag", RType.STRING)) {
			this.tag = (String) model.get("tag").getValue();
		}
	}

	@Override
	protected RCompound dataAsModel() {
		RCompound compound = new RCompound();
		if (hasProgress()) {
			compound.set("progress", progress);
		}
		if (hasTitle()) {
			compound.set("title", title);
		}
		if (hasSubTitle()) {
			compound.set("subTitle", subTitle);
		}
		if (hasTag()) {
			compound.set("tag", tag);
		}
		return null;
	}

}
