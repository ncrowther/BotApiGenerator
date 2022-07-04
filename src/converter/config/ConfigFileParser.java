package converter.config;

/* 
 * Licensed Materials - Property of IBM Corporation.
 * 
 * 5725-A20
 * 
 * Copyright IBM Corporation 2021. All Rights Reserved.
 * 
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corporation.
 */
import org.json.JSONArray;
import org.json.JSONObject;

import converter.common.StringUtils;

import org.apache.commons.text.StringEscapeUtils;

public class ConfigFileParser implements IConfigParser {
	
	private RpaConfig task = new RpaConfig();

	public ConfigFileParser(String json) {
		parseProcess(json);
	}

	public  RpaConfig getConfig() {
		return task;
	}

	private void parseProcess(String json) {

		JSONObject jsonObj = new JSONObject(json);

		getTaskProperties(jsonObj);
	}

	private void getTaskProperties(JSONObject activity) {

		if (activity.has("properties")) {
			JSONArray propertiesArr = activity.getJSONArray("properties");
			for (int i = 0; i < propertiesArr.length(); i++) {
				JSONObject property = propertiesArr.getJSONObject(i);
				
				if (property.has("RpaTenant")) {
					getRpaTenantParameters(property, task);
				}
				
				if (property.has("Odm")) {
					getOdmParameters(property, task);
				}				
			}
		}
	}

	private void getRpaTenantParameters(JSONObject property, RpaConfig task) {
		JSONArray inputsArr = property.getJSONArray("RpaTenant");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			getProcessName(task, inputObj);
			getTenantBaseUrl(task, inputObj);		
			getTenantId(task, inputObj);
			getRpaUser(task, inputObj);
			getRpaPwd(task, inputObj);			
		}
	}

	private void getProcessName(RpaConfig task, JSONObject inputObj) {
		String processName = inputObj.getString("processName");	
		System.out.println("getProcessName:" + processName);
		task.setName(processName);
	}
	
	private void getTenantBaseUrl(RpaConfig task, JSONObject inputObj) {
		String baseUrl = inputObj.getString("baseUrl");
		System.out.println("baseUrl:" + baseUrl);
		task.setBaseUrl(baseUrl);
	}	
	
	private void getTenantId(RpaConfig task, JSONObject inputObj) {
		String tenantId = inputObj.getString("tenantId");
		System.out.println("tenantId:" + tenantId);
		task.setTenantId(tenantId);
	}	
	
	private void getRpaUser(RpaConfig task, JSONObject inputObj) {
		String rpaUser = inputObj.getString("rpaUser");
		System.out.println("rpaUser:" + rpaUser);
		task.setRpaUser(rpaUser);
	}	
	
	private void getRpaPwd(RpaConfig task, JSONObject inputObj) {
		String rpaPwd = inputObj.getString("rpaPwd");
		System.out.println("rpaPwd:" + rpaPwd);
		task.setRpaPwd(rpaPwd);
	}		
	
	private void getOdmParameters(JSONObject property, RpaConfig task) {
		JSONArray inputsArr = property.getJSONArray("Odm");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			getOdmPath(task, inputObj);
			getOdmHost(task, inputObj);
			getOdmPayload(task, inputObj);		
			getResUser(task, inputObj);			
			getResPwd(task, inputObj);					
		}
	}

	private void getOdmHost(RpaConfig task, JSONObject inputObj) {
		String apiHost = inputObj.getString("host");
		System.out.println("apiHost:" + apiHost);
		task.setOdmHost(apiHost);
	}	
	
	private void getOdmPath(RpaConfig task, JSONObject inputObj) {
		String apiPath = inputObj.getString("path");
		System.out.println("apiPath:" + apiPath);
		task.setOdmPath(apiPath);
	}	
	
	private void getOdmPayload(RpaConfig task, JSONObject inputObj) {
		String apiPayload = inputObj.getString("payload");
		apiPayload = StringEscapeUtils.escapeJson(apiPayload);
		System.out.println("apiPayload:" + apiPayload);
		task.setOdmPayload(apiPayload);
	}	
	
	private void getResUser(RpaConfig task, JSONObject inputObj) {
		String resUser = inputObj.getString("resUser");
		System.out.println("resUser:" + resUser);
		task.setResUser(resUser);
	}		

	private void getResPwd(RpaConfig task, JSONObject inputObj) {
		String resPwd = inputObj.getString("resPwd");
		System.out.println("resPwd:" + resPwd);
		task.setResPwd(resPwd);
	}			
	

	public static void main(String[] args) {

	 String json = "{\r\n" + 
	 		"  \"name\": \"RPA GENERATOR CONFIG\",\r\n" + 
	 		"      \"properties\": [{\r\n" + 
	 		"        \"RpaTenant\": [{\r\n" + 
	 		"          \"processName\": \"CustomerRefunds\",\r\n" + 
	 		"          \"baseUrl\": \"https://uk1api.wdgautomation.com\",\r\n" + 
	 		"          \"tenantId\": \"e780ec1f-e62f-4148-8335-2f3ac251373e\",\r\n" + 
	 		"          \"rpaUser\": \"ncrowther@uk.ibm.com\",\r\n" + 
	 		"          \"rpaPwd\": \"Porker01!\"\r\n" + 
	 		"        }],\r\n" + 
	 		"         \"Odm\": [{\r\n" + 
	 		"            \"host\": \"\",\r\n" + 
	 		"            \"path\": \"\",\r\n" + 
	 		"            \"resUser\": \"\",\r\n" + 
	 		"            \"resPwd\": \"\",\r\n" + 
	 		"            \"payload\": \"\"\r\n" + 
	 		"          }]\r\n" + 
	 		"      }]\r\n" + 
	 		"}";
	
	ConfigFileParser jsonParser = new ConfigFileParser(json);
	
	RpaConfig c = jsonParser.getConfig();
	
	System.out.println(c);
  }
	
}
