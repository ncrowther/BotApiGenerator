package converter.bwl;

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
import java.util.HashMap;

import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import converter.bpmn.RpaConfig;
import converter.bpmn.IBpmnParser;
import converter.common.StringUtils;

import org.apache.commons.text.StringEscapeUtils;

public class BwlJsonParser implements IBpmnParser {

	private Map<String, RpaConfig> taskMap = new HashMap<String, RpaConfig>();
	private Map<String, String> sequenceMap = new HashMap<String, String>();
	private Stack<RpaConfig> taskNodes = new Stack<RpaConfig>();

	public BwlJsonParser(String json) {
		parseProcess(json);
	}

	public Map<String, RpaConfig> getTaskMap() {
		return taskMap;
	}

	public Map<String, String> getSequenceMap() {
		return sequenceMap;
	}

	public Stack<RpaConfig> getTaskIds() {
		return taskNodes;
	}

	public RpaConfig getTask(String taskId) {
		return taskMap.get(taskId);
	}

	private void parseProcess(String json) {

		JSONObject obj = new JSONObject(json);

		JSONArray arr = obj.getJSONArray("milestones");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject jsonObj = arr.getJSONObject(i);
			getMilestones(jsonObj);
		}
	}

	private void getMilestones(JSONObject jsonObj) {
		//String post_id = jsonObj.getString("name");
		// System.out.println(post_id);
		getActivities(jsonObj);
	}

	private void getTaskProperties(JSONObject activity, String name) {

		RpaConfig task = new RpaConfig();
		String id = "1";
		task.setId(id);
		task.setName(name);

		taskMap.put(id, task);
		taskNodes.push(task);

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
			getTenantBaseUrl(task, inputObj);		
			getTenantId(task, inputObj);
			getRpaUser(task, inputObj);
			getRpaPwd(task, inputObj);			
		}
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
	
	private void getActivities(JSONObject milestone) {
		JSONArray activityArr = milestone.getJSONArray("activities");
		for (int j = 0; j < activityArr.length(); j++) {

			JSONObject activity = activityArr.getJSONObject(j);
			String taskName = activity.getString("name");
			taskName = StringUtils.removeInvalidCharacters(taskName);
			//System.out.println(taskName);

			if (activity.has("sub-type")) {
				String subtype = activity.getString("sub-type");
				//System.out.println(subtype);
				if (subtype.equals("robotic-task")) {
					getTaskProperties(activity, taskName);
				}
				if (subtype.equals("subprocess")) {
					getActivities(activity);
				}				
			}			
		}
	}

	public static void main(String[] args) {

	 String json = "{\r\n" + 
	 		"  \"name\": \"RPA GENERATOR CONFIG\",\r\n" + 
	 		"  \"milestones\": [{\r\n" + 
	 		"    \"activities\": [{\r\n" + 
	 		"      \"type\": \"activity\",\r\n" + 
	 		"      \"name\": \"HelloMoto\",\r\n" + 
	 		"      \"sub-type\": \"robotic-task\",\r\n" + 
	 		"      \"properties\": [{\r\n" + 
	 		"        \"RpaTenant\": [{\r\n" + 
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
	 		"    }]\r\n" + 
	 		"  }]\r\n" + 
	 		"}";
	
	BwlJsonParser jsonParser = new BwlJsonParser(json);
  }
	
}
