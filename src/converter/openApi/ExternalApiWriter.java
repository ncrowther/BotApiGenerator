package converter.openApi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import converter.common.CodePlacement;
import converter.config.RpaConfig;
import datastructures.BotInfo;

public class ExternalApiWriter {

	public static void writeExternalApiFile(String filename, RpaConfig rpaConfig, BotInfo botInfo)
			throws IOException {

		Map<String, List<String>> generatedCode = ExternalApiCodeConverter.generateCode(rpaConfig, botInfo);
		
		FileOutputStream outputStream = new FileOutputStream(filename);

		String tenantId = rpaConfig.getTenantId();	
		String username = rpaConfig.getRpaUser();
		String password = rpaConfig.getRpaPwd();
		
		String botName = getCode(generatedCode, CodePlacement.BOTNAME.toString());
		String baseURL = getCode(generatedCode, CodePlacement.BASE_URL.toString());
		String invokeProcessUrl = getCode(generatedCode, CodePlacement.INVOKE_URL.toString());
		String getResultUrl = getCode(generatedCode, CodePlacement.GET_RESULT_URL.toString());		
		String apiInputParams = getCode(generatedCode, CodePlacement.API_INPUT_PARAMS.toString());
		String apiOutputParams = getCode(generatedCode, CodePlacement.API_OUTPUT_PARAMS.toString());
		
		
		StringBuilder strBuilder = new StringBuilder();		
		strBuilder.append("openapi: 3.0.1\r\n" + 
			"info:\r\n" + 
			"  title: " + botName + " RPA API\r\n" + 
			"  description: |-\r\n" + 
			"    This API presents an authenticated interface to invoke the " + botName + " RPA process asynchronously via the RPA tenant.  Authentication is enforced through bearer token. ![Architecture](https://rpapi.eu-gb.mybluemix.net/Architecture.PNG)\r\n" + 
			"        The **IBM RPA API V2.0 API** provides support for external apps to the authentication flow, tenant management and configuration,workspace management, and process management APIs.\r\n" + 
			"\r\n" + 
			"    * **The Authentication APIs** provide a JSON Web Token (JWT) to be used as a bearer token to make API calls to the IBM RPA API Server.\r\n" + 
			"\r\n" + 
			"     * **The Tenant management APIs** provide endpoints to create and maintain tenants. This REST API supports creating, retrieving,modifying and deleting tenants. Only platform administrators can create tenants.\r\n" + 
			"\r\n" + 
			"     * **The Process management APIs** provide endpoints to create orchestration processes instances.This REST API supports retrieving information about processes and process instances and creating process instances.\r\n" + 
			"\r\n" + 
			"     * **The Workspace APIs** provide endpoints to retrieve informationabout the current workspace of the user. A workspace is a tenant.\r\n" + 
			"  contact:\r\n" + 
			"    email: ncrowther@uk.ibm.com\r\n" + 
			"  version: 1.0.0\r\n" + 
			"externalDocs:\r\n" + 
			"  description: Find out more about Swagger\r\n" + 
			"  url: http://swagger.io\r\n" + 
			"servers:\r\n" + 
			baseURL + 
			"tags:\r\n" + 
			"- name: RPA Secured API\r\n" + 
			"  description: Run a RPA bot through the tenant API\r\n" + 
			"  externalDocs:\r\n" + 
			"    description: Find out more\r\n" + 
			"    url: http://localhost:8080/docs\r\n" + 
			"paths:\r\n" + 
			"  /v2.0/configuration/regions:\r\n" + 
			"    get:\r\n" + 
			"      tags:\r\n" + 
			"        - Authentication\r\n" + 
			"      summary: List all available regions.\r\n" + 
			"      description: \"List all available regions where RPA SaaS servers are deployed.\\r\\n\\r\\nSample request:\\r\\n            \\r\\n    GET /v2.0/configuration/regions\"\r\n" + 
			"      responses:\r\n" + 
			"        '200':\r\n" + 
			"          description: List of Regions retrieved successfully.\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/RegionInfoModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/RegionInfoModel'\r\n" + 
			"        '400':\r\n" + 
			"          description: Bad Request\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '401':\r\n" + 
			"          description: Unauthorized\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '403':\r\n" + 
			"          description: Forbidden\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '404':\r\n" + 
			"          description: Not Found\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '500':\r\n" + 
			"          description: Internal Server Error\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"  /v1.0/{locale}/account/tenant:\r\n" + 
			"    get:\r\n" + 
			"      tags:\r\n" + 
			"        - Authentication\r\n" + 
			"      summary: List all the tenants that an user can access.\r\n" + 
			"      description: \"Lists all the tenants that the user defined in `{UserName}` can access.\\r\\n\\r\\nSample request:\\r\\n            \\r\\n    GET /v2.0/account/tenant?UserName=" + username + "\"\r\n" + 
			"      parameters:\r\n" + 
			"        - name: locale\r\n" + 
			"          in: path\r\n" + 
			"          required: true\r\n" + 
			"          description: The user's locale\r\n" + 
			"          schema:\r\n" + 
			"            type: string\r\n" + 
			"            description: The locale\r\n" + 
			"            example: en-US      \r\n" + 
			"        - name: UserName\r\n" + 
			"          in: query\r\n" + 
			"          description: The user name is the email.\r\n" + 
			"          schema:\r\n" + 
			"            type: string\r\n" + 
			"            description: The user name is the email.\r\n" + 
			"            nullable: true\r\n" + 
			"            example: " + username + "\r\n" + 
			"      responses:\r\n" + 
			"        '200':\r\n" + 
			"          description: List of Tenant Information retrieved successfully.\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                type: array\r\n" + 
			"                items:\r\n" + 
			"                  $ref: '#/components/schemas/IdNameModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                type: array\r\n" + 
			"                items:\r\n" + 
			"                  $ref: '#/components/schemas/IdNameModel'\r\n" + 
			"        '400':\r\n" + 
			"          description: Bad Request\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '401':\r\n" + 
			"          description: Unauthorized\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '403':\r\n" + 
			"          description: Forbidden\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '404':\r\n" + 
			"          description: Not Found\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"        '500':\r\n" + 
			"          description: Internal Server Error\r\n" + 
			"          content:\r\n" + 
			"            application/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
			"            text/json:\r\n" + 
			"              schema:\r\n" + 
			"                $ref: '#/components/schemas/ErrorModel'\r\n" + 
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
			"            default: " + tenantId + "\r\n" + 
			"          required: true\r\n" + 
			"      requestBody:\r\n" + 
			"        required: true\r\n" + 
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
			"                  default: " + username + "\r\n" + 
			"                password:\r\n" + 
			"                  type: string\r\n" + 
			"                  description: The user's password\r\n" + 
			"                  default: " + password + "\r\n" + 
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
		           invokeProcessUrl + 
			"    post:\r\n" + 
			"      tags:\r\n" + 
			"      - RPA invoke API\r\n" + 
			"      summary: Asynchronously runs a bot process\r\n" + 
			"      description: Asynchronously runs a process on a RPA tenant specified in the URL.  All\r\n" + 
			"        requests are authenticated using a bearer token\r\n" + 
			"      operationId: runAsyncProcess\r\n" + 
			"      parameters:\r\n" + 
			"      - name: lang\r\n" + 
			"        in: query\r\n" + 
			"        description: Bot script name to run.  This script must be published on the\r\n" + 
			"          tenant belonging to the host\r\n" + 
			"        required: false\r\n" + 
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
		         getResultUrl + 	 
			"    get:\r\n" + 
			"      tags:\r\n" + 
			"        - Get RPA Result\r\n" + 
			"      summary: Retreives the result of a bot process\r\n" + 
			"      description: Retreives the result of a process running on the RPA tenant specified in the URL.  All\r\n" + 
			"        requests are authenticated using a bearer token\r\n" + 
			"      operationId: getInvocationResult\r\n" + 
			"      parameters:\r\n" + 
			"      - name: instanceId   # Note the name is the same as in the path\r\n" + 
			"        in: path\r\n" + 
			"        required: true\r\n" + 
			"        schema:\r\n" + 
			"          type: string\r\n" + 
			"          minimum: 1\r\n" + 
			"        description: The bot process instance ID\r\n" + 
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
			"    RegionInfoModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the region.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: ExampleRegion\r\n" + 
			"        description:\r\n" + 
			"          type: string\r\n" + 
			"          description: The description of the region.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: Example region number 1\r\n" + 
			"        apiUrl:\r\n" + 
			"          type: string\r\n" + 
			"          description: The API URL for the region.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: https://my-example-url:port/version\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ErrorModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        errorMessage:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        modelState:\r\n" + 
			"          type: object\r\n" + 
			"          additionalProperties:\r\n" + 
			"            type: array\r\n" + 
			"            items:\r\n" + 
			"              type: string\r\n" + 
			"          nullable: true\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    IdNameModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the object.\r\n" + 
			"          format: uuid\r\n" + 
			"          example: 5768843c-9ba0-4302-a1e7-7baebed2cfd3\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the object.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: My-Tenant\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ProcessListItemModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the process.\r\n" + 
			"          format: uuid\r\n" + 
			"          example: 5768843c-9ba0-4302-a1e7-7baebed2cfd3\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the process.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: My process\r\n" + 
			"        description:\r\n" + 
			"          type: string\r\n" + 
			"          description: The description of the process\r\n" + 
			"          nullable: true\r\n" + 
			"          example: Process description\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ProcessListItemModelPagedResult:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        results:\r\n" + 
			"          type: array\r\n" + 
			"          items:\r\n" + 
			"            $ref: '#/components/schemas/ProcessListItemModel'\r\n" + 
			"          description: Returns a list of processes that match the criteria.\r\n" + 
			"          nullable: true\r\n" + 
			"        offset:\r\n" + 
			"          type: integer\r\n" + 
			"          description: The number of items that were skipped.\r\n" + 
			"          format: int32\r\n" + 
			"          example: 20\r\n" + 
			"        limit:\r\n" + 
			"          type: integer\r\n" + 
			"          description: The maximum number of items retrieved. If this value is 0, the request had no limit.\r\n" + 
			"          format: int32\r\n" + 
			"          example: 100\r\n" + 
			"        returned:\r\n" + 
			"          type: integer\r\n" + 
			"          description: The number of items retrieved.\r\n" + 
			"          format: int32\r\n" + 
			"          example: 50\r\n" + 
			"        total:\r\n" + 
			"          type: integer\r\n" + 
			"          description: The total amount of items retrieved.\r\n" + 
			"          format: int32\r\n" + 
			"          example: 50\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    DataType:\r\n" + 
			"      enum:\r\n" + 
			"        - 1 = Bool\r\n" + 
			"        - 2 = Text\r\n" + 
			"        - 3 = Numeric\r\n" + 
			"        - 4 = DateTime\r\n" + 
			"      type: integer\r\n" + 
			"      format: int32\r\n" + 
			"    ProcessVariableModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the variable in the process.\r\n" + 
			"          format: uuid\r\n" + 
			"          example: 3fa85f64-5717-4562-b3fc-2c963f66afa6\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of variable.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: variableName\r\n" + 
			"        isBusinessKey:\r\n" + 
			"          type: boolean\r\n" + 
			"          description: '`true` if the variable is a business key; else, `false`.'\r\n" + 
			"          example: true\r\n" + 
			"        type:\r\n" + 
			"          $ref: '#/components/schemas/DataType'\r\n" + 
			"        variableType:\r\n" + 
			"          type: string\r\n" + 
			"          description: 'The type of the variable. Possible values: [string, number, boolean, date-time].'\r\n" + 
			"          nullable: true\r\n" + 
			"          readOnly: true\r\n" + 
			"          example: string\r\n" + 
			"        format:\r\n" + 
			"          type: string\r\n" + 
			"          description: 'The format of the variable. Possible values: [string, number, boolean, date-time]'\r\n" + 
			"          nullable: true\r\n" + 
			"          readOnly: true\r\n" + 
			"          example: date-time\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ScriptMetadataModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the script.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: scriptName\r\n" + 
			"        description:\r\n" + 
			"          type: string\r\n" + 
			"          description: The description of the script.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: Script Description\r\n" + 
			"        variableType:\r\n" + 
			"          $ref: '#/components/schemas/DataType'\r\n" + 
			"        type:\r\n" + 
			"          type: string\r\n" + 
			"          description: 'The type of the variable. Possible values: [string, number, boolean, date-time].'\r\n" + 
			"          nullable: true\r\n" + 
			"          example: string\r\n" + 
			"        format:\r\n" + 
			"          type: string\r\n" + 
			"          description: 'The format of the variable. Possible values: [string, number, boolean, date-time]'\r\n" + 
			"          nullable: true\r\n" + 
			"          example: date-time\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ScriptInputOutputSchemaModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        title:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the variable.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: My Title\r\n" + 
			"        type:\r\n" + 
			"          type: string\r\n" + 
			"          description: The type of the variable.\r\n" + 
			"          nullable: true\r\n" + 
			"          readOnly: true\r\n" + 
			"          example: string\r\n" + 
			"        properties:\r\n" + 
			"          type: object\r\n" + 
			"          additionalProperties:\r\n" + 
			"            $ref: '#/components/schemas/ScriptMetadataModel'\r\n" + 
			"          description: Properties of the object\r\n" + 
			"          nullable: true\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ScriptVersionSchemaModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the script\r\n" + 
			"          nullable: true\r\n" + 
			"          example: scriptName\r\n" + 
			"        description:\r\n" + 
			"          type: string\r\n" + 
			"          description: The description of the script.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: My description\r\n" + 
			"        inputs:\r\n" + 
			"          $ref: '#/components/schemas/ScriptInputOutputSchemaModel'\r\n" + 
			"        outputs:\r\n" + 
			"          $ref: '#/components/schemas/ScriptInputOutputSchemaModel'\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ProcessModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the process.\r\n" + 
			"          format: uuid\r\n" + 
			"          example: 3fa85f64-5717-4562-b3fc-2c963f66afa6\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the process.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: processname\r\n" + 
			"        description:\r\n" + 
			"          type: string\r\n" + 
			"          description: The description of the process.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: Process Description\r\n" + 
			"        variables:\r\n" + 
			"          type: array\r\n" + 
			"          items:\r\n" + 
			"            $ref: '#/components/schemas/ProcessVariableModel'\r\n" + 
			"          description: The variables in the process.\r\n" + 
			"          nullable: true\r\n" + 
			"        scriptSchema:\r\n" + 
			"          $ref: '#/components/schemas/ScriptVersionSchemaModel'\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    SingleStepProcessExecutionModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        payload:\r\n" + 
			"          description: \"Enter an JSON to assign values to the script's input variables. The keys in the JSON must have the same name as the input parameters from the script. \\r\\nIf the script contains input and output variables, the 'Get process information request' returns the names and types of these variables in the scriptSchema.\"\r\n" + 
			"          nullable: true\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ProcessId:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the process.\r\n" + 
			"          format: uuid\r\n" + 
			"          readOnly: true\r\n" + 
			"          example: 3fa85f64-5717-4562-b3fc-2c963f66afa6\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ProcessInstanceStatus:\r\n" + 
			"      enum:\r\n" + 
			"        - 1 = New\r\n" + 
			"        - 2 = Processing\r\n" + 
			"        - 4 = Pausing\r\n" + 
			"        - 8 = Paused\r\n" + 
			"        - 16 = Canceling\r\n" + 
			"        - 32 = Canceled\r\n" + 
			"        - 64 = Failed\r\n" + 
			"        - 128 = Done\r\n" + 
			"        - 256 = Resuming\r\n" + 
			"        - 512 = Retrying\r\n" + 
			"      type: integer\r\n" + 
			"      format: int32\r\n" + 
			"    ProcessInstanceStateVariableModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the variable.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: MyProcessInstanceVariable\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    ProcessInstanceStateModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        status:\r\n" + 
			"          $ref: '#/components/schemas/ProcessInstanceStatus'\r\n" + 
			"        statusName:\r\n" + 
			"          type: string\r\n" + 
			"          description: The status of the process.\r\n" + 
			"          nullable: true\r\n" + 
			"          readOnly: true\r\n" + 
			"          example: done\r\n" + 
			"        variables:\r\n" + 
			"          type: array\r\n" + 
			"          items:\r\n" + 
			"            $ref: '#/components/schemas/ProcessInstanceStateVariableModel'\r\n" + 
			"          description: The list of variables in the process.\r\n" + 
			"          nullable: true\r\n" + 
			"        outputs:\r\n" + 
			"          type: object\r\n" + 
			"          additionalProperties: {}\r\n" + 
			"          description: The output variables from the script that processed this instance.\r\n" + 
			"          nullable: true\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    SaveTenantV2Model:\r\n" + 
			"      required:\r\n" + 
			"        - name\r\n" + 
			"        - userEmail\r\n" + 
			"        - userName\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        name:\r\n" + 
			"          maxLength: 50\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the tenant.\r\n" + 
			"          example: RPA-Customer\r\n" + 
			"        userEmail:\r\n" + 
			"          type: string\r\n" + 
			"          description: The email of the user that will be the tenant owner.\r\n" + 
			"          format: email\r\n" + 
			"          example: " + username + "\r\n" + 
			"        userName:\r\n" + 
			"          maxLength: 50\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the user that will be the tenant owner.\r\n" + 
			"          example: " + username + "\r\n" + 
			"      additionalProperties: false\r\n" + 
			"      description: The model used to create a new tenant.\r\n" + 
			"    TenantId:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the tenant.\r\n" + 
			"          format: uuid\r\n" + 
			"          readOnly: true\r\n" + 
			"          example: " + tenantId + "\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    TenantModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the tenant.\r\n" + 
			"          format: uuid\r\n" + 
			"          example: 3fa85f64-5717-4562-b3fc-2c963f66afa6\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the tenant.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: IBM-RPA\r\n" + 
			"        isApproved:\r\n" + 
			"          type: boolean\r\n" + 
			"          description: Whether the tenant is approved for use.\r\n" + 
			"          example: true\r\n" + 
			"        isDeactivated:\r\n" + 
			"          type: boolean\r\n" + 
			"          description: Whether the tenant is deactivated.\r\n" + 
			"          example: false\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    WorkspaceModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          description: The ID of the workspace. A workspace is a tenant.\r\n" + 
			"          format: uuid\r\n" + 
			"          example: 3fa85f64-5717-4562-b3fc-2c963f66afa6\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          description: The name of the workspace. A workspace is a tenant.\r\n" + 
			"          nullable: true\r\n" + 
			"          example: IBM-RPA\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    AuthenticateModel:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        grant_type:\r\n" + 
			"          type: string\r\n" + 
			"          description: The type of the login method. Use 'password'.\r\n" + 
			"          default: password\r\n" + 
			"          nullable: true\r\n" + 
			"        username:\r\n" + 
			"          type: string\r\n" + 
			"          description: The user's email to log in to the tenant.\r\n" + 
			"          example: " + username + "\r\n" + 
			"        password:\r\n" + 
			"          type: string\r\n" + 
			"          description: The user's password.\r\n" + 
			"          format: password\r\n" + 
			"          nullable: true\r\n" + 
			"        culture:\r\n" + 
			"          type: string\r\n" + 
			"          description: The code of the language. See [Supported languages](https://www.ibm.com/docs/en/rpa/21.0?topic=api-reference#supported-languages) for the supported language codes. Default code is 'en-US'\r\n" + 
			"          default: en-US\r\n" + 
			"          nullable: true\r\n" + 
			"      additionalProperties: false\r\n" + 
			"    FipsComplianceState:\r\n" + 
			"      enum:\r\n" + 
			"        - 0 = Disabled\r\n" + 
			"        - 1 = InTransition\r\n" + 
			"        - 2 = Enabled\r\n" + 
			"      type: integer\r\n" + 
			"      format: int32\r\n" + 
			"    AuthenticationTokenResponse:\r\n" + 
			"      type: object\r\n" + 
			"      properties:\r\n" + 
			"        id:\r\n" + 
			"          type: string\r\n" + 
			"          format: uuid\r\n" + 
			"        name:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        email:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        lcid:\r\n" + 
			"          type: integer\r\n" + 
			"          format: int32\r\n" + 
			"          nullable: true\r\n" + 
			"        roles:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        permissions:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        tenantName:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        tenantCode:\r\n" + 
			"          type: integer\r\n" + 
			"          format: int32\r\n" + 
			"          nullable: true\r\n" + 
			"        tenantTrialUrl:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        fipsComplianceState:\r\n" + 
			"          $ref: '#/components/schemas/FipsComplianceState'\r\n" + 
			"        access_token:\r\n" + 
			"          type: string\r\n" + 
			"          nullable: true\r\n" + 
			"        expires_in:\r\n" + 
			"          type: integer\r\n" + 
			"          format: int32\r\n" + 
			"      additionalProperties: false  \r\n" + 
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
			"      bearerFormat: JWT");

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
