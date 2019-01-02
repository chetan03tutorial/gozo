package com.lbg.ib.api.saveandresumecancel;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.lbg.ib.api.test.services.GenericFeaturesImpl;
import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.lbg.ib.api.BaseEnvSetUp;
import com.lbg.ib.api.services.util.JsonUtils;


import cucumber.api.java.en.Then;

public class SaveandResumeCancelStep extends BaseEnvSetUp 
{

	
	public static final String ENV = "env";
	private GenericFeaturesImpl acquServiceImpl;
	public Map<String, String> actualResult;
	private Properties envProperty;
	private static JsonParser parser=new JsonParser();
	private String appId;
	public static String ErrMsg;

	
	public SaveandResumeCancelStep () throws Exception 
	
	{
		super();
		acquServiceImpl = new GenericFeaturesImpl();
		try {
			envProperty = this.getEnvProperty();

		} catch (Exception e) {
			throw new Exception(e);
		}

	}
	
	
	
	public String readRequest(String fileName) {
		return readRequestData("jsonRequestFiles/snr", fileName);
	}
	
	public String readRequestData(String filePath , String fileName)
	{
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		InputStream in = contextClassLoader.getResourceAsStream(filePath+"/"+fileName+".json");
		String theString = null;
		try {
			 theString = IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theString;
	}
	
	private String parseJson(String jsonMessage, String key) {

		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
		JsonElement jsonElement = o.get(key);
		if (jsonElement != null) {
			if (!jsonElement.isJsonNull()) {
				String value = jsonElement.toString();
				System.out.println("value:" + value);
				return value;
			}
		}

		return null;

	}

	@Then("^I perform the cancel operation for save and resume enpoint and Request \"([^\"]*)\" \"([^\"]*)\"$")
	public void acquCaseCustomerdetails(String endpoint, String requestData) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		actualResult = acquServiceImpl.getResponseFromAPI(readRequest(requestData), url);
		assertTrue(actualResult != null);
	}	

	
	@Then("^I should see httpstatuscode as \"([^\"]*)\"$")
	public void validatehttpscode(String responsecode) {
		String status = actualResult.get("StatusCode");
		assertTrue(responsecode.equals(status));
	}
	
	
	
	public static String replaceJsonRecursiveExists(String jsonMessage, String key,String replaceValue){
        JsonObject o = parser.parse(jsonMessage).getAsJsonObject();
  replaceJsonRecursiveExists(o,key,replaceValue);
  return o.toString();
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
	
	
	
	
	@Then("^I perform the snr cancel api validation using enpoint and Request \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
	public void assignCaseOperationValidation(String endpoint, String requestData, String requiredarrangementId) throws Throwable {
		String url = (String) this.getEnvProperty().get(endpoint);
		String request = readRequest(requestData);
		
		//In Runtime, insert the appid (We got appid from the Getcase response) to the request json of Assigncase api
		String newrequest=replaceJsonRecursiveExists(request, "arrangementId",requiredarrangementId);
		actualResult = acquServiceImpl.getResponseFromAPI(newrequest, url);
		String status = actualResult.get("StatusCode");
		
		String messageBody = actualResult.get("StatusMessage");
		String error = parseJson(messageBody, "error");
		String messageActual = parseJson(error, "message");	
		ErrMsg=messageActual;
		
	}

	
	@Then("^I should validate error message for CrossBrand \"([^\"]*)\"$")
	public void i_should_see_message(String message) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		//String messageBody = actualResult.get("StatusMessage");
		//String error = parseJson(messageBody, "error");
		String messageActual = ErrMsg;
		if (null != messageActual) {
			messageActual = messageActual.replaceAll("\"", "");
			assertTrue(messageActual.equalsIgnoreCase(message));
		} else {
			assertTrue(String.valueOf(messageActual).equalsIgnoreCase(message));
		}
	}
	
	
	@Then("^I should see assignedArrangementId as \"([^\"]*)\"$")
	public void validateColleagueId(String colleagueId) {
		String messageBody = actualResult.get("StatusMessage");
		String fetchedColleagueId = JsonUtils.parseJsonRecursive(messageBody, "arrangementId").replaceAll("\"", "");
		assertTrue(colleagueId.equals(fetchedColleagueId));
	}
	
	
}
