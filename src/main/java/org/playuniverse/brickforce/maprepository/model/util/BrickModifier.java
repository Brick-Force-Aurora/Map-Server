package org.playuniverse.brickforce.maprepository.model.util;

public final class BrickModifier {

	private BrickModifier() {}
	
	public static boolean isFunctional(short template) {
		return template == 180 || template == 161 || template == 162 || (template >= 106 && template <= 111);
	}
	
}
