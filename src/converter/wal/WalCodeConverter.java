package converter.wal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import converter.bpmn.BpmnTask;
import converter.common.CodePlacement;

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
public class WalCodeConverter {

	private static Map<String, List<String>> generatedCode = new HashMap<String, List<String>>();

	public static Map<String, List<String>> generateCode(BpmnTask task) {

		try {
			if (task != null) {
				System.out.println("**GENERATING CODE FOR " + task.getName());

				generateInOutParams( task, null);
				
				generateOdmCall( task, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedCode;
	}

	private static void generateInOutParams(BpmnTask task, BpmnTask parentTask) {

		List<String> inputParamNames = task.getInputParams();

		generateBotName(task.getName());


		if (task.getDocumentation() != null) {
			generateWalDocumentation(task.getDocumentation());
		}

		String paramNames = "";
		Iterator<String> paramNameIterator = inputParamNames.iterator();
		while (paramNameIterator.hasNext()) {

			String paramName = paramNameIterator.next();
			
			paramNames += paramName + ": ${" + paramName + "} ";

			generateWalInputDefinitions(paramName);
		}
		
		generateWalMessageBox(task.getName(), paramNames);

		List<String> outputParamName = task.getoutputParams();
		for (String paramName : outputParamName) {
			generateWalOutputDefinitions(paramName);
		}
	}

	private static void generateWalInputDefinitions(String paramName) {
		String paramsStr = "defVar --name " + paramName + " --type String --parameter";
		addCode(CodePlacement.DEF_INPUT_VARS.toString(), paramsStr);
	}
	
	private static void generateWalMessageBox(String botName, String paramNames) {
		String paramsStr = "// messageBox --title \"" + botName + "\" --text \"" + paramNames + "\" --icon \"Information\" --buttons \"OK\" --defaultbutton \"FirstButton\"\n";
		addCode(CodePlacement.MAIN.toString(), paramsStr);
	}	

	private static void generateWalOutputDefinitions(String paramName) {
		String paramsStr = "defVar --name " + paramName + " --type String --output";
		addCode(CodePlacement.DEF_OUTPUT_VARS.toString(), paramsStr);

		String varStr = "setVar --name \"${" + paramName + "}\" --value 0 ";
		addCode(CodePlacement.MAIN.toString(), varStr);

		String responseStr = "setVar --name \"${ResponseMessage}\" --value OK\r\n";
		addCode(CodePlacement.MAIN.toString(), responseStr);
		
	}
	
	private static void generateWalDocumentation(String documentation) {
		String docStr = "// " + documentation;
		addCode(CodePlacement.WAL_DOCUMENTATION.toString(), docStr);
	}
	
	private static void generateBotName(String botname) {
		addCode(CodePlacement.BOTNAME.toString(), botname);
	}

	private static void addCode(String placement, String paramsStr) {
		List<String> functionCode = generatedCode.get(placement);

		if (functionCode == null) {
			functionCode = new ArrayList<String>();
			generatedCode.put(placement, functionCode);
		}
		functionCode.add(paramsStr);
	}
	
	private static void generateOdmCall(BpmnTask task, BpmnTask parentTask) {

		String odmHost = task.getOdmHost();
		String odmPath = task.getOdmPath();
		String odmPayload = task.getOdmPayload();
		

		String paramsStr = "defVar --name ODM_Body --type String\r\n" + 
				"defVar --name ODM_response --type String\r\n" + 
				"defVar --name extractionSuccess --type Boolean\r\n" + 
				"defVar --name extractedTable --type DataTable\r\n" + 
				"defVar --name tableRows --type Numeric\r\n" + 
				"defVar --name tableColumns --type Numeric\r\n";
				
		addCode(CodePlacement.DEF_INTERNAL_VARS.toString(), paramsStr);
		
		String gosubStr = "goSub --label callDecisionService"; 	
		addCode(CodePlacement.MAIN.toString(), gosubStr);
		
		// CALL ODM
		String callOdmStr =
				"\r\nbeginSub --name callDecisionService\r\n" + 
				"	setVar --name \"${ODM_Body}\" --value \"" + odmPayload + "\"\r\n" +
				"	logMessage --message \"### Calling ODM with : ${ODM_Body}\" --type \"Info\"\r\n" + 
				"	httpRequest --verb \"Post\" --url \"http://" + odmHost + "/DecisionService/rest" +  odmPath + "?format=JSON\" --formatter \"Json\" --source \"${ODM_Body}\" success=success ODM_response=value\r\n" + 
				"	logMessage --message \"Response = ${ODM_response}\" --type \"Info\"\r\n" + 
				"	\r\n" + 
				"	jsonToTable --json \"${ODM_response}\" --jsonPath \"$\" tableColumns=columns tableRows=rows extractionSuccess=success extractedTable=value\r\n" + 
				"// You can fetch your response data using the command below\r\n" + 
				"// findColumnByName --dataTable ${extractedTable} --name response columnIndex=value\r\n" + 
				"	\r\n" + 
				"// Show the ODM user defined response data if it exists\r\n" + 
				"	if --left \"${tableColumns}\" --operator \"Greater_Than_Equal_To\" --right 2\r\n" + 
				"		getTableCell --dataTable ${extractedTable} --column 2 --row 1 ResponseMessage=value\r\n" + 
				"		logMessage --message \"ODM return : ${ResponseMessage}\" --type \"Info\"\r\n" + 
				"	endIf\r\n" + 
				"	\r\n" + 
				"endSub\r\n";	
		
		addCode(CodePlacement.CALLODM.toString(), callOdmStr);
	}


}
