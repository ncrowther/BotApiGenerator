package converter.bwl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import converter.common.BpmnTask;
import converter.common.CodePlacement;

public class CodeConverter {

	private static Map<String, List<String>> generatedCode = new HashMap<String, List<String>>();

	public static void reset() {
		generatedCode = new HashMap<String, List<String>>();
	}

	public static Map<String, List<String>> generateWDGFunctionCode(String startId, IBwlParser bpmnParser) {

		try {
			generateCode(bpmnParser, startId, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedCode;
	}

	private static void generateCode(IBwlParser bpmnParser, String taskId, BpmnTask parentTask) {

		BpmnTask task = bpmnParser.getTask("1");
		if (task != null) {

			System.out.println("**GENERATING CODE FOR " + task.getName());

			generateStartCode(bpmnParser, task, null);
		}
	}

	private static void generateStartCode(IBwlParser bpmnParser, BpmnTask task, BpmnTask parentTask) {

		List<String> inputParamNames = task.getInputParams();
		
		generateBotName(task.getName());
		
		// Wal
		generateWalDocumentation(task.getDocumentation());
		
		//openAPI
		generateAPIDocumentation(task.getDocumentation());
		
		
		Iterator<String> paramNameIterator = inputParamNames.iterator();
		while (paramNameIterator.hasNext()) {
			
			String paramName = paramNameIterator.next();
			
			// WAL
			generateWalInputDefinitions(paramName);
			
			// POSTMAN
			generatePostmanInputParams(paramNameIterator, paramName);		
			
			// Open API
			generateOpenApiQuery(paramName);
	
			
		}
		
		List<String> outputParamName = task.getoutputParams();
		for (String paramName : outputParamName) {
			generateWalOutputDefinitions(paramName);
			generateOpenApiOutputDefinitions(paramName);			
		}		
	}

	private static void generateOpenApiOutputDefinitions(String paramName) {
		String paramsStr = "      " + paramName + ":\r\n" + 
				           "        type: string\r\n";
		addCode(CodePlacement.API_OUTPUT_PARAMS.toString(), paramsStr);
		
	}
	
	private static void generateOpenApiQuery(String paramName) {
		String parameterStr = "      - name: " + paramName + "\r\n" + 
				"        in: query\r\n" + 
				"        required: false\r\n" + 
				"        type: string\r\n" + 
				"        description: TBS\r\n";
				
		addCode(CodePlacement.API_INPUT_PARAMS.toString(), parameterStr);
	}

	private static void generatePostmanInputParams(Iterator<String> paramNameIterator, String paramName) {
		String urlParamStr = "&" + paramName + "=TEST";
		addCode(CodePlacement.URLPARAMS.toString(), urlParamStr);
		
		String queryParamStr = "						{\r\n" + 
				"							\"key\": \"" + paramName + "\",\r\n" + 
				"							\"value\": \"TEST\"\r\n" + 
				"						}";
		
		addCode(CodePlacement.QUERYPARAMS.toString(), queryParamStr);				
		
		if (paramNameIterator.hasNext()) {
			addCode(CodePlacement.QUERYPARAMS.toString(), ",\n");	
		}
	}

	private static void generateWalInputDefinitions(String paramName) {
		String paramsStr = "defVar --name " + paramName + " --type String --parameter";
		addCode(CodePlacement.DEF_INPUT_VARS.toString(), paramsStr);

		String logStr = "logMessage --message \"" + paramName + ": ${" + paramName + "}\" --type \"Info\"";			
		addCode(CodePlacement.MAIN.toString(), logStr);
	}
	
	private static void generateWalOutputDefinitions(String paramName) {
		String paramsStr = "defVar --name " + paramName + " --type String --output";
		addCode(CodePlacement.DEF_OUTPUT_VARS.toString(), paramsStr);
		
		String logStr = "setVar --name \"${" + paramName + "}\" --value 0 ";			
		addCode(CodePlacement.MAIN.toString(), logStr);
	}

	private static void generateAPIDocumentation(String documentation) {
		String docStr = "  description: " + documentation + "\r\n";
		addCode(CodePlacement.API_DOCUMENTATION.toString(), docStr);
	}	

	private static void generateWalDocumentation(String documentation) {
		String docStr = "// " + documentation ;
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
