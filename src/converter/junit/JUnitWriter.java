package converter.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import converter.bpmn.RpaConfig;
import converter.common.CodePlacement;
import converter.common.StringUtils;
import rpa.api.parameters.RpaParameter;

public class JUnitWriter {

	public static void writeJUnitFile(String filename, RpaConfig bpmnTask, List<RpaParameter> botSignature)
			throws IOException {

		Map<String, List<String>> generatedCode = JUnitCodeConverter.generateCode(bpmnTask, botSignature);
		
		FileOutputStream outputStream = new FileOutputStream(filename);

		String botName = getCode(generatedCode, CodePlacement.BOTNAME.toString());
		botName = StringUtils.convertToTitleCase(botName);
		String description = getCode(generatedCode, CodePlacement.API_DOCUMENTATION.toString());
		String inputParams = getCode(generatedCode, CodePlacement.API_INPUT_PARAMS.toString());
		String outputParams = getCode(generatedCode, CodePlacement.API_OUTPUT_PARAMS.toString());
		String host = getCode(generatedCode, CodePlacement.API_SYSTEM.toString());
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("package junit;\r\n" + 
				"\r\n" + 
				"import static org.junit.Assert.assertTrue;\r\n" + 
				"import static org.junit.Assert.fail;\r\n" + 
				"import static org.junit.jupiter.api.Assertions.assertNotNull;\r\n" + 
				"\r\n" + 
				"import org.junit.Test;\r\n" + 
				"\r\n" + 
				"import rpa.api.RpaApi;\r\n" + 
				"import rpa.json.JsonUtils;\r\n" + 
				"\r\n" + 
				"public class " + botName + "Test {\r\n" + 
				"\r\n" + 
				"	static final String baseURL = \"https://uk1api.wdgautomation.com\";\r\n" + 
				"	static final String tenantId = \"e780ec1f-e62f-4148-8335-2f3ac251373e\";\r\n" + 
				"	static final String username = \"ncrowther@uk.ibm.com\";\r\n" + 
				"	static final String password = \"Porker01!\";\r\n" + 
				"	static final String processName = \"" + botName + "\";\r\n" + 
				"	static final String payload = \"{ \\\"payload\\\": { " + inputParams + " }}\";\r\n" + 
				"	static final String COMPLETED_STATUS = \"done\";\r\n" + 
				"	static final int waitSeconds = 30;\r\n" + 
				"	\r\n" + 
				"	@Test\r\n" + 
				"	public void testStartProcess() {\r\n" + 
				"\r\n" + 
				"		try {		\r\n" + 
				"			String token = RpaApi.getBearerToken(baseURL, tenantId, username, password);\r\n" + 
				"			assertNotNull( token, \"Token generated\");\r\n" + 
				"\r\n" + 
				"			String result = RpaApi.startProcessAndWait(baseURL, tenantId, token, processName, payload, waitSeconds);\r\n" + 
				"\r\n" + 
				"			assertNotNull(result, \"Result not null\");\r\n" +
				"\r\n" + 				
				"			String status = JsonUtils.getStatus(result);\r\n" + 
				"\r\n" + 
				"			assertTrue(\"Expected: \" + COMPLETED_STATUS + \", Actual: \" + status, status.equals(COMPLETED_STATUS));\r\n" + 
				"\r\n" +
				"			Object outputVar = null;\r\n" + 				
				outputParams + 
				"		\r\n" + 
				"		} catch (Exception e) {\r\n" + 
				"			e.printStackTrace();\r\n" + 
				"			fail();\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"}");        

		strBuilder.append("\n");		
		
		byte[] strToBytes = strBuilder.toString().getBytes();
		outputStream.write(strToBytes);


		outputStream.close();
	}

	private static String getCode(Map<String, List<String>> generatedCode, String key) {

		List<String> functionCode = generatedCode.get(key);

		StringBuilder strBuilder = new StringBuilder();

		if (functionCode != null) {
			for (String codeLine : functionCode) {
				if (codeLine != null) {
					strBuilder.append(codeLine);
				}
			}
		}

		return strBuilder.toString();
	}
}
