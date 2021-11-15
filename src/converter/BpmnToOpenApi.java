package converter;

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
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import converter.bpmn.CodeConverter;
import converter.IBwlParser;
import converter.bpmn.BwlBpmnParser;
import converter.bpmn.BpmnTask;
import converter.openApi.OpenApiWriter;
import converter.common.StringUtils;
import converter.postman.PostmanWriter;
import converter.wal.WalWriter;

public class BpmnToOpenApi {
	
	public static void main(String[] args) {

		String inputFileName= "";
		String walFileName = null;
		String postmanFileName = null;
		String openApiFileName = null;
		
		try {
			if (args.length > 0) {
				inputFileName = args[0];
				String extension =  StringUtils.getExtensionByStringHandling(inputFileName);
				
				if (extension.equals("bpmn")) {
				    walFileName = inputFileName.replace(extension, "txt");
				    walFileName = walFileName.replace("data", "generated");
				    
				    postmanFileName = inputFileName.replace(extension, "json");
				    postmanFileName = postmanFileName.replace("data", "generated");
				    
				    openApiFileName = inputFileName.replace(extension, "yaml");
				    openApiFileName = openApiFileName.replace("data", "generated");			
				} else {
					System.err.println("Please supply a bpmn file");
					System.exit(1);					
				}				
			} else {
				System.err.println("No file specified");
				System.exit(1);
			}

			System.out.println("Processing " + inputFileName);
			
			File inputFile = new File(inputFileName);		
			IBwlParser bpmnParser = new BwlBpmnParser(inputFile, walFileName);

			Stack<BpmnTask> startIds = bpmnParser.getStartIds();
			
			while (!startIds.empty()) {
				BpmnTask startNode = startIds.pop();
				
				CodeConverter.reset();

				Map<String, List<String>> codeMap = CodeConverter.generateWDGFunctionCode(startNode.getId(), bpmnParser);

				walFileName =  startNode.getName();
				
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
