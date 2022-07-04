package converter.junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import rpa.api.RpaApi;
import rpa.json.JsonUtils;

public class Weather4uTest {

	static final String baseURL = "https://ap1api.wdgautomation.com";
	static final String tenantId = "ad2f6157-975e-40d8-b475-68977b92f4c3";
	static final String username = "ncrowther@uk.ibm.com";
	static final String password = "Porker01!!";
	static final String processName = "Weather4u";
	static final String payload = "{ \"payload\": { \"in_region\": \"328328\", }}";
	static final String COMPLETED_STATUS = "done";
	static final int waitSeconds = 30;
	
	@Test
	public void testStartProcess() {

		try {
			String result = RpaApi.startProcessAndWait(baseURL, tenantId, username, password, processName, payload, waitSeconds);

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
