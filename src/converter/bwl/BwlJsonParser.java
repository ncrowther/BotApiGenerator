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

import converter.bpmn.BpmnTask;
import converter.bpmn.IBpmnParser;
import converter.common.StringUtils;

import org.apache.commons.text.StringEscapeUtils;

public class BwlJsonParser implements IBpmnParser {

	private Map<String, BpmnTask> taskMap = new HashMap<String, BpmnTask>();
	private Map<String, String> sequenceMap = new HashMap<String, String>();
	private Stack<BpmnTask> taskNodes = new Stack<BpmnTask>();

	public BwlJsonParser(String json) {
		parseProcess(json);
	}

	public Map<String, BpmnTask> getTaskMap() {
		return taskMap;
	}

	public Map<String, String> getSequenceMap() {
		return sequenceMap;
	}

	public Stack<BpmnTask> getTaskIds() {
		return taskNodes;
	}

	public BpmnTask getTask(String taskId) {
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

		BpmnTask task = new BpmnTask();
		String id = "1";
		task.setId(id);
		task.setName(name);

		taskMap.put(id, task);
		taskNodes.push(task);

		if (activity.has("properties")) {
			JSONArray propertiesArr = activity.getJSONArray("properties");
			for (int i = 0; i < propertiesArr.length(); i++) {
				JSONObject property = propertiesArr.getJSONObject(i);

				if (property.has("inputs")) {
					getInputParameters(property, task);
				}

				if (property.has("outputs")) {
					getOutputParameters(property, task);
				}
				
				if (property.has("System")) {
					getSystemParameter(property, task);
				}	
				
				if (property.has("Odm")) {
					getOdmParameters(property, task);
				}				
			}
		}
	}

	private void getInputParameters(JSONObject property, BpmnTask task) {
		JSONArray inputsArr = property.getJSONArray("inputs");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			String inputParam = inputObj.getString("name");
			inputParam = StringUtils.removeInvalidCharacters(inputParam);
			// System.out.println("In:" + inputParam);
			task.addInputParam(inputParam);
		}
	}

	private void getOutputParameters(JSONObject property, BpmnTask task) {
		JSONArray inputsArr = property.getJSONArray("outputs");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			String outputParam = inputObj.getString("name");
			outputParam = StringUtils.removeInvalidCharacters(outputParam);
			// System.out.println("Out:" + outputParam);
			task.addOutputParam(outputParam);
		}
	}
	
	private void getSystemParameter(JSONObject property, BpmnTask task) {
		JSONArray inputsArr = property.getJSONArray("System");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			String systemParam = inputObj.getString("name");
			systemParam = StringUtils.removeInvalidCharacters(systemParam);
			System.out.println("SystemParam:" + systemParam);
			task.setSystem(systemParam);
		}
	}	
	
	private void getOdmParameters(JSONObject property, BpmnTask task) {
		JSONArray inputsArr = property.getJSONArray("Odm");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			getOdmPath(task, inputObj);
			getOdmHost(task, inputObj);
			getOdmPayload(task, inputObj);			
		}
	}

	private void getOdmHost(BpmnTask task, JSONObject inputObj) {
		String apiHost = inputObj.getString("host");
		System.out.println("apiHost:" + apiHost);
		task.setOdmHost(apiHost);
	}	
	
	private void getOdmPath(BpmnTask task, JSONObject inputObj) {
		String apiPath = inputObj.getString("path");
		System.out.println("apiPath:" + apiPath);
		task.setOdmPath(apiPath);
	}	
	
	private void getOdmPayload(BpmnTask task, JSONObject inputObj) {
		String apiPayload = inputObj.getString("payload");
		apiPayload = StringEscapeUtils.escapeJson(apiPayload);
		System.out.println("apiPayload:" + apiPayload);
		task.setOdmPayload(apiPayload);
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

		BwlJsonParser jsonParser = new BwlJsonParser(json);
	}

	static String json = "{\r\n" + 
			"  \"name\" : \"ProcessRefund\",\r\n" + 
			"  \"id\" : \"aeed62e59b\",\r\n" + 
			"  \"type\" : \"blueprint\",\r\n" + 
			"  \"properties\" : [ ],\r\n" + 
			"  \"tags\" : [ ],\r\n" + 
			"  \"milestones\" : [ {\r\n" + 
			"    \"name\" : \"Process Refunds\",\r\n" + 
			"    \"id\" : \"aeed62e5a1\",\r\n" + 
			"    \"type\" : \"milestone\",\r\n" + 
			"    \"activities\" : [ {\r\n" + 
			"      \"name\" : \"Start\",\r\n" + 
			"      \"id\" : \"aeed62e5b5\",\r\n" + 
			"      \"type\" : \"start-event\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"aeed62fa57\",\r\n" + 
			"          \"name\" : \"App Connect\"\r\n" + 
			"        } ]\r\n" + 
			"      } ]\r\n" + 
			"    }, {\r\n" + 
			"      \"name\" : \"ReadRefunds\",\r\n" + 
			"      \"id\" : \"afed620037\",\r\n" + 
			"      \"type\" : \"activity\",\r\n" + 
			"      \"sub-type\" : \"normal\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"aeed62fa57\",\r\n" + 
			"          \"name\" : \"App Connect\"\r\n" + 
			"        } ]\r\n" + 
			"      } ]\r\n" + 
			"    }, {\r\n" + 
			"      \"name\" : \"More Refunds\",\r\n" + 
			"      \"id\" : \"aeed62ed04\",\r\n" + 
			"      \"type\" : \"gateway\",\r\n" + 
			"      \"sub-type\" : \"exclusive-gateway\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"aeed62fa57\",\r\n" + 
			"          \"name\" : \"App Connect\"\r\n" + 
			"        } ]\r\n" + 
			"      } ]\r\n" + 
			"    }, {\r\n" + 
			"      \"name\" : \"ProcessRefund\",\r\n" + 
			"      \"id\" : \"aeed62ed17\",\r\n" + 
			"      \"type\" : \"activity\",\r\n" + 
			"      \"sub-type\" : \"normal\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"ceec7bfb81\",\r\n" + 
			"          \"name\" : \"rpa\"\r\n" + 
			"        } ]\r\n" + 
			"      }, {\r\n" + 
			"        \"inputs\" : [ {\r\n" + 
			"          \"id\" : \"aeed62e5f3\",\r\n" + 
			"          \"name\" : \"RefundAmount\"\r\n" + 
			"        }, {\r\n" + 
			"          \"id\" : \"b2ebed40d6\",\r\n" + 
			"          \"name\" : \"CustomerNumber\"\r\n" + 
			"        }, {\r\n" + 
			"          \"id\" : \"35ed655852\",\r\n" + 
			"          \"name\" : \"CardType\"\r\n" + 
			"        }, {\r\n" + 
			"          \"id\" : \"35ed655858\",\r\n" + 
			"          \"name\" : \"CardNumber\"\r\n" + 
			"        }, {\r\n" + 
			"          \"id\" : \"35ed65585c\",\r\n" + 
			"          \"name\" : \"expiryDate\"\r\n" + 
			"        }, {\r\n" + 
			"          \"id\" : \"35ed655860\",\r\n" + 
			"          \"name\" : \"cvc\"\r\n" + 
			"        } ]\r\n" + 
			"      }, {\r\n" + 
			"        \"outputs\" : [ {\r\n" + 
			"          \"id\" : \"ed657947\",\r\n" + 
			"          \"name\" : \"paymentId\"\r\n" + 
			"        }, {\r\n" + 
			"          \"id\" : \"35ed65584b\",\r\n" + 
			"          \"name\" : \"transactionDate\"\r\n" + 
			"        } ]\r\n" + 
			"      }, {\r\n" + 
			"        \"System\" : [ {\r\n" + 
			"          \"id\" : \"7bed640af6\",\r\n" + 
			"          \"name\" : \"localhost:8099\"\r\n" + 
			"        } ]\r\n" + 
			"      } ],\r\n" + 
			"      \"description\" : \"<p>Process customer refund using credit card</p><generatedbybwl></generatedbybwl>\"\r\n" + 
			"    }, {\r\n" + 
			"      \"name\" : \"Subprocess\",\r\n" + 
			"      \"id\" : \"36ed65c702\",\r\n" + 
			"      \"type\" : \"activity\",\r\n" + 
			"      \"sub-type\" : \"subprocess\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"aeed62fa57\",\r\n" + 
			"          \"name\" : \"App Connect\"\r\n" + 
			"        } ]\r\n" + 
			"      } ],\r\n" + 
			"      \"activities\" : [ {\r\n" + 
			"        \"name\" : \"Start\",\r\n" + 
			"        \"id\" : \"36ed65c707\",\r\n" + 
			"        \"type\" : \"start-event\",\r\n" + 
			"        \"properties\" : [ {\r\n" + 
			"          \"participants\" : [ {\r\n" + 
			"            \"id\" : \"aeed62fa57\",\r\n" + 
			"            \"name\" : \"App Connect\"\r\n" + 
			"          } ]\r\n" + 
			"        } ]\r\n" + 
			"      }, {\r\n" + 
			"        \"name\" : \"Email Results\",\r\n" + 
			"        \"id\" : \"aeed62ed24\",\r\n" + 
			"        \"type\" : \"activity\",\r\n" + 
			"        \"sub-type\" : \"robotic-task\",\r\n" + 
			"        \"properties\" : [ {\r\n" + 
			"          \"participants\" : [ {\r\n" + 
			"            \"id\" : \"aeed62fa57\",\r\n" + 
			"            \"name\" : \"App Connect\"\r\n" + 
			"          } ]\r\n" + 
			"        }, {\r\n" + 
			"          \"inputs\" : [ {\r\n" + 
			"            \"id\" : \"4decf8fe03\",\r\n" + 
			"            \"name\" : \"email Address\"\r\n" + 
			"          }, {\r\n" + 
			"            \"id\" : \"36ed655000\",\r\n" + 
			"            \"name\" : \"email Header\"\r\n" + 
			"          }, {\r\n" + 
			"            \"id\" : \"36ed655004\",\r\n" + 
			"            \"name\" : \"email Body\"\r\n" + 
			"          } ]\r\n" + 
			"        }, {\r\n" + 
			"          \"outputs\" : [ {\r\n" + 
			"            \"id\" : \"36ed65501e\",\r\n" + 
			"            \"name\" : \"emailResponse\"\r\n" + 
			"          } ]\r\n" + 
			"        } ],\r\n" + 
			"        \"description\" : \"<p><span style=\\\"background-color:#ffff99\\\">Email Customer refund message</span></p><generatedbybwl></generatedbybwl>\"\r\n" + 
			"      }, {\r\n" + 
			"        \"name\" : \"ODM\",\r\n" + 
			"        \"id\" : \"35ed65992c\",\r\n" + 
			"        \"type\" : \"activity\",\r\n" + 
			"        \"sub-type\" : \"normal\",\r\n" + 
			"        \"properties\" : [ {\r\n" + 
			"          \"participants\" : [ {\r\n" + 
			"            \"id\" : \"aeed62fa57\",\r\n" + 
			"            \"name\" : \"App Connect\"\r\n" + 
			"          } ]\r\n" + 
			"        } ],\r\n" + 
			"        \"linked-decision\" : {\r\n" + 
			"          \"name\" : \"Authorise transfer\",\r\n" + 
			"          \"id\" : \"cecbe9aa9\"\r\n" + 
			"        }\r\n" + 
			"      }, {\r\n" + 
			"        \"name\" : \"End\",\r\n" + 
			"        \"id\" : \"36ed65c709\",\r\n" + 
			"        \"type\" : \"end-event\",\r\n" + 
			"        \"properties\" : [ {\r\n" + 
			"          \"participants\" : [ {\r\n" + 
			"            \"id\" : \"aeed62fa57\",\r\n" + 
			"            \"name\" : \"App Connect\"\r\n" + 
			"          } ]\r\n" + 
			"        } ]\r\n" + 
			"      } ]\r\n" + 
			"    }, {\r\n" + 
			"      \"name\" : \"End\",\r\n" + 
			"      \"id\" : \"aeed62ed09\",\r\n" + 
			"      \"type\" : \"end-event\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"aeed62fa57\",\r\n" + 
			"          \"name\" : \"App Connect\"\r\n" + 
			"        } ]\r\n" + 
			"      } ]\r\n" + 
			"    }, {\r\n" + 
			"      \"name\" : \"End\",\r\n" + 
			"      \"id\" : \"bfed621402\",\r\n" + 
			"      \"type\" : \"end-event\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"ceec7bfb81\",\r\n" + 
			"          \"name\" : \"rpa\"\r\n" + 
			"        } ]\r\n" + 
			"      } ]\r\n" + 
			"    }, {\r\n" + 
			"      \"name\" : \"End\",\r\n" + 
			"      \"id\" : \"bfed620e02\",\r\n" + 
			"      \"type\" : \"end-event\",\r\n" + 
			"      \"properties\" : [ {\r\n" + 
			"        \"participants\" : [ {\r\n" + 
			"          \"id\" : \"ceec7bfb81\",\r\n" + 
			"          \"name\" : \"rpa\"\r\n" + 
			"        } ]\r\n" + 
			"      } ]\r\n" + 
			"    } ]\r\n" + 
			"  } ],\r\n" + 
			"  \"problems\" : [ ],\r\n" + 
			"  \"attachments\" : [ ],\r\n" + 
			"  \"space-ids\" : [ \"12eb456a97\", \"93eb715f68\", \"41ece48a6f\", \"6cecf836b1\", \"56ecf3206a\", \"56ecf36e2f\" ],\r\n" + 
			"  \"space-names\" : [ \"Blueworks Live Training\", \"Nigel Crowther Training Space\", \"WDGBots\", \"WDGProjects\", \"ONS\", \"Finance\" ],\r\n" + 
			"  \"archived-state\" : \"active\",\r\n" + 
			"  \"published-state\" : \"unpublished\",\r\n" + 
			"  \"last-modified-date\" : \"2021-11-16T11:29:16.764+0000\",\r\n" + 
			"  \"last-modified-by-user\" : {\r\n" + 
			"    \"id\" : \"49706c7f\",\r\n" + 
			"    \"full-name\" : \"Nigel Crowther\",\r\n" + 
			"    \"email-address\" : \"ncrowther@uk.ibm.com\"\r\n" + 
			"  },\r\n" + 
			"  \"created-date\" : \"2021-11-03T20:50:00.936+0000\",\r\n" + 
			"  \"created-by-user\" : {\r\n" + 
			"    \"id\" : \"49706c7f\",\r\n" + 
			"    \"full-name\" : \"Nigel Crowther\",\r\n" + 
			"    \"email-address\" : \"ncrowther@uk.ibm.com\"\r\n" + 
			"  },\r\n" + 
			"  \"text-annotations\" : [ ]\r\n" + 
			"}\r\n" + 
			""
			+ "";
}
