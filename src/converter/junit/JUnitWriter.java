package converter.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import converter.common.CodePlacement;
import converter.common.StringUtils;
import converter.config.RpaConfig;
import datastructures.BotInfo;
import rpa.api.RpaApi;
import rpa.api.parameters.RpaParameter;

public class JUnitWriter {

	public static void writeJUnitFile(String filename, RpaConfig botConfig, BotInfo botInfo)
			throws IOException {

		Map<String, List<String>> generatedCode = JUnitCodeConverter.generateCode(botConfig, botInfo.getBotSignature());
		
		FileOutputStream outputStream = new FileOutputStream(filename);

		String botName = getCode(generatedCode, CodePlacement.BOTNAME.toString());
		botName = StringUtils.convertToTitleCase(botName);
		String inputParams = getCode(generatedCode, CodePlacement.API_INPUT_PARAMS.toString());
		String outputParams = getCode(generatedCode, CodePlacement.API_OUTPUT_PARAMS.toString());
		String baseUrl = botConfig.getBaseUrl();
		String tenantId = botConfig.getTenantId();
		String username = botConfig.getRpaUser();
		String pwd = botConfig.getRpaPwd();
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
				"	static final String baseURL = \"" + baseUrl + "\";\r\n" + 
				"	static final String tenantId = \"" + tenantId + "\";\r\n" + 
				"	static final String username = \"" + username +"\";\r\n" + 
				"	static final String password = \"" + pwd + "\";\r\n" + 
				"	static final String processName = \"" + botName + "\";\r\n" + 
				"	static final String payload = \"{ \\\"payload\\\": { " + inputParams + " }}\";\r\n" + 
				"	static final String COMPLETED_STATUS = \"done\";\r\n" + 
				"	static final int waitSeconds = 30;\r\n" + 
				"	\r\n" + 
				"	  @Before\r\n" + 
				"	  public void setUp()\r\n" + 
				"	  {\r\n" + 
				"	    // SECURITY: REMOVE THE LINE BELOW IF NOT USING THE SKYTAP LAB TENANT\r\n" + 
				"	    RpaApi.ignoreSSL();\r\n" + 
				"	  }"  +		
				"	\r\n" + 				
				"	@Test\r\n" + 
				"	public void testStartProcess() {\r\n" + 
				"\r\n" + 
				"		try {" + 
				"\r\n" + 
				"			String result = RpaApi.startProcessAndWait(baseURL, tenantId, username, password, processName, payload, waitSeconds);\r\n" +
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
