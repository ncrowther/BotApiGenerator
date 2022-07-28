package junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import rpa.api.RpaApi;
import rpa.json.JsonUtils;

public class RefundsTest {

	static final String baseURL = "https://ap1api.wdgautomation.com";
	static final String tenantId = "ad2f6157-975e-40d8-b475-68977b92f4c3";
	static final String username = "ncrowther@uk.ibm.com";
	static final String password = "Porker01!!";
	static final String processName = "Refunds";
	static final String payload = "{ \"payload\": { \"in_param_paymentType\": \"string\",\"purchaseDate\": \"2022-01-01 00:00:00\",\"birthdate\": \"2022-01-01 00:00:00\",\"isHappy\": \"true\",\"amountInteger\": \"123456\",\"in_param_refundReason\": \"string\",\"in_param_ticket\": \"string\",\"in_param_repaymentAmt\": \"string\", }}";
	static final String COMPLETED_STATUS = "done";
	static final int waitSeconds = 20;
	
	@Before
	public void setUp()
	{
	  // SECURITY: REMOVE THE LINE BELOW IF NOT USING THE SKYTAP LAB TENANT
	  RpaApi.ignoreSSL();
	}	

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
