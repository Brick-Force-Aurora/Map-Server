package org.playuniverse.brickforce.maprepository.model.util;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class CSharpCompat {

	public static final String[] EMPTY_ARRAY = new String[0];

	public static String[] split(String input, String... delimiters) {
		if (delimiters.length == 0) {
			return EMPTY_ARRAY;
		}
		ArrayList<String> list = new ArrayList<>();
		list.add(input);
		for (String delimiter : delimiters) {
			String regexSafe = Pattern.quote(delimiter);
			for (int index = 0; index < list.size(); index++) {
				String current = list.get(index);
				if (!current.contains(delimiter)) {
					continue;
				}
				list.remove(index);
				String[] items = current.split(regexSafe);
				for (int index0 = items.length - 1; index0 >= 0; index0--) {
					list.add(index, items[index0]);
				}
			}
		}
		return list.toArray(String[]::new);
	}

	public static String[] splitRemoveEmpty(String input, String... delimiters) {
		if (delimiters.length == 0) {
			return EMPTY_ARRAY;
		}
		ArrayList<String> list = new ArrayList<>();
		list.add(input);
		for (String delimiter : delimiters) {
			String regexSafe = Pattern.quote(delimiter);
			for (int index = 0; index < list.size(); index++) {
				String current = list.get(index);
				if (!current.contains(delimiter)) {
					continue;
				}
				list.remove(index);
				String[] items = current.split(regexSafe);
				for (int index0 = items.length - 1; index0 >= 0; index0--) {
					String item = items[index0];
					if (item.isEmpty()) {
						continue;
					}
					list.add(index, item);
				}
			}
			if(list.isEmpty()) {
				return EMPTY_ARRAY;
			}
		}
		return list.toArray(String[]::new);
	}

	public static <E> E parse(String input, E fallback, Function<String, E> parser) {
		try {
			return parser.apply(input);
		} catch (NumberFormatException exp) {
			return fallback;
		}
	}

}
