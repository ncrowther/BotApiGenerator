package converter.openApi;

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
public class ApiCodeConverter {

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
			// openAPI
			generateAPIDocumentation(task.getDocumentation());
		}
		
		
		if (task.getSystem() != null) {		
			// openAPI
			generateApiSystem(task.getSystem());
		}		

		Iterator<String> paramNameIterator = inputParamNames.iterator();
		while (paramNameIterator.hasNext()) {

			String paramName = paramNameIterator.next();
			
			// Open API
			generateOpenApiQuery(paramName);

		}

		List<String> outputParamName = task.getoutputParams();
		for (String paramName : outputParamName) {
			generateOpenApiOutputDefinitions(paramName);
		}
	}

	private static void generateOpenApiOutputDefinitions(String paramName) {
		String paramsStr = "      " + paramName + ":\r\n" + "        type: string\r\n";
		addCode(CodePlacement.API_OUTPUT_PARAMS.toString(), paramsStr);

	}

	private static void generateOpenApiQuery(String paramName) {
		String parameterStr = "      - name: " + paramName + "\r\n" + "        in: query\r\n"
				+ "        required: false\r\n" + "        type: string\r\n" + "        description: TBS\r\n";

		addCode(CodePlacement.API_INPUT_PARAMS.toString(), parameterStr);
	}

	private static void generateAPIDocumentation(String documentation) {
		String docStr = "  description: " + documentation + "\r\n";
		addCode(CodePlacement.API_DOCUMENTATION.toString(), docStr);
	}
		
	private static void generateApiSystem(String system) {
		String docStr = "host: " + system + "\r\n";
		addCode(CodePlacement.API_SYSTEM.toString(), docStr);
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
