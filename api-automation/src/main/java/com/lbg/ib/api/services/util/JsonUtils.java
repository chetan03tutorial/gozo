/**
 * 
 */
package com.lbg.ib.api.services.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.lbg.ib.api.services.beans.DetermineEligibility;


/**
 * @author Sapient
 *
 */


public class JsonUtils {


	static JsonParser parser = new JsonParser();
	
	public static String parseJsonRecursive(String jsonMessage, String key){
         JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
          return parseJsonRecursive(o,key);
    }

	public static boolean checkJsonRecursiveExists(String jsonMessage, String key){
        JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
         return checkJsonRecursiveExists(o,key);
   }
	
	public static JsonArray parseJsonIntoArray(String jsonMessage){
        JsonArray o = parser.parse(jsonMessage).getAsJsonArray();
        return o; 
   }
	
	
	public static String replaceJsonRecursiveExists(String jsonMessage, String key,String replaceValue){
        JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
          replaceJsonRecursiveExists(o,key,replaceValue);
          return o.toString();
   }

	 public static boolean checkJsonRecursiveExists(JsonObject o, String key){

		  Set<Entry<String,JsonElement>> entyrSet = o.entrySet();
		  boolean result = false;

		  for(Entry<String,JsonElement> entry: entyrSet){

		   JsonElement jsonElement = entry.getValue();
					   if(entry.getKey().equals(key)){
						   	   if(jsonElement!=null){
								   return true;
							   }
					   }else{
							   if(jsonElement!=null && jsonElement.isJsonObject()){
								   result = result | checkJsonRecursiveExists(jsonElement.getAsJsonObject(),key);
							   }
					   }
			   }
			   return result;
	   }
	 
	 
	 public static boolean replaceJsonRecursiveExists(JsonObject o, String key,String replaceValue){

		  Set<Entry<String,JsonElement>> entyrSet = o.entrySet();
		  boolean result = false;

		  for(Entry<String,JsonElement> entry: entyrSet){

		   JsonElement jsonElement = entry.getValue();
					   if(entry.getKey().equals(key)){
						   if(replaceValue!=null){
							   entry.setValue(new JsonPrimitive(replaceValue));
						   }
							   if(jsonElement!=null){
								   return true;
							   }
					   }else{
							   if(jsonElement!=null && jsonElement.isJsonObject()){
								   result = result | replaceJsonRecursiveExists(jsonElement.getAsJsonObject(),key,replaceValue);
							   }
					   }
			   }
			   return result;
	   }	 
		  
	 public static String parseJsonRecursive(JsonObject o, String key){
 
		  Set<Entry<String,JsonElement>> entyrSet = o.entrySet();
		  for(Entry<String,JsonElement> entry: entyrSet){

			   JsonElement jsonElement = entry.getValue();
						   if(entry.getKey().equals(key)){
							   if(jsonElement!=JsonNull.INSTANCE){
								   return jsonElement.getAsString();
							   }
						   }else{
								   if(jsonElement!=null && jsonElement.isJsonObject()){
										   String output = parseJsonRecursive(jsonElement.getAsJsonObject(),key);
										   if(output!=null){
												   return output;
										   }
								   }
						   }
				   }
				   return null;
	   }
	
	public static String parseJson(String jsonMessage, String key) {
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
		JsonElement jsonElement = null;
		if (o.has("error")){
			JsonElement errorObject = o.get("error");
			jsonElement = errorObject.getAsJsonObject().get("message");

		}else{
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
	}

	


	public static List<DetermineEligibility> DECIparseJson(String jsonMessage) throws Exception {

		String dEligibilityJson = JsonUtils.parseJson(jsonMessage, "eligibilityDetails");

		TypeToken<List<DetermineEligibility>> token = new TypeToken<List<DetermineEligibility>>() {
		};
		Gson gson = new Gson();
		List<DetermineEligibility> eligibilityList = gson.fromJson(dEligibilityJson, token.getType());

		return eligibilityList;
	}


	public static List<DetermineEligibility> DECIProductSelectorParseJson(String jsonMessage) throws Exception {

		String dEligibilityJson = JsonUtils.parseJson(jsonMessage, "products");

		TypeToken<List<DetermineEligibility>> token = new TypeToken<List<DetermineEligibility>>() {
		};
		Gson gson = new Gson();
		List<DetermineEligibility> eligibilityList = gson.fromJson(dEligibilityJson, token.getType());

		return eligibilityList;
	}
	
	
	public static List<Map<String,Object>>  genericProductSelectorParseJsonResponse(String jsonMessage,String input) throws Exception {

		String dEligibilityJson = JsonUtils.parseJson(jsonMessage, input);

		TypeToken<List<Map<String,Object>> > token = new TypeToken<List<Map<String,Object>>>() {
		};
		Gson gson = new Gson();
		List<Map<String,Object>> eligibilityList = gson.fromJson(dEligibilityJson, token.getType());

		return eligibilityList;
	}	
	
	
	public static List<Object>  genericProductSelectorParseJsonResponseList(String jsonMessage,String input) throws Exception {

		String dEligibilityJson = JsonUtils.parseJson(jsonMessage, input);

		TypeToken<List<Object>> token = new TypeToken<List<Object>>() {
		};
		Gson gson = new Gson();
		List<Object> eligibilityList = gson.fromJson(dEligibilityJson, token.getType());

		return eligibilityList;
	}

	public static String parseJsonArray(String jsonMessage, String key) {

		JsonParser parser = new JsonParser();
		JsonArray jsonArray = parser.parse(jsonMessage).getAsJsonArray();
		JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
		JsonElement element = jsonObject.get(key);
		if (element != null) {
			if (!element.isJsonNull()) {
				String value = element.toString();
				return value;
			}
		}

		return null;

	}

	public static String parseJsonArrayValue(String jsonMessage) {

		JsonParser parser = new JsonParser();
		System.out.println("json message" + jsonMessage);
		JsonArray jsonArray = parser.parse(jsonMessage).getAsJsonArray();
		return jsonArray.get(0).getAsString();

	}


	public static int objectCount(String jsonMessage){
		int count = 0;
		JsonParser parser = new JsonParser();
		JsonArray jsonArray = parser.parse(jsonMessage).getAsJsonArray();
		count = jsonArray.size();
		return count;
	}


	public static void main(String[] args) throws Exception {
		String json = "[{\"key\":\"PostCode\",\"messageIds\":[\"MSG_01\"]}]";
		JsonUtils.parseJsonArrayValue(json);
	}

}
