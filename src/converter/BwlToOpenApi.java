package converter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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

import java.util.Stack;

import converter.bwl.BwlJsonParser;
import converter.bwl.BwlRestApi;
import converter.bpmn.RpaConfig;
import converter.bpmn.IBpmnParser;
import converter.openApi.InternalApiWriter;
import converter.common.StringUtils;
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
			
			if (args.length > 3) {
				baseDir = args[3];	
				System.out.println("Arg[3]: " + args[3]);
			} else {
				throw new Exception("No base directory specified");
			}			

			System.out.println("Processing URL:" + bwlProcessUrl + " U:" + bwlUsername + " P:" + bwlPassword);

			String jsonString = BwlRestApi.getJson(bwlProcessUrl, bwlUsername, bwlPassword);
			
			// System.out.println(jsonString);

			IBpmnParser bpmnParser = new BwlJsonParser(jsonString);

			Stack<RpaConfig> taskIds = bpmnParser.getTaskIds();
			
			while (!taskIds.empty()) {
				RpaConfig bpmnTask = taskIds.pop();
				

				final String generatedDir = baseDir + "\\generated\\";
				String botFile =  generatedDir + StringUtils.convertToTitleCase(bpmnTask.getName());
				String walFileName = botFile + ".txt";
				String openApiFileName = botFile + ".yaml";
				
				WalWriter.writeRPAFile(walFileName, bpmnTask);
				//InternalApiWriter.writeInternalApiFile(openApiFileName, bpmnTask);
				
				System.out.println("Code generated in " + walFileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
