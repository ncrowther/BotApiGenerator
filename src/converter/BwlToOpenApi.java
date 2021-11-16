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
import java.util.List;
import java.util.Map;
import java.util.Stack;

import converter.bpmn.CodeConverter;
import converter.IBwlParser;
import converter.bwl.BwlRestApi;
import converter.bpmn.BpmnTask;
import converter.openApi.OpenApiWriter;
import converter.common.StringUtils;
import converter.jsonparser.BwlJsonParser;
import converter.postman.PostmanWriter;
import converter.wal.WalWriter;

public class BwlToOpenApi {
	
	public static void main(String[] args) {

		String bwlProcessUrl= "";
		String bwlUsername = "";
		String bwlPassword = "";
		String baseDir = ".";
		
		System.out.println("Args: " + args.length);
		
		try {
			if (args.length > 0) {
				bwlProcessUrl = args[0];	
				System.out.println("Arg[0]: " + args[0]);
			} else {
				throw new Exception("No URL specified");
			}				
			
			if (args.length > 1) {
				bwlUsername = args[1];
				System.out.println("Arg[1]: " + args[1]);
			} else {
				throw new Exception("No BWL Username supplied");
			}	
			
			if (args.length > 2) {
				bwlPassword = args[2];	
				System.out.println("Arg[2]: " + args[2]);
			} else {
				throw new Exception("No BWL Password supplied");
	
			}	
			
			// D:\\RPA\\github\\BotApiGenerator
			if (args.length > 3) {
				baseDir = args[3];	
				System.out.println("Arg[3]: " + args[3]);
			} else {
				throw new Exception("No base directory specified");
			}			

			System.out.println("Processing URL:" + bwlProcessUrl + " U:" + bwlUsername + " P:" + bwlPassword);

			String jsonString = BwlRestApi.getJson(bwlProcessUrl, bwlUsername, bwlPassword);
			
			// System.out.println(jsonString);

			IBwlParser bpmnParser = new BwlJsonParser(jsonString);

			Stack<BpmnTask> startIds = bpmnParser.getStartIds();
			
			while (!startIds.empty()) {
				BpmnTask startNode = startIds.pop();
				
				CodeConverter.reset();

				Map<String, List<String>> codeMap = CodeConverter.generateWDGFunctionCode(startNode.getId(), bpmnParser);

				final String generatedDir = baseDir + "\\generated\\";
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
