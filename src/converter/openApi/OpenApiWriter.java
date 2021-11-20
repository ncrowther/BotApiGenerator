package converter.openApi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import converter.bpmn.BpmnTask;
import converter.common.CodePlacement;

public class OpenApiWriter {

	public static void writeOpenApiFile(String filename, BpmnTask bpmnTask)
			throws IOException {

		Map<String, List<String>> generatedCode = ApiCodeConverter.generateCode(bpmnTask);
		
		FileOutputStream outputStream = new FileOutputStream(filename);

		String botName = getCode(generatedCode, CodePlacement.BOTNAME.toString());
		String description = getCode(generatedCode, CodePlacement.API_DOCUMENTATION.toString());
		String apiInputParams = getCode(generatedCode, CodePlacement.API_INPUT_PARAMS.toString());
		String apiOutputParams = getCode(generatedCode, CodePlacement.API_OUTPUT_PARAMS.toString());
		String host = getCode(generatedCode, CodePlacement.API_SYSTEM.toString());
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("swagger: '2.0'\r\n" + 
				"info:\r\n" + 
				"  version: '1.0.5'\r\n" + 
				"  title: " + botName + " API\r\n" + 
				   description +
				"  contact:\r\n" + 
				"    email: ncrowther@uk.ibm.com\r\n" + 
				host +
				"basePath: /\r\n" + 
				"schemes:\r\n" + 
				"- https\r\n" + 
				"consumes:\r\n" + 
				"- application/json\r\n" + 
				"produces:\r\n" + 
				"- application/json\r\n" + 
				"paths:\r\n" + 
				"  /scripts/" + botName +":\r\n" + 
				"    post:\r\n" + 
				"      description: This is an interface to RPA bot " + botName + "\r\n" + 
				"      summary: Invoke " + botName + " bot through RPA API\r\n" + 
				"      tags:\r\n" + 
				"      - RPA\r\n" + 
				"      operationId: " + botName + "\r\n" + 
				"      deprecated: false\r\n" + 
				"      produces:\r\n" + 
				"      - application/json\r\n" + 
				"      parameters:\r\n" + 
				"      - name: unlockMachine\r\n" + 
				"        in: query\r\n" + 
				"        required: true\r\n" + 
				"        type: string\r\n" + 
				"        description: true to unlock false otherwise\r\n" + 
				         apiInputParams  + 
				"      responses:\r\n" + 
				"        '200':\r\n" + 
				"          description: successful operation\r\n" + 
				"          schema:\r\n" + 
				"            $ref: '#/definitions/ApiResponse'\r\n" + 
				"          headers: {}\r\n" + 
				"definitions:\r\n" + 
				"  ApiResponse:\r\n" + 
				"    title: " + botName + "Response\r\n" + 
				"    type: object\r\n" + 
				"    properties:\r\n" + 
			    "      responseCode:\r\n" +
				"        type: string\r\n" +	
				"      responseMessage:\r\n" + 
				"        type: string\r\n" +	          
		               apiOutputParams  + 
				"tags:\r\n" + 
				"- name: " + botName +"\r\n" + 
				"  description: IBM RPA bot interface\r\n" + 
				"externalDocs:\r\n" + 
				"    url: https://rpapi.eu-gb.mybluemix.net\r\n" + 
				"    description: Find out more on the IBM RPA API");        

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
