package converter.openApi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import converter.bpmn.RpaConfig;
import converter.common.CodePlacement;
import rpa.api.parameters.RpaParameter;

public class ExternalApiWriter {

	public static void writeExternalApiFile(String filename, RpaConfig rpaConfig, List<RpaParameter> botSignature)
			throws IOException {

		Map<String, List<String>> generatedCode = ExternalApiCodeConverter.generateCode(rpaConfig, botSignature);
		
		FileOutputStream outputStream = new FileOutputStream(filename);

		String botName = getCode(generatedCode, CodePlacement.BOTNAME.toString());
		String description = getCode(generatedCode, CodePlacement.API_DOCUMENTATION.toString());
		String apiInputParams = getCode(generatedCode, CodePlacement.API_INPUT_PARAMS.toString());
		String apiOutputParams = getCode(generatedCode, CodePlacement.API_OUTPUT_PARAMS.toString());
		String host = getCode(generatedCode, CodePlacement.API_SYSTEM.toString());
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("openapi: 3.0.1\r\n" + 
				"info:\r\n" + 
				"  title: RPA Authenticated Asynchronous API\r\n" + 
				"  description: |-\r\n" + 
				"    This API presents an authenticated interface to invoke bot scripts asynchronously via the RPA tenant.  Authentication is enforced through bearer token. ![Architecture](https://rpapi.eu-gb.mybluemix.net/Architecture.png)\r\n" + 
				"    url: https://uk1api.wdgautomation.com/v2.0\r\n" + 
				"    Examples and labs- <br><br>[Presentation](/RPAWithAppConnect.pdf) <br>[Lab Guide](/labguide.pdf) <br>[Video](https://youtu.be/_BL6wobZlJ8) <br>[IBM RPA Agent API](https://www.ibm.com/docs/en/rpa/21.0?topic=bot-starting-bots-by-api-call) <br>[Git Repo](https://github.com/ncrowther/rpa-secure-gateway.git)\r\n" + 
				"  contact:\r\n" + 
				"    email: ncrowther@uk.ibm.com\r\n" + 
				"  version: 1.0.0\r\n" + 
				"externalDocs:\r\n" + 
				"  description: Find out more about Swagger\r\n" + 
				"  url: http://swagger.io\r\n" + 
				"servers:\r\n" + 
				"- url: https://uk1api.wdgautomation.com\r\n" + 
				"tags:\r\n" + 
				"- name: RPA Secured API\r\n" + 
				"  description: Run a RPA bot through the tenant API\r\n" + 
				"  externalDocs:\r\n" + 
				"    description: Find out more\r\n" + 
				"    url: http://localhost:8080/docs\r\n" + 
				"paths:\r\n" + 
				"  /v1.0/token:\r\n" + 
				"    post:\r\n" + 
				"      tags:\r\n" + 
				"      - Login to RPA tenant\r\n" + 
				"      summary: Login to RPA tenant using tenant credentials\r\n" + 
				"      description: Asynchronously runs a process on a RPA tenant specified in the URL.  All\r\n" + 
				"        requests are authenticated using a bearer token\r\n" + 
				"      operationId: RpaLogin\r\n" + 
				"      parameters:\r\n" + 
				"        - in: header\r\n" + 
				"          name: tenantId\r\n" + 
				"          schema:\r\n" + 
				"            type: string\r\n" + 
				"            format: uuid\r\n" + 
				"            default: e780ec1f-e62f-4148-8335-2f3ac251373e\r\n" + 
				"          required: true\r\n" + 
				"      requestBody:\r\n" + 
				"        content:\r\n" + 
				"          application/x-www-form-urlencoded:\r\n" + 
				"            schema:\r\n" + 
				"              type: object\r\n" + 
				"              properties:\r\n" + 
				"                grant_type:\r\n" + 
				"                  type: string\r\n" + 
				"                  description: Type of log in method. Default type is password\r\n" + 
				"                  default: password\r\n" + 
				"                username:\r\n" + 
				"                  type: string\r\n" + 
				"                  description: Type of log in method. Default type is\r\n" + 
				"                  default: ncrowther@uk.ibm.com\r\n" + 
				"                password:\r\n" + 
				"                  type: string\r\n" + 
				"                  description: The user's password\r\n" + 
				"                  default: Porker01!\r\n" + 
				"                culture:\r\n" + 
				"                  type: string\r\n" + 
				"                  description: The code of the language. See Supported languages for the supported language codes\r\n" + 
				"                  example: en-US\r\n" + 
				"                  default: en-US\r\n" + 
				"      responses:\r\n" + 
				"        200:\r\n" + 
				"          description: Response for the execution of the bot. Contains  the id of the running process\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/RPAAuthorizationResponse'\r\n" + 
				"        400:\r\n" + 
				"          description: Invalid Grant\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/RPAInvalidGrant'\r\n" + 
				"      deprecated: false\r\n" + 
				"  /v2.0/workspace/{workspaceId}/process/{processId}/instance:\r\n" + 
				"    post:\r\n" + 
				"      tags:\r\n" + 
				"      - RPA invoke API\r\n" + 
				"      summary: Asynchronously runs a bot process\r\n" + 
				"      description: Asynchronously runs a process on a RPA tenant specified in the URL.  All\r\n" + 
				"        requests are authenticated using a bearer token\r\n" + 
				"      operationId: runAsyncProcess\r\n" + 
				"      parameters:\r\n" + 
				"      - name: workspaceId   # Note the name is the same as in the path\r\n" + 
				"        in: path\r\n" + 
				"        required: true\r\n" + 
				"        schema:\r\n" + 
				"          type: string\r\n" + 
				"          minimum: 1\r\n" + 
				"        description: The process ID\r\n" + 
				"      - name: processId   # Note the name is the same as in the path\r\n" + 
				"        in: path\r\n" + 
				"        required: true\r\n" + 
				"        schema:\r\n" + 
				"          type: string\r\n" + 
				"          minimum: 1\r\n" + 
				"        description: The process ID\r\n" + 
				"      - name: lang\r\n" + 
				"        in: query\r\n" + 
				"        description: Bot script name to run.  This script must be published on the\r\n" + 
				"          tenant belonging to the host\r\n" + 
				"        required: true\r\n" + 
				"        schema:\r\n" + 
				"          type: string\r\n" + 
				"      requestBody:\r\n" + 
				"        description: Input parameters corresponding to the RPA bot script to be invoked.\r\n" + 
				"        content:\r\n" + 
				"          application/json:\r\n" + 
				"            schema:\r\n" + 
				"              $ref: '#/components/schemas/RPARequest'\r\n" + 
				"        required: false\r\n" + 
				"      responses:\r\n" + 
				"        200:\r\n" + 
				"          description: Response for the execution of the bot.\r\n" + 
				"            Contains  the id of the running process\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/RPAInvocationResponse'\r\n" + 
				"        400:\r\n" + 
				"          description: Bad Request\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/RPAInvocationError'\r\n" + 
				"        401:\r\n" + 
				"          description: Unauthorized\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/RPAUnauthorizedResponse'\r\n" + 
				"        404:\r\n" + 
				"          description: Not Found\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/RPAInvocationResponse'\r\n" + 
				"        default:\r\n" + 
				"          description: Error occurring when invoking the execution of the decision\r\n" + 
				"            service operation.\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/Error'\r\n" + 
				"      security:\r\n" + 
				"        - bearerAuth: []\r\n" + 
				"      deprecated: false\r\n" + 
				"  /v2.0/workspace/{workspaceId}/process/{processId}/instance/{instanceId}:\r\n" + 
				"    get:\r\n" + 
				"      tags:\r\n" + 
				"        - Get RPA Result\r\n" + 
				"      summary: Retreives the result of a bot process\r\n" + 
				"      description: Retreives the result of a process running on the RPA tenant specified in the URL.  All\r\n" + 
				"        requests are authenticated using a bearer token\r\n" + 
				"      operationId: getInvocationResult\r\n" + 
				"      parameters:\r\n" + 
				"      - name: workspaceId   # Note the name is the same as in the path\r\n" + 
				"        in: path\r\n" + 
				"        required: true\r\n" + 
				"        schema:\r\n" + 
				"          type: string\r\n" + 
				"          minimum: 1\r\n" + 
				"        description: The process ID\r\n" + 
				"      - name: processId   # Note the name is the same as in the path\r\n" + 
				"        in: path\r\n" + 
				"        required: true\r\n" + 
				"        schema:\r\n" + 
				"          type: string\r\n" + 
				"          minimum: 1\r\n" + 
				"        description: The process ID\r\n" + 
				"      - name: instanceId   # Note the name is the same as in the path\r\n" + 
				"        in: path\r\n" + 
				"        required: true\r\n" + 
				"        schema:\r\n" + 
				"          type: string\r\n" + 
				"          minimum: 1\r\n" + 
				"        description: The instance ID\r\n" + 
				"      responses:\r\n" + 
				"        200:\r\n" + 
				"          description: Response for the execution of the bot.\r\n" + 
				"            Contains  the output parameters that are returned by the bot execution.\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/RPAResponse'\r\n" + 
				"        400:\r\n" + 
				"          description: Bad Request\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/Error'\r\n" + 
				"        401:\r\n" + 
				"          description: Unauthorized\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/Error'\r\n" + 
				"        404:\r\n" + 
				"          description: Not Found\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/Error'\r\n" + 
				"        default:\r\n" + 
				"          description: Error occurring when invoking the execution of the decision\r\n" + 
				"            service operation.\r\n" + 
				"          content:\r\n" + 
				"            application/json:\r\n" + 
				"              schema:\r\n" + 
				"                $ref: '#/components/schemas/Error'\r\n" + 
				"      deprecated: false\r\n" + 
				"      security:\r\n" + 
				"        - bearerAuth: []\r\n" + 
				"components:\r\n" + 
				"  schemas:\r\n" + 
				"    RPAAuthorizationResponse:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        access_token:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The authorization token returned by the login\"\r\n" + 
				"          example: \"U0R52k0pimsbgvaKWW6UU9V...\"\r\n" + 
				"        token_type:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The type of token returned by the login\"\r\n" + 
				"          example: \"bearer\"\r\n" + 
				"        expires_in:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"Time, in seconds, that the token can be used before expiring. By default, the token expires in 24 hours\"\r\n" + 
				"          example: \"86399\"\r\n" + 
				"        id:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The user ID\"\r\n" + 
				"          example: \"d426a64f-8d21-4a42-8531-7b66d195f3fs\"\r\n" + 
				"        name:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The user name\"\r\n" + 
				"          example: \"Ned Groggs\"\r\n" + 
				"        email:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The user's email\"\r\n" + 
				"          example: \"ned.groggs@ibm.com\"\r\n" + 
				"        tenantName:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The name of the current tenant\"\r\n" + 
				"          example: \"IBM RPA\"\r\n" + 
				"        tenantCode:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The code of the current tenant\"\r\n" + 
				"          example: \"5000\"\r\n" + 
				"        roles:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"The user's roles in the current tenant\"\r\n" + 
				"          example: \"Owner,SuperAdmin,Admin,User\"\r\n" + 
				"        LCID:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"Returns the language code identifier. For supported language codes\"\r\n" + 
				"          example: \"en-US\"\r\n" + 
				"        FipsComplianceState:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"Returns 2 if the tenant complies with the Federal Information Processing Standards (FIPS), 1 if the tenant is in the process of complying with FIPS, or 0 if the tenant does not comply with the standards.\"\r\n" + 
				"          example: \"2\"\r\n" + 
				"    RPARequest:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        payload:\r\n" + 
				"          $ref: '#/components/schemas/RPAParams'\r\n" + 
				"    RPAParams:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
			           apiInputParams +
				"      description: Input parameters corresponding to the RPA bot script to be invoked.      \r\n" + 
				"    RPAInvocationResponse:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        id:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"Invocation identifier\"\r\n" + 
				"          example: \"90fe0a56-4fe9-4ff3-837c-8737702961d1\"\r\n" + 
				"    RPAResponse:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        status:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"State of async call\"\r\n" + 
				"          example: \"Done\"\r\n" + 
				"        variables:\r\n" + 
				"         type: array\r\n" + 
				"         items:\r\n" + 
				"          $ref: '#/components/schemas/RPABotVariables'\r\n" + 
				"        outputs:\r\n" + 
				"          $ref: '#/components/schemas/RPABotResponseSignature'\r\n" +  
				"    RPABotVariables:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        name:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"Name of bot process variable\"\r\n" + 
				"          example: \"invokerId\"\r\n" + 
				"        value:\r\n" + 
				"          type: string\r\n" + 
				"          description: \"Id of the invoker\"\r\n" + 
				"          example: \"123456789\"\r\n" + 
				"    RPABotResponseSignature:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
			             apiOutputParams + 
				"    RPAInvocationError:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        errorMessage:\r\n" + 
				"          type: string\r\n" + 
				"          description: Error message.\r\n" + 
				"        modelState:\r\n" + 
				"          type: string\r\n" + 
				"          description: State of process.\r\n" + 
				"    RPAInvalidGrant:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        error:\r\n" + 
				"          type: string\r\n" + 
				"          description: Error code.\r\n" + 
				"        error_description:\r\n" + 
				"          type: string\r\n" + 
				"          description: Error message.\r\n" + 
				"    RPAUnauthorizedResponse:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        message:\r\n" + 
				"          type: string\r\n" + 
				"          description: Error message.\r\n" + 
				"    Error:\r\n" + 
				"      type: object\r\n" + 
				"      properties:\r\n" + 
				"        code:\r\n" + 
				"          type: integer\r\n" + 
				"          description: HTTP error code.\r\n" + 
				"          format: int32\r\n" + 
				"        message:\r\n" + 
				"          type: string\r\n" + 
				"          description: Error message.\r\n" + 
				"        details:\r\n" + 
				"          type: string\r\n" + 
				"          description: Detailed error message.\r\n" + 
				"        errorCode:\r\n" + 
				"          type: string\r\n" + 
				"          description: Product error code.\r\n" + 
				"      description: Error occurring when invoking the execution of the decision service\r\n" + 
				"        operation.\r\n" + 
				"  securitySchemes:\r\n" + 
				"    bearerAuth:\r\n" + 
				"      type: http\r\n" + 
				"      description: JWT Authorization header using the Bearer scheme.\r\n" + 
				"      scheme: bearer\r\n" + 
				"      bearerFormat: JWT\r\n" + 
				"\r\n");  

		strBuilder.append("\n");		
		
		byte[] strToBytes = strBuilder.toString().getBytes();
		outputStream.write(strToBytes);

		outputStream.close();
	}

	private static String getCode(Map<String, List<String>> generatedCode, String key) {

		List<String> functionCode = generatedCode.get(key);

		StringBuilder strBuilder = new StringBuilder();

		if (functionCode != null) {
			for (String codeLine : functionCode) {
				if (codeLine != null) {
					strBuilder.append(codeLine);
				}
			}
		}

		return strBuilder.toString();
	}
}
