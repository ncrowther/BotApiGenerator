package converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
import java.util.stream.Stream;

import converter.bwl.BwlJsonParser;
import converter.bpmn.RpaConfig;
import converter.bpmn.IBpmnParser;
import converter.openApi.ExternalApiWriter;
import converter.openApi.InternalApiWriter;
import converter.common.StringUtils;
import converter.junit.JUnitWriter;
import converter.wal.WalWriter;
import rpa.api.RpaApi;
import rpa.api.RpaApi.RpaApiException;
import rpa.api.parameters.RpaParameter;
import rpa.json.JsonUtils;

public class CreateABot {

	public static void main(String[] args) {

		String bpmnFile = "";
		String baseDir = ".";

		System.out.println("Args: " + args.length);

		try {
			if (args.length > 0) {
				bpmnFile = args[0];
				System.out.println("Arg[0]: " + args[0]);
			} else {
				throw new Exception("No file specified");
			}

			if (args.length > 1) {
				baseDir = args[1];
				System.out.println("Arg[1]: " + args[1]);
			} else {
				throw new Exception("No base directory specified");
			}

			System.out.println("Processing :" + bpmnFile);		
			
			String jsonString = readFile(bpmnFile);

			System.out.println(jsonString);

			IBpmnParser bpmnParser = new BwlJsonParser(jsonString);

			Stack<RpaConfig> rpaConfigs = bpmnParser.getTaskIds();

			while (!rpaConfigs.empty()) {
				RpaConfig rpaConfig = rpaConfigs.pop();
		
				List<RpaParameter> botSignature = fetchBotSignature(rpaConfig);
	
				final String generatedDir = baseDir + "\\generated\\";
				
				createDirIfDoesNotExist(generatedDir);
			    
				String botFile = generatedDir + StringUtils.convertToTitleCase(rpaConfig.getName());
				String walFileName = botFile + ".txt";
				String internalOpenApiFileName = botFile + "_internal.yaml";
				String externalOpenApiFileName = botFile + "_external.yaml";				
				String junitFileName = botFile + "Test.java";
				
				//WalWriter.writeRPAFile(walFileName, rpaConfig);
				//System.out.println("Wal generated in " + walFileName);
				
				InternalApiWriter.writeInternalApiFile(internalOpenApiFileName, rpaConfig, botSignature);
				System.out.println("Internal API generated in " + internalOpenApiFileName);

				ExternalApiWriter.writeExternalApiFile(externalOpenApiFileName, rpaConfig, botSignature);
				System.out.println("External API generated in " + externalOpenApiFileName);
				
				JUnitWriter.writeJUnitFile(junitFileName, rpaConfig, botSignature);
				System.out.println("JUnit test generated in " + junitFileName);				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<RpaParameter> fetchBotSignature(RpaConfig rpaConfig) throws RpaApiException {
		String baseURL = rpaConfig.getBaseUrl();
		String tenantId = rpaConfig.getTenantId();
		String username = rpaConfig.getRpaUser();
		String password = rpaConfig.getRpaPwd();
		String processName = rpaConfig.getName();

		String token = RpaApi.getBearerToken(baseURL, tenantId, username, password);
		
		String processId = RpaApi.getProcessIdByName(rpaConfig.getBaseUrl(), 
				                                     rpaConfig.getTenantId(), 
				                                     token,
				                                     processName);
		
		List<RpaParameter> botSignature = RpaApi.getProcessDetails(rpaConfig.getBaseUrl(), rpaConfig.getTenantId(),
				token, processId);
		
		System.out.println("botSignature: " + botSignature);
		
		return botSignature;
	}

	private static void createDirIfDoesNotExist(final String generatedDir) {
		File directory = new File(generatedDir);
		if (! directory.exists()){
		    directory.mkdir();
		    // If you require it to make the entire directory path including parents,
		    // use directory.mkdirs(); here instead.
		}
	}
	
	private static String readFile(String filePath)
	{
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream < String > stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
		{
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}
}
