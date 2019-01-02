/**
 * 
 */
package com.lbg.ib.api.alligator.util;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Sapient
 *
 */
public class JsonUtils {

	private static JsonParser parser = new JsonParser();

	/*public static String parseJson(String jsonMessage, String key) {
		JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
		JsonElement jsonElement = null;
		if (o.has("error")) {
			JsonElement errorObject = o.get("error");
			jsonElement = errorObject.getAsJsonObject().get("message");
		} else {
			jsonElement = o.get(key);
		}
		if (jsonElement != null) {
			if (!jsonElement.isJsonNull()) {
				String value = jsonElement.toString();
				return value;
			}
		}
		return null;
	}

	public static JsonObject parseJson(String jsonData) throws JSONException {
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(jsonData).getAsJsonObject();
		return o;
	}*/

	public static <T> T resolve(String content, TypeReference<T> typeReference) throws InvalidFormatException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(content, typeReference);
		} catch (IOException e) {
			String message = e.getMessage();
			String[] split = e.getMessage().split("\n");
			if (split.length > 0)
				message = split[0];
			throw new InvalidFormatException("Invalid JSON format: " + message, e);
		}
	}

	public static String convertToJson(Object value) throws InvalidFormatException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(value);
		} catch (IOException e) {
			String message = e.getMessage();
			String[] split = e.getMessage().split("\n");
			if (split.length > 0)
				message = split[0];
			throw new InvalidFormatException("Invalid JSON format: " + message, e);
		}
	}
	
	
	public static String parseJson(String jsonMessage, String key) {
		JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
		JsonElement jsonElement = null;
		if (o.has("error")) {
			JsonElement errorObject = o.get("error");
			jsonElement = errorObject.getAsJsonObject().get("message");
		} else {
			jsonElement = o.get(key);
		}
		if (jsonElement != null) {
			if (!jsonElement.isJsonNull()) {
				String value = jsonElement.toString();
				return value;
			}
		}
		return null;
	}
	
	public static JsonObject parseJson(String jsonData)  {
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(jsonData).getAsJsonObject();
		return o;
	}

}
