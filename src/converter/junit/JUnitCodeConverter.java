package converter.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import converter.bpmn.RpaConfig;
import converter.common.CodePlacement;
import rpa.api.parameters.ParameterDirection;
import rpa.api.parameters.RpaParameter;

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
public class JUnitCodeConverter {

	private static Map<String, List<String>> generatedCode = new HashMap<String, List<String>>();

	public static Map<String, List<String>> generateCode(RpaConfig task, List<RpaParameter> botSignature) {

		try {
			if (task != null) {
				System.out.println("**GENERATING CODE FOR " + task.getName());

				generateStartCode(task, botSignature);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedCode;
	}

	private static void generateStartCode(RpaConfig task, List<RpaParameter> botSignature) {

		generateBotName(task.getName());

		if (task.getDocumentation() != null) {
			generateAPIDocumentation(task.getDocumentation());
		}

		if (task.getSystem() != null) {
			generateApiSystem(task.getSystem());
		}

		Iterator<RpaParameter> paramNameIterator = botSignature.iterator();
		while (paramNameIterator.hasNext()) {

			RpaParameter paramName = paramNameIterator.next();

			if (paramName.getDirection() == ParameterDirection.input) {
				generateInputParams(paramName);
			}
		}

		paramNameIterator = botSignature.iterator();
		while (paramNameIterator.hasNext()) {

			RpaParameter paramName = paramNameIterator.next();

			if (paramName.getDirection() == ParameterDirection.output) {
				generateOutputParameters(paramName);
			}
		}
	}

	private static void generateOutputParameters(RpaParameter paramName) {
		String paramsStr = "			outputVar = JsonUtils.getResultVar(result, \"" + paramName.getName() + "\"); \r\n"
				+ "			System.out.println(outputVar);\r\n";
		addCode(CodePlacement.API_OUTPUT_PARAMS.toString(), paramsStr);

	}

	private static void generateInputParams(RpaParameter paramName) {
		String parameterStr = "\\\"" + paramName.getName() + "\\\": \\\"test\\\",";

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
