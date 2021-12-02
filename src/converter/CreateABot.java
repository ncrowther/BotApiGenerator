package converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import converter.bpmn.BpmnTask;
import converter.bpmn.IBpmnParser;
import converter.openApi.OpenApiWriter;
import converter.common.StringUtils;
import converter.wal.WalWriter;

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

			//Path path = Path.of(bpmnFile);
			//String jsonString = Files.readString(path, StandardCharsets.US_ASCII);
			
			String jsonString = readFile(bpmnFile);

			System.out.println(jsonString);

			IBpmnParser bpmnParser = new BwlJsonParser(jsonString);

			Stack<BpmnTask> taskIds = bpmnParser.getTaskIds();

			while (!taskIds.empty()) {
				BpmnTask bpmnTask = taskIds.pop();

				final String generatedDir = baseDir + "\\generated\\";
				
				createDirIfDoesNotExist(generatedDir);
			    
				String botFile = generatedDir + StringUtils.convertToTitleCase(bpmnTask.getName());
				String walFileName = botFile + ".txt";
				String openApiFileName = botFile + ".yaml";

				WalWriter.writeRPAFile(walFileName, bpmnTask);
				OpenApiWriter.writeOpenApiFile(openApiFileName, bpmnTask);

				System.out.println("Code generated in " + walFileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
