package converter.wal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import converter.bpmn.BpmnTask;
import converter.common.CodePlacement;

public class WalWriter {

	public static void writeRPAFile(String filename, BpmnTask bpmnTask)
			throws IOException {
		
		Map<String, List<String>> generatedCode = WalCodeConverter.generateCode(bpmnTask);
		
		FileOutputStream outputStream = new FileOutputStream(filename);

		StringBuilder strBuilder = new StringBuilder();
		
		String docs = getCode(generatedCode, CodePlacement.WAL_DOCUMENTATION.toString());		
		strBuilder.append(docs);        
		strBuilder.append("// Input Variables \r\n");
		String inputVars = getCode(generatedCode, CodePlacement.DEF_INPUT_VARS.toString());
		strBuilder.append(inputVars);

		strBuilder.append("// Output Variables \r\n");		
		String outputVars = getCode(generatedCode, CodePlacement.DEF_OUTPUT_VARS.toString());
		strBuilder.append(outputVars);		
		strBuilder.append(  "defVar --name ResponseCode --type String --value 0 --output\r\n" + 
			            	"defVar --name ResponseMessage --type String --output\r\n");
		
		strBuilder.append("// Local Variables \r\n");			
		strBuilder.append(  "defVar --name success --type Boolean --value True\r\n"); 	
		
		strBuilder.append("\r\n\r\n// Add your bot code here \r\n");
		String code = getCode(generatedCode, CodePlacement.MAIN.toString());
		strBuilder.append(code);
		
		strBuilder.append("\r\n" + 
				"// Return response.  ResponseCode is 0 for success, any other number for error code\r\n" + 
				"if --left \"${success}\" --operator \"Is_True\"\r\n" + 
				"	setVar --name \"${ResponseCode}\" --value 0\r\n" + 
				"	setVar --name \"${ResponseMessage}\" --value OK\r\n" + 
				"else\r\n" + 
				"	// ResponseCode greater than 0 indicates failure\r\n" + 
				"	setVar --name \"${ResponseCode}\" --value 123\r\n" + 
				"	setVar --name \"${ResponseMessage}\" --value \"Bot failed\"\r\n" + 
				"endIf");
		
		
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
					strBuilder.append('\n');
				}
			}
		}

		return strBuilder.toString();
	}
}
