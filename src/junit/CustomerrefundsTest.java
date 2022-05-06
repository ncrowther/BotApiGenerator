package junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import rpa.api.RpaApi;
import rpa.json.JsonUtils;

public class CustomerrefundsTest {

	static final String baseURL = "https://uk1api.wdgautomation.com";
	static final String tenantId = "e780ec1f-e62f-4148-8335-2f3ac251373e";
	static final String username = "ncrowther@uk.ibm.com";
	static final String password = "Porker01!";
	static final String processName = "Customerrefunds";
	static final String payload = "{ \"payload\": { \"in_param_paymentType\": \"Card\",\"in_param_refundReason\": \"Guts\",\"invokerId\": \"ntc\",\"in_param_ticket\": \"T546566\",\"in_param_repaymentAmt\": \"£56.76\", }}";
	static final String COMPLETED_STATUS = "done";
	static final int waitSeconds = 40;
	
	@Test
	public void testStartProcess() {

		try {		

			String result = RpaApi.startProcessAndWait(baseURL, tenantId, username, password, processName, payload, waitSeconds);

			assertNotNull(result, "Result not null");

			String status = JsonUtils.getStatus(result);

			assertTrue("Expected: " + COMPLETED_STATUS + ", Actual: " + status, status.equals(COMPLETED_STATUS));

			Object outputVar = null;
			outputVar = JsonUtils.getResultVar(result, "out_success"); 
			System.out.println(outputVar);
			outputVar = JsonUtils.getResultVar(result, "out_desc"); 
			System.out.println(outputVar);
			outputVar = JsonUtils.getResultVar(result, "out_code"); 
			System.out.println(outputVar);
		
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
