package converter.bwl;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import bwlapi.BwlRestApi;
import converter.common.BpmnTask;
import converter.common.OpenApiWriter;
import converter.common.PostmanWriter;
import converter.common.StringUtils;
import converter.common.WalWriter;

public class BwlToApi {
	
	public static void main(String[] args) {

		String bwlProcessUrl= "";
		
		try {
			if (args.length > 0) {
				bwlProcessUrl = args[0];					
			} else {
				System.err.println("No URL specified");
				System.exit(1);
			}

			System.out.println("Processing " + bwlProcessUrl);

			String jsonString = BwlRestApi.getJson(bwlProcessUrl);
			IBwlParser bpmnParser = new BwlJsonParser(jsonString);

			Stack<BpmnTask> startIds = bpmnParser.getStartIds();
			
			while (!startIds.empty()) {
				BpmnTask startNode = startIds.pop();
				
				CodeConverter.reset();

				Map<String, List<String>> codeMap = CodeConverter.generateWDGFunctionCode(startNode.getId(), bpmnParser);

				final String generatedDir = "D:\\RPA\\github\\BotApiGenerator\\generated\\";
				String botFile =  generatedDir + StringUtils.convertToTitleCase(startNode.getName());
				String walFileName = botFile + ".txt";
				String postmanFileName = botFile + ".json";
				String openApiFileName = botFile + ".yaml";
				
				WalWriter.writeWDGFile(walFileName, codeMap);
				PostmanWriter.writeCollectionFile(postmanFileName, codeMap);
				OpenApiWriter.writeCollectionFile(openApiFileName, codeMap);
				
				System.out.println("Code generated in " + walFileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
