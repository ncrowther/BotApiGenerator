package junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import rpa.api.RpaApi;
import rpa.json.JsonUtils;

public class Weather4uTest {

	static final String baseURL = "https://uk1api.wdgautomation.com";
	static final String tenantId = "e780ec1f-e62f-4148-8335-2f3ac251373e";
	static final String username = "ncrowther@uk.ibm.com";
	static final String password = "Porker01!";
	static final String processName = "Weather4u";
	static final String payload = "{ \"payload\": { \"in_region\": \"328328\", }}";
	static final String COMPLETED_STATUS = "done";
	static final int waitSeconds = 30;
	
	@Test
	public void testStartProcess() {

		try {		
			String token = RpaApi.getBearerToken(baseURL, tenantId, username, password);
			assertNotNull( token, "Token generated");

			String result = RpaApi.startProcessAndWait(baseURL, tenantId, token, processName, payload, waitSeconds);

			assertNotNull(result, "Result not null");

			String status = JsonUtils.getStatus(result);

			assertTrue("Expected: " + COMPLETED_STATUS + ", Actual: " + status, status.equals(COMPLETED_STATUS));

			Object outputVar = null;
			outputVar = JsonUtils.getResultVar(result, "out_forecastDateTime"); 
			System.out.println(outputVar);
			outputVar = JsonUtils.getResultVar(result, "out_success"); 
			System.out.println(outputVar);
			outputVar = JsonUtils.getResultVar(result, "out_forecast"); 
			System.out.println(outputVar);
		
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
