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

				generateStartCode( task, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedCode;
	}

	private static void generateStartCode(BpmnTask task, BpmnTask parentTask) {

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

		String logStr = "setVar --name \"${" + paramName + "}\" --value 0 ";
		addCode(CodePlacement.MAIN.toString(), logStr);
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

}
