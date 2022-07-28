package converter.openApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import converter.common.CodePlacement;
import converter.config.RpaConfig;
import datastructures.BotInfo;
import rpa.api.parameters.BotSignature;
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
public class ExternalApiCodeConverter {

	private static Map<String, List<String>> generatedCode = new HashMap<String, List<String>>();

	public static Map<String, List<String>> generateCode(RpaConfig config, BotInfo botInfo) {

		try {
			if (config != null) {
				System.out.println("**GENERATING CODE FOR " + config.getProcessName());

				generateStartCode(config, botInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedCode;
	}

	private static void generateStartCode(RpaConfig config, BotInfo botInfo) {

		generateBotName(config.getProcessName());

		if (config.getDocumentation() != null) {
			// openAPI
			generateAPIDocumentation(config.getDocumentation());
		}

		generateBaseUrl(config);
		
		generateInvokeUrl(botInfo);
		
		generateGetResultUrl(botInfo);

		if (config.getSystem() != null) {
			// openAPI
			generateApiSystem(config.getSystem());
		}
		
		BotSignature botParams = botInfo.getBotSignature();
		
		Iterator<RpaParameter> paramNameIterator = botParams.getParameters().iterator();
		while (paramNameIterator.hasNext()) {

			RpaParameter paramName = paramNameIterator.next();

			if (paramName.getDirection() == ParameterDirection.input) {
				generateInputParams(paramName);
			} else {
				generateOutputParams(paramName);
			}
		}

	}

	private static void generateOutputParams(RpaParameter paramName) {
		String paramStr = "        " + paramName.getName() + ":\r\n" + "          type: " + paramName.getObjectType() + "\r\n" + 
				"          description: TBS\r\n";
		addCode(CodePlacement.API_OUTPUT_PARAMS.toString(), paramStr);

	}

	private static void generateInputParams(RpaParameter paramName) {
		String paramStr = "        " 
	     + paramName.getName() 
		 + ":\r\n" 
	     + "          type: " + paramName.getObjectType() 
		 + "\r\n" 
	     + "          default: " + paramName.getExamplePayload() 	     
	     + "\r\n" 
		 + "          description: Bot input param\r\n";

		addCode(CodePlacement.API_INPUT_PARAMS.toString(), paramStr);
	}
	
	private static void generateBaseUrl(RpaConfig config) {
		String invokeUrl = "- url: " + config.getBaseUrl() + "\r\n";
		addCode(CodePlacement.BASE_URL.toString(), invokeUrl);
	}
	
	private static void generateInvokeUrl(BotInfo botInfo) {
		String invokeUrl = "  /v2.0/workspace/" + botInfo.getWorkspaceId() + "/process/" + botInfo.getProcessId() + "/instance:\r\n";
		addCode(CodePlacement.INVOKE_URL.toString(), invokeUrl);
	}
	
	private static void generateGetResultUrl(BotInfo botInfo) {
		String getResultUrl = 	"  /v2.0/workspace/" + botInfo.getWorkspaceId() + "/process/" + botInfo.getProcessId() + "/instance/{instanceId}:\r\n";
		addCode(CodePlacement.GET_RESULT_URL.toString(), getResultUrl);
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
