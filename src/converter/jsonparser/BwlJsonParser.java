package converter.jsonparser;

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

import converter.IBwlParser;
import converter.bpmn.BpmnTask;
import converter.bpmn.TaskType;
import converter.common.StringUtils;

public class BwlJsonParser implements IBwlParser {

	private Map<String, BpmnTask> taskMap = new HashMap<String, BpmnTask>();
	private Map<String, String> sequenceMap = new HashMap<String, String>();
	private Stack<BpmnTask> startNodes = new Stack<BpmnTask>();

	public BwlJsonParser(String json) {
		parseProcess(json);
	}

	public Map<String, BpmnTask> getTaskMap() {
		return taskMap;
	}

	public Map<String, String> getSequenceMap() {
		return sequenceMap;
	}

	public Stack<BpmnTask> getStartIds() {
		return startNodes;
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
		String post_id = jsonObj.getString("name");
		// System.out.println(post_id);
		getActivities(jsonObj);
	}

	private void getTaskProperties(JSONObject activity, String name) {

		BpmnTask task = new BpmnTask();
		String id = "1";
		task.setId(id);
		task.setType(TaskType.TASK);
		task.setName(name);

		taskMap.put(id, task);
		startNodes.push(task);

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
			}
		}
	}

	private void getInputParameters(JSONObject property, BpmnTask task) {
		JSONArray inputsArr = property.getJSONArray("inputs");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			String inputParam = inputObj.getString("name");
			inputParam = StringUtils.removeInvalidCharacters(inputParam);
			System.out.println("In:" + inputParam);
			task.addInputParam(inputParam);
		}
	}

	private void getOutputParameters(JSONObject property, BpmnTask task) {
		JSONArray inputsArr = property.getJSONArray("outputs");
		for (int i = 0; i < inputsArr.length(); i++) {
			JSONObject inputObj = inputsArr.getJSONObject(i);
			String outputParam = inputObj.getString("name");
			outputParam = StringUtils.removeInvalidCharacters(outputParam);
			System.out.println("Out:" + outputParam);
			task.addOutputParam(outputParam);
		}
	}

	private void getActivities(JSONObject milestone) {
		JSONArray activityArr = milestone.getJSONArray("activities");
		for (int j = 0; j < activityArr.length(); j++) {

			JSONObject activity = activityArr.getJSONObject(j);
			String taskName = activity.getString("name");
			taskName = StringUtils.removeInvalidCharacters(taskName);
			// System.out.println(taskName);

			if (activity.has("sub-type")) {
				String subtype = activity.getString("sub-type");
				// System.out.println(subtype);
				if (subtype.equals("robotic-task")) {
					getTaskProperties(activity, taskName);
				}
			}
		}
	}

	public static void main(String[] args) {

		BwlJsonParser jsonParser = new BwlJsonParser(json);
	}

	static String json = "{\r\n" + "  \"name\" : \"AddSalesLeads\",\r\n" + "  \"id\" : \"7bed641530\",\r\n"
			+ "  \"type\" : \"blueprint\",\r\n" + "  \"properties\" : [ ],\r\n" + "  \"tags\" : [ ],\r\n"
			+ "  \"milestones\" : [ {\r\n" + "    \"name\" : \"Milestone 1\",\r\n" + "    \"id\" : \"7bed641534\",\r\n"
			+ "    \"type\" : \"milestone\",\r\n" + "    \"activities\" : [ {\r\n" + "      \"name\" : \"Start\",\r\n"
			+ "      \"id\" : \"7bed641550\",\r\n" + "      \"type\" : \"start-event\"\r\n" + "    }, {\r\n"
			+ "      \"name\" : \"Prepare Sales Data\",\r\n" + "      \"id\" : \"8fed642502\",\r\n"
			+ "      \"type\" : \"activity\",\r\n" + "      \"sub-type\" : \"normal\"\r\n" + "    }, {\r\n"
			+ "      \"name\" : \"AddSalesLead\",\r\n" + "      \"id\" : \"7bed641538\",\r\n"
			+ "      \"type\" : \"activity\",\r\n" + "      \"sub-type\" : \"robotic-task\",\r\n"
			+ "      \"properties\" : [ {\r\n" + "        \"inputs\" : [ {\r\n"
			+ "          \"id\" : \"7bed642a53\",\r\n" + "          \"name\" : \"first_name\"\r\n" + "        }, {\r\n"
			+ "          \"id\" : \"7bed642a5e\",\r\n" + "          \"name\" : \"last_name\"\r\n" + "        }, {\r\n"
			+ "          \"id\" : \"35ebf9360a\",\r\n" + "          \"name\" : \"Email\"\r\n" + "        }, {\r\n"
			+ "          \"id\" : \"7bed642a6c\",\r\n" + "          \"name\" : \"job_title\"\r\n" + "        }, {\r\n"
			+ "          \"id\" : \"7bed642a75\",\r\n" + "          \"name\" : \"company\"\r\n" + "        }, {\r\n"
			+ "          \"id\" : \"a7ed6442ba\",\r\n" + "          \"name\" : \"phone\"\r\n" + "        }, {\r\n"
			+ "          \"id\" : \"a7ed6442bd\",\r\n" + "          \"name\" : \"client_address\"\r\n"
			+ "        }, {\r\n" + "          \"id\" : \"a7ed6442c0\",\r\n" + "          \"name\" : \"client_city\"\r\n"
			+ "        }, {\r\n" + "          \"id\" : \"a7ed6442c5\",\r\n"
			+ "          \"name\" : \"client_state\"\r\n" + "        }, {\r\n"
			+ "          \"id\" : \"a7ed6442ca\",\r\n" + "          \"name\" : \"client_zipcode\"\r\n"
			+ "        }, {\r\n" + "          \"id\" : \"a7ed6442d1\",\r\n" + "          \"name\" : \"interest\"\r\n"
			+ "        }, {\r\n" + "          \"id\" : \"4decf8fe0f\",\r\n" + "          \"name\" : \"followup\"\r\n"
			+ "        } ]\r\n" + "      }, {\r\n" + "        \"outputs\" : [ {\r\n"
			+ "          \"id\" : \"7ded641c5f\",\r\n" + "          \"name\" : \"salesId\"\r\n" + "        } ]\r\n"
			+ "      }, {\r\n" + "        \"System\" : [ {\r\n" + "          \"id\" : \"7bed640af6\",\r\n"
			+ "          \"name\" : \"localhost:8099\"\r\n" + "        } ]\r\n" + "      } ],\r\n"
			+ "      \"description\" : \"<p>This bot adds new <span style=\\\"color:#ff0000\\\"><i>leads </i></span>to the leads website called JKAutomation</p><generatedbybwl></generatedbybwl>\"\r\n"
			+ "    }, {\r\n" + "      \"name\" : \"Log Activity In SalesForce\",\r\n"
			+ "      \"id\" : \"8fed64250a\",\r\n" + "      \"type\" : \"activity\",\r\n"
			+ "      \"sub-type\" : \"normal\"\r\n" + "    } ]\r\n" + "  }, {\r\n"
			+ "    \"name\" : \"Milestone 2\",\r\n" + "    \"id\" : \"7bed641536\",\r\n"
			+ "    \"type\" : \"milestone\",\r\n" + "    \"activities\" : [ {\r\n" + "      \"name\" : \"End\",\r\n"
			+ "      \"id\" : \"7bed641552\",\r\n" + "      \"type\" : \"end-event\"\r\n" + "    } ]\r\n" + "  } ],\r\n"
			+ "  \"problems\" : [ ],\r\n" + "  \"attachments\" : [ ],\r\n"
			+ "  \"space-ids\" : [ \"12eb456a97\", \"93eb715f68\", \"41ece48a6f\", \"6cecf836b1\", \"56ecf3206a\", \"7bed641529\" ],\r\n"
			+ "  \"space-names\" : [ \"Blueworks Live Training\", \"Nigel Crowther Training Space\", \"WDGBots\", \"WDGProjects\", \"ONS\", \"Sales\" ],\r\n"
			+ "  \"archived-state\" : \"active\",\r\n" + "  \"published-state\" : \"published\",\r\n"
			+ "  \"last-modified-date\" : \"2021-11-12T14:14:44.855+0000\",\r\n" + "  \"last-modified-by-user\" : {\r\n"
			+ "    \"id\" : \"49706c7f\",\r\n" + "    \"full-name\" : \"Nigel Crowther\",\r\n"
			+ "    \"email-address\" : \"ncrowther@uk.ibm.com\"\r\n" + "  },\r\n"
			+ "  \"created-date\" : \"2021-11-10T19:43:53.529+0000\",\r\n" + "  \"created-by-user\" : {\r\n"
			+ "    \"id\" : \"49706c7f\",\r\n" + "    \"full-name\" : \"Nigel Crowther\",\r\n"
			+ "    \"email-address\" : \"ncrowther@uk.ibm.com\"\r\n" + "  },\r\n"
			+ "  \"published-date\" : \"2021-11-12T14:01:03.747+0000\",\r\n"
			+ "  \"published-snapshot-name\" : \"current-version\",\r\n" + "  \"text-annotations\" : [ ]\r\n" + "}\r\n"
			+ "";
}
