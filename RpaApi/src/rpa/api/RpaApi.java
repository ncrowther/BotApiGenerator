package rpa.api;

import rpa.api.parameters.RpaParameter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import rpa.json.JsonUtils;

public class RpaApi {
	public static boolean debug = true;

	public static String getBearerToken(String baseURL, String tenantId, String username, String password) throws RpaApiException {
   
		String token = null;	
		
		HashMap<String, String> headerMap = new HashMap<String, String>();

		String body = "";

		headerMap.put("grant_type", "password");
		headerMap.put("username", username);
		headerMap.put("password", password);
		headerMap.put("culture", "en-US");

		String url = baseURL + "/v1.0/token";
		try {
			String result = postEncoded(url, body, tenantId, headerMap);
			
			token = JsonUtils.getBearerToken(result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return token;
	}
	
	public static String getProcessIdByName(String baseUrl, String tenantId, String bearerToken,  String processName) {

		String processId = null;
		
		String getProcessUrl = baseUrl
				+ "/v2.0/workspace/" + tenantId + "/process?lang=en-US";

		HashMap<String, String> headerMap = new HashMap<String, String>();

		headerMap.put("Authorization", "Bearer " + bearerToken);
		headerMap.put("Content-Type", "application/json");
		
		try {
			String result = doRest("GET",getProcessUrl, null, headerMap, null, null);
			
			processId = JsonUtils.findProcessId(result, processName);

			// System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return processId;
	}	
	
	// https://uk1api.wdgautomation.com/v2.0/workspace/e780ec1f-e62f-4148-8335-2f3ac251373e/process/ba96dc5f-600f-4bdf-b609-54f12b2855b3
	public static List<RpaParameter> getProcessDetails(String baseUrl, String tenantId, String bearerToken, String processId) {

		List<RpaParameter> botSignature = null;
			
		String startProcessUrl = baseUrl
				+ "/v2.0/workspace/" + tenantId + "/process/" + processId;

		HashMap<String, String> headerMap = new HashMap<String, String>();

		headerMap.put("Authorization", "Bearer " + bearerToken);
		headerMap.put("Content-Type", "application/json");

		try {
			String response = doRest("GET", startProcessUrl, null, headerMap, null, null);

			botSignature = JsonUtils.getSignature(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return botSignature;
	}
	
	public static String startProcessAndWait(String baseUrl, String tenantId, String username, String password, String processName, String payload, Integer waitSeconds) throws RpaApiException, InterruptedException {

		String result = null;
		
		String bearerToken = getBearerToken(baseUrl, tenantId, username, password);
		
		if (bearerToken == null) {
			throw new RpaApiException("Failed to login  to tenant +  " + tenantId + " with username " + username);
		}
		
		String processId = getProcessIdByName(baseUrl, tenantId, bearerToken, processName);
		
		if (processId == null) {
			throw new RpaApiException("Process " + processName + " not found");
		}
		
		String processInstanceId = startProcess(baseUrl, tenantId, bearerToken, processId, payload);

		Thread.sleep(waitSeconds * 1000);
			
		result = getResult( baseUrl, tenantId, bearerToken, processId, processInstanceId);

		return result;
	}
	
	private static String getResult(String baseUrl, String tenantId, String bearerToken, String processId, String processInstanceId) {

		String result = "ERROR";
		
		String getProcessUrl = baseUrl
				+ "/v2.0/workspace/" + tenantId + "/process/" + processId + "/instance/" + processInstanceId;

		HashMap<String, String> headerMap = new HashMap<String, String>();

		headerMap.put("Authorization", "Bearer " + bearerToken);
		headerMap.put("Content-Type", "application/json");
		
		try {
			result = doRest("GET", getProcessUrl, null, headerMap, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
		

	public static String doRest(String command, String urlString, String content, HashMap<String, String> headerMap,
			String userid, String password) throws Exception {
		if (debug) {
			System.out.println(">> doRest: command=" + command + ", urlString=" + urlString + ", content=" + content
					+ ", userid=" + userid + ", password=" + password);
		}

		if ((!command.equals("GET")) && (!command.equals("POST")) && (!command.equals("PUT"))) {
			throw new RpaApiException("Unsupported command: " + command + ".  Supported commands are GET, POST, PUT");
		}
		try {
			URL url = new URL(urlString);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setRequestMethod(command);

			if (headerMap != null) {
				Set keySet = headerMap.keySet();
				Iterator it = keySet.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					httpUrlConnection.addRequestProperty(key, (String) headerMap.get(key));
				}

			}

			if ((userid != null) && (!userid.isEmpty())) {
				String authorization = "Basic " + new String(Base64.encodeBase64(
						new String(new StringBuilder(String.valueOf(userid)).append(":").append(password).toString())
								.getBytes()));
				httpUrlConnection.setRequestProperty("Authorization", authorization);
			}

			if ((content != null) && ((command.equals("POST")) || (command.equals("PUT")))) {
				httpUrlConnection.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(httpUrlConnection.getOutputStream());
				wr.write(content);
				wr.flush();
				wr.close();
			}

			InputStreamReader inReader = new InputStreamReader(httpUrlConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inReader);
			StringBuffer sb = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				sb.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			httpUrlConnection.disconnect();

			if (debug) {
				System.out.println("doRest: result is " + sb.toString());
			}
			return sb.toString();
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new RpaApiException("doRest exception: " + e.toString());
		}
	}
	

	public static class RpaApiException extends Exception {
		private static final long serialVersionUID = 8768678L;

		public RpaApiException(String arg0) {
			super();
		}
	}

	private static String postEncoded(String urlString, String content, String tenantId, HashMap<String, String> params)
			throws Exception {

		try {
			URL url = new URL(urlString);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setRequestMethod("POST");

			String urlParameters = getDataString(params);

			httpUrlConnection.addRequestProperty("tenantId", tenantId);

			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;

			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setInstanceFollowRedirects(false);
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpUrlConnection.setRequestProperty("charset", "utf-8");
			httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			httpUrlConnection.setUseCaches(false);
			DataOutputStream wr = new DataOutputStream(httpUrlConnection.getOutputStream());
			wr.write(postData);
			wr.flush();
			wr.close();

			InputStreamReader inReader = new InputStreamReader(httpUrlConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inReader);
			StringBuffer sb = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				sb.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			httpUrlConnection.disconnect();

			if (debug) {
				System.out.println("doRest: result is " + sb.toString());
			}
			return sb.toString();
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new RpaApiException("doRest exception: " + e.toString());
		}
	}

	private static String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		return result.toString();
	}
	
	private static String startProcess(String baseUrl, String tenantId, String bearerToken, String processId, String payload) {

		String processInstanceId = "ERROR";
			
		String startProcessUrl = baseUrl
				+ "/v2.0/workspace/" + tenantId + "/process/" + processId + "/instance?lang=en-US";

		HashMap<String, String> headerMap = new HashMap<String, String>();

		headerMap.put("Authorization", "Bearer " + bearerToken);
		headerMap.put("Content-Type", "application/json");

		try {
			String response = doRest("POST", startProcessUrl, payload, headerMap, null, null);
			
			processInstanceId = JsonUtils.getProcessId(response);

			// System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return processInstanceId;
	}
	
	public static void main(String args[]) {

		String baseURL = "https://uk1api.wdgautomation.com";
		String tenantId = "e780ec1f-e62f-4148-8335-2f3ac251373e";
		String username = "ncrowther@uk.ibm.com";
		String password = "Porker01!";
		String processName = "Weather4U";
		String payload = "{ \"payload\": { \"in_region\": \"328328\" }}";
		final int WAIT_SECS = 30;

		try {
			
			String result = startProcessAndWait(baseURL, tenantId, username, password, processName, payload, WAIT_SECS);
			
			String status = JsonUtils.getStatus(result);
			
			if (status.equals("done")) {
				Object outputVar = JsonUtils.getResultVar(result, "out_forecast");			
				System.out.println(outputVar);
			} else {
				System.out.println("Failed: " + result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
}