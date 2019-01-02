package com.lbg.ib.api.alligator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

public class JsonManager {

	/*private final Map<String, Object> json;

	public JsonManager(Map<String, Object> json) {
		this.json = json;
	}

	public Map<String, Object> map() {
		return json;
	}*/

	public static String update(String json, String path, Object newValue) {
		String response = null;
		try {
			Map<String, Object> jsonMap = JsonUtils.resolve(json,new TypeReference<HashMap<String, Object>>() {});
			updateJson(jsonMap, Path.parse(path), newValue);
			response = JsonUtils.convertToJson(jsonMap);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static void updateJson(Map<String, Object> data, Iterator<Token> path, Object newValue) {
		Token token = path.next();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if (!token.accept(entry.getKey(), entry.getValue())) {
				continue;
			}
			if (path.hasNext()) {
				Object value = token.value(entry.getValue());
				if (value instanceof Map) {
					updateJson((Map<String, Object>) value, path, newValue);
				}
			} else {
				token.update(entry, newValue);
			}
		}
	}
}

class Path {
	public static Iterator<Token> parse(String path) {
		if (path.isEmpty()) {
			return Collections.<Token> emptyList().iterator();
		}
		if (path.startsWith("$.")) {
			path = path.substring(2);
		}

		List<Token> tokens = new ArrayList<Token>();
		for (String part : path.split("\\.")) {
			if (part.matches("\\w+\\[\\d+\\]")) {
				String fieldName = part.substring(0, part.indexOf('['));
				int index = Integer.parseInt(part.substring(part.indexOf('[') + 1, part.indexOf(']')));
				tokens.add(new ArrayToken(fieldName, index));
			} else {
				tokens.add(new FieldToken(part));
			}
		}
		;

		return tokens.iterator();
	}
}

abstract class Token {

	protected final String fieldName;

	Token(String fieldName) {
		this.fieldName = fieldName;
	}

	public abstract Object value(Object value);

	public abstract boolean accept(String key, Object value);

	public abstract void update(Map.Entry<String, Object> entry, Object newValue);
}

class FieldToken extends Token {

	FieldToken(String fieldName) {
		super(fieldName);
	}

	@Override
	public Object value(Object value) {
		return value;
	}

	@Override
	public boolean accept(String key, Object value) {
		return fieldName.equals(key);
	}

	@Override
	public void update(Map.Entry<String, Object> entry, Object newValue) {
		entry.setValue(newValue);
	}
}

class ArrayToken extends Token {

	private final int index;

	ArrayToken(String fieldName, int index) {
		super(fieldName);
		this.index = index;
	}

	@Override
	public Object value(Object value) {
		return ((List) value).get(index);
	}

	@Override
	public boolean accept(String key, Object value) {
		
		
		return fieldName.equals(key) && value instanceof List && ((List) value).size() > index;
	}

	@Override
	public void update(Map.Entry<String, Object> entry, Object newValue) {
		List list = (List) entry.getValue();
		list.set(index, newValue);
	}
}