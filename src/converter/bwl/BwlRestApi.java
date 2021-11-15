package converter.bwl;

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
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

/**
 * Call the Blueworks Live REST APIs.
 * 
 */
public class BwlRestApi {
	/*
	 * This value is required if
	 * the user exists in more than one account. If the user exists in only one
	 * account, you can set this value to null.
	 */
	private final static String REST_API_ACCOUNT_NAME = null;

	public static String getJson(String bwlProcessUrl, String bwlUsername, String bwlPassword) {

		String jsonResult = "";

		String restApiGetProcess = bwlProcessUrl.replace("/scr/processes/", "/bwl/blueprints/");

		try {
			// Call the REST APIs. In this example, you are calling the API to return the
			// list of users.
			StringBuilder appListUrlBuilder = new StringBuilder(restApiGetProcess);

			// If the user exists in more than one account, specify the account to use when
			// authenticating.
			if (REST_API_ACCOUNT_NAME != null) {
				appListUrlBuilder.append("&account=").append(REST_API_ACCOUNT_NAME);
			}

			HttpURLConnection restApiURLConnection = getRestApiConnection(appListUrlBuilder.toString(), bwlUsername, bwlPassword);
			if (restApiURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.err.println(
						"Error calling the Blueworks Live REST API: " + restApiURLConnection.getResponseMessage());
				System.exit(1);
			}

			// Process the JSON result. This example prints the name of each user.
			InputStream restApiStream = restApiURLConnection.getInputStream();
			try {
				jsonResult = readInputStream(restApiStream);
				// System.out.println(jsonResult);

			} finally {
				// Clean up the streams you opened.
				restApiStream.close();
			}

		} catch (Exception e) {
			// Handle the exceptions that might occur. You might
			// want to handle
			// authentication problems separately so that the user will know their
			// credentials caused the problem.
			e.printStackTrace();
		}
		return jsonResult;
	}

	/**
	 * Set up the connection to a REST API including handling the Basic
	 * Authentication request headers that must be present on every API call.
	 * 
	 * @param apiCall
	 *            The URL string indicating the API call and parameters.
	 * @return the open connection
	 */
	public static HttpURLConnection getRestApiConnection(String apiCall, String bwlUsername, String bwlPassword) throws IOException {

		// Call the provided API on the Blueworks Live server.
		URL restApiUrl = new URL(apiCall);
		HttpURLConnection restApiURLConnection = (HttpURLConnection) restApiUrl.openConnection();

		// Add the HTTP Basic Authentication header that must be present on every API
		// call.
		addAuthenticationHeader(restApiURLConnection, bwlUsername, bwlPassword);

		return restApiURLConnection;
	}

	/**
	 * Add the HTTP Basic Authentication header that must be present on every API
	 * call.
	 * 
	 * @param restApiURLConnection
	 *            The open connection to the REST API.
	 */
	private static void addAuthenticationHeader(HttpURLConnection restApiURLConnection, String bwlUsername, String bwlPassword) {
		String userPwd = bwlUsername + ":" + bwlPassword;
		String encoded = DatatypeConverter.printBase64Binary(userPwd.getBytes());
		restApiURLConnection.setRequestProperty("Authorization", "Basic " + encoded);
	}

	private static String readInputStream(InputStream inputStream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		for (int result = bis.read(); result != -1; result = bis.read()) {
			buf.write((byte) result);
		}
		// StandardCharsets.UTF_8.name() > JDK 7
		return buf.toString("UTF-8");
	}
}
