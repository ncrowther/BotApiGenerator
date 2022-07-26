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

import java.util.stream.Stream;

import converter.openApi.ExternalApiWriter;
import converter.openApi.InternalApiWriter;
import converter.common.StringUtils;
import converter.config.ConfigFileParser;
import converter.config.IConfigParser;
import converter.config.RpaConfig;
import converter.junit.JUnitWriter;
import datastructures.BotInfo;
import rpa.api.RpaApi;
import rpa.api.RpaApi.RpaApiException;
import rpa.api.parameters.RpaParameter;

public class CreateABot {

	public static void main(String[] args) {

		String configFile = "";
		String baseDir = ".";

		System.out.println("Args: " + args.length);

		try {
			if (args.length > 0) {
				configFile = args[0];
				System.out.println("Arg[0]: " + args[0]);
			} else {
				throw new Exception("No config file specified");
			}

			if (args.length > 1) {
				baseDir = args[1];
				System.out.println("Arg[1]: " + args[1]);
			} else {
				throw new Exception("No base directory specified");
			}
			
			if (args.length > 2) {
				String noSSL = args[2];
				System.out.println("Arg[2]: " + args[2]);
				if (noSSL.equals("Y")) {
					RpaApi.ignoreSSL();
				}
			}

			System.out.println("Config file :" + configFile);

			String jsonString = readFile(configFile);
			IConfigParser configParser = new ConfigFileParser(jsonString);
			RpaConfig rpaConfig = configParser.getConfig();
			
			System.out.println(rpaConfig);

			BotInfo botInfo = getBotInfo(rpaConfig);

			final String generatedDir = baseDir + "\\generated\\";

			createDirIfDoesNotExist(generatedDir);

			String botFile = generatedDir + StringUtils.convertToTitleCase(rpaConfig.getName());
			String walFileName = botFile + ".txt";
			String internalOpenApiFileName = botFile + "_internal.yaml";
			String externalOpenApiFileName = botFile + "_external.yaml";
			String junitFileName = botFile + "Test.java";

			// WalWriter.writeRPAFile(walFileName, rpaConfig);
			// System.out.println("Wal generated in " + walFileName);

			InternalApiWriter.writeInternalApiFile(internalOpenApiFileName, rpaConfig, botInfo);
			System.out.println("Internal API generated in " + internalOpenApiFileName);

			ExternalApiWriter.writeExternalApiFile(externalOpenApiFileName, rpaConfig, botInfo);
			System.out.println("External API generated in " + externalOpenApiFileName);

			JUnitWriter.writeJUnitFile(junitFileName, rpaConfig, botInfo);
			System.out.println("JUnit test generated in " + junitFileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static BotInfo getBotInfo(RpaConfig rpaConfig) throws RpaApiException {
		
		BotInfo botInfo = new BotInfo();
		
		String baseURL = rpaConfig.getBaseUrl();
		String tenantId = rpaConfig.getTenantId();
		String username = rpaConfig.getRpaUser();
		String password = rpaConfig.getRpaPwd();
		String processName = rpaConfig.getName();

		String token = RpaApi.getBearerToken(baseURL, tenantId, username, password);

		String processId = RpaApi.getProcessIdByName(rpaConfig.getBaseUrl(), rpaConfig.getTenantId(), token,
				processName);

		List<RpaParameter> botSignature = RpaApi.getProcessDetails(rpaConfig.getBaseUrl(), rpaConfig.getTenantId(),
				token, processId);

		botInfo.setBotSignature(botSignature);
		botInfo.setWorkspaceId(tenantId);
		botInfo.setProcessId(processId);
		
		System.out.println("BotInfo: " + botInfo);
		
		return botInfo;
	}

	private static void createDirIfDoesNotExist(final String generatedDir) {
		File directory = new File(generatedDir);
		if (!directory.exists()) {
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}
	}

	private static String readFile(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}
}
