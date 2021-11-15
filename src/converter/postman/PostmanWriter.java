package converter.postman;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import converter.common.CodePlacement;

public class PostmanWriter {

	public static void writeCollectionFile(String filename, Map<String, List<String>> generatedCode)
			throws IOException {

		FileOutputStream outputStream = new FileOutputStream(filename);

		String botName = getCode(generatedCode, CodePlacement.BOTNAME.toString());
		String urlParams = getCode(generatedCode, CodePlacement.URLPARAMS.toString());
		String queryParams = getCode(generatedCode, CodePlacement.QUERYPARAMS.toString());
		
		StringBuilder strBuilder = new StringBuilder();
			
		UUID uuid = UUID.randomUUID();
		
		strBuilder.append("{\r\n" + 
				"	\"info\": {\r\n" + 
				"		\"_postman_id\": \"" + uuid + "\",\r\n" + 
				"		\"name\": \"RPABots\",\r\n" + 
				"		\"schema\": \"https://schema.getpostman.com/json/collection/v2.1.0/collection.json\"\r\n" + 
				"	},\r\n" + 
				"	\"item\": [\r\n" + 
				"		{\r\n" + 
				"			\"name\": \"Invoke " + botName + "\",\r\n" + 
				"			\"request\": {\r\n" + 
				"				\"method\": \"POST\",\r\n" + 
				"				\"header\": [],\r\n" + 
				"				\"url\": {\r\n" + 
				"					\"raw\": \"https://localhost:8099/scripts/" + botName + "?unlockMachine=False");
				
		 strBuilder.append(urlParams);
		 
		 strBuilder.append("\",\r\n");
		 strBuilder.append("					\"protocol\": \"https\",\r\n" + 
				"					\"host\": [\r\n" + 
				"						\"localhost\"\r\n" + 
				"					],\r\n" + 
				"					\"port\": \"8099\",\r\n" + 
				"					\"path\": [\r\n" + 
				"						\"scripts\",\r\n" + 
				"						\"testbot\"\r\n" + 
				"					],\r\n" + 
				"					\"query\": [\r\n" + 
				"						{\r\n" + 
				"							\"key\": \"unlockMachine\",\r\n" + 
				"							\"value\": \"False\"\r\n" + 
				"						},\r\n");
		 
		  strBuilder.append(queryParams);
		  
		  strBuilder.append("\n					]\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			\"response\": []\r\n" + 
				"		}\r\n" + 
				"	]\r\n" + 
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
