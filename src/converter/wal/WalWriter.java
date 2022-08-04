package converter.wal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import converter.common.CodePlacement;
import converter.config.RpaConfig;
import converter.openApi.ExternalApiCodeConverter;
import datastructures.BotInfo;
import rpa.api.parameters.BotSignature;

public class WalWriter {

	public static void writeRPAFile(String filename, RpaConfig rpaConfig, BotInfo botInfo)
			throws IOException {
		
		Map<String, List<String>> generatedCode = ExternalApiCodeConverter.generateCode(rpaConfig, botInfo);
		
		FileOutputStream outputStream = new FileOutputStream(filename);
		
		StringBuilder strBuilder = new StringBuilder();
		
		String docs = getCodeBlock(generatedCode, CodePlacement.WAL_DOCUMENTATION.toString());		
		strBuilder.append(docs);        
		strBuilder.append("// Input Variables \r\n");
		String inputVars = getCodeBlock(generatedCode, CodePlacement.DEF_INPUT_VARS.toString());
		strBuilder.append(inputVars);

		strBuilder.append("// Output Variables \r\n");		
		String outputVars = getCodeBlock(generatedCode, CodePlacement.DEF_OUTPUT_VARS.toString());
		strBuilder.append(outputVars);		
		strBuilder.append(  "defVar --name ResponseCode --type String --value 0 --output\r\n" + 
			            	"defVar --name ResponseMessage --type String --output\r\n");
		
		strBuilder.append("// Local Variables \r\n");			
		strBuilder.append(  "defVar --name success --type Boolean --value True\r\n"); 	
		String internalVars = getCodeBlock(generatedCode, CodePlacement.DEF_INTERNAL_VARS.toString());
		strBuilder.append(internalVars);	
		
		String responseStr = "setVar --name \"${ResponseMessage}\" --value OK\r\n";
		strBuilder.append(responseStr);
		
		String code = getCodeBlock(generatedCode, CodePlacement.MAIN.toString());
		strBuilder.append(code);
		
		
		strBuilder.append("\r\n\r\n// Add your bot code here \r\n");
		
		strBuilder.append("\r\n" + 
				"// Return response.  ResponseCode is 0 for success, any other number for error code\r\n" + 
				"if --left \"${success}\" --operator \"Is_True\"\r\n" + 
				"	setVar --name \"${ResponseCode}\" --value 0\r\n" + 
				"else\r\n" + 
				"	setVar --name \"${ResponseCode}\" --value -1\r\n" + 
				"	setVar --name \"${ResponseMessage}\" --value \"Bot failed\"\r\n" + 
				"endIf\r\n");
		
		String callApi = getCodeBlock(generatedCode, CodePlacement.CALLODM.toString());
		
		if (callApi != null) {
			strBuilder.append(callApi);			
		}
		
		byte[] strToBytes = strBuilder.toString().getBytes();
		outputStream.write(strToBytes);


		outputStream.close();
	}	

	private static String getCodeBlock(Map<String, List<String>> generatedCode, String key) {

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
