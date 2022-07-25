package rpa.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rpa.api.parameters.ParameterDirection;
import rpa.api.parameters.RpaParameter;

public class JsonUtils {
	
	public static String getBearerToken(String jsonStr) {		
		JSONObject jsonResonse = new JSONObject(jsonStr.toString());
		return (String) jsonResonse.get("access_token");
	}
		
	public static String getProcessId(String response) {
		JSONObject jsonResonse = new JSONObject(response.toString());
		return (String) jsonResonse.get("id");
	}
	
	public static String getStatus(String response) {
		JSONObject jsonResponse = new JSONObject(response.toString());
		return (String) jsonResponse.get("status");
	}
	
	public static String findProcessId(String response, String processName) {
		
		String processId = null;
		JSONObject jsonResponse = new JSONObject(response.toString());
		
		JSONArray processes = jsonResponse.getJSONArray("results");

		for (int j = 0; j < processes.length(); j++) {
			JSONObject process = processes.getJSONObject(j);	
			if (process.getString("name").equalsIgnoreCase(processName)) {
				return process.getString("id");
			}
		}
		
		return processId;
	}	
	
	public static Object getResultVar(String response, String varname) {
		JSONObject jsonResponse = new JSONObject(response.toString());
		
		JSONObject outputs =  jsonResponse.getJSONObject("outputs");
		
		return outputs.get(varname);
	}	
	
	public static List<RpaParameter> getSignature(String response) {
		
		List<RpaParameter> botSignature = new ArrayList<RpaParameter>();
		
		JSONObject jsonResponse = new JSONObject(response.toString());

		JSONObject schema =  jsonResponse.getJSONObject("scriptSchema");
		extractInputParams(botSignature, schema);		
		extractOutputParams(botSignature, schema);	
		
		return botSignature;
		
	}

	private static void extractInputParams(List<RpaParameter> botSignature, JSONObject schema) {
		JSONObject inputSchema =  schema.getJSONObject("inputSchema");
		JSONObject properties =  inputSchema.getJSONObject("properties");
		
		JSONArray inParams =  properties.names();
		
		for (int i = 0; i < inParams.length(); i++) {
			String paramName = (String) inParams.get(i);
			JSONObject paramType =  properties.getJSONObject(paramName);
			String objectType = paramType.getString("type");
			
			RpaParameter param = new RpaParameter(ParameterDirection.input, paramName, objectType);
			botSignature.add(param);
		}
	}
	
	private static void extractOutputParams(List<RpaParameter> botSignature, JSONObject schema) {
		JSONObject outputSchema =  schema.getJSONObject("outputSchema");
		JSONObject properties =  outputSchema.getJSONObject("properties");
		
		JSONArray outParams =  properties.names();
		
		for (int i = 0; i < outParams.length(); i++) {
			String paramName = (String) outParams.get(i);
			JSONObject paramType =  properties.getJSONObject(paramName);
			String objectType = paramType.getString("type");
			
			RpaParameter param = new RpaParameter(ParameterDirection.output, paramName, objectType);
			botSignature.add(param);
		}
	}	
	
	public static void main(String args[]) {
		String jsonStr = "{\"access_token\":\"ZCM1TYcnsaqhUV0JsBRNs1IxnzHKpGZ-HChcgn6U6pbdwGKjbCoYPYBocE8igY9kQL_PnoCCVTyy4vcSjjG89CjYLhzWGqYvv7rp3b-zlzDEQdKqYTfaWcggI-f_uTpVf07flYiEmJJpit5ThKwK6vQk5nF1utsgGfc1_BUF9wtKAUXeqUE6_m4kJW6al-pGaSxg18CiQ2yHml4jF1Wb-xCrgSFg_MORuve4qFiTxaXzM6e7g-_XbwYbG0LWthtd0aXyIXpsriFL44AuHkLjyNrC_hyltmrccdFiVXYAGgtSDEY3Cu7TcYm3_7pGtO1Hv7YX7k79uqTclsiQU_cPL09ZPWoyhF9IQUaJdmezRubXc2emvt7hGpNZzY8rjrfT1RBqpAEQmJp3X2m5Zqg0ruIV9WyBdwu-NwL0pnvccTZZMhSXmGla0LF2d2uAOiJKuIV3C055WhJtpi9SMYTQOV1gkAvEEFdiSag2QdvPEVU1OtIPEoRnZQ_fol1fIkKvFloNGMY7ySTASLiTbWOQ-8395f_kypT1c6i1kAP6uoNbjdNUAcueSzt8zAfCZn_A5Y-0LQo4c-Kzg2Z7ks1WfF0UUPmwpc-bOLP87Gsf7hSlEvyW9gC4siGZaqCBXMdEAmL_RtUArA3pTO7eJ5G8JniLtEvgkWz87nXAGVdMK1dIYc7ofcOZysdudUIBrPiqR7E9NTHxftHPNKWFw6UIqdWeqVJAiYEuHNS27KyjIcPtxaxNuLcPHwQX8y_5FjnZTPLwQ8W4I8FJtNuZ-hFk2mqKcHGNB16uBHsiF0SaiWvvy7__CkurYjemWzpWmJfaGw63WrLojG-y8mKIRSBWxWIKhfeKdybnQ3ux1nyidz5iL0Un7EAX-rzQUkB0FkSTvZ7ZxBAULYaI77UsQ6nvYLcRSwTJiP1sz3RcpozuxsZCnCQ_uuL4PSzzrPkhB_aj8wukBBo40KqgkFXH-ZNBmD4TpYpn4WwCSoB2LaKOI1F2hNcN2NytRq-_3mRyRjRYTHc7I7PJ_ELGd93WCysQSDW6K2IyYxoy46H-vDwMiOpyZOb0ZEPa56oz6qpA4lXyzP7fLqELcROuLSlhj7xNk1RIqWT6RVst4OsYJqZWoWY\",\"token_type\":\"bearer\",\"expires_in\":86399,\"Id\":\"d426a64f-8d21-4a42-8531-7b66d195f16a\",\"Name\":\"Nigel Crowther\",\"Email\":\"ncrowther@uk.ibm.com\",\"Roles\":\"\",\"Permissions\":\"PermissionWorkflowsViewKey,PermissionBotsViewKey,PermissionDashboardViewKey,PermissionMachineLearningManageKey,PermissionBotsCreateKey,PermissionInstancesCreateKey,PermissionChatMappingsViewAndUseKey,PermissionChatMappingsManageKey,PermissionQueueProvidersViewKey,PermissionBotsManageKey,PermissionQueuesViewKey,PermissionDashboardListKey,PermissionSchedulesScheduleKey,PermissionLauncherAppUseKey,PermissionProfileDownloadScriptsKey,PermissionInstancesViewKey,PermissionWorkflowsManageKey,PermissionVaultAppUseKey,PermissionQueueProvidersListKey,PermissionMachineLearningTrainKey,PermissionProfileViewKey,PermissionIvrMappingsViewAndUseKey,PermissionSchedulesViewKey,PermissionQueuesManageKey,PermissionBotsListKey,PermissionStudioAppUseKey,PermissionDashboardManageKey,PermissionSchedulesManageKey,PermissionIvrRecordsViewKey,PermissionIvrMappingsManageKey,PermissionCountersViewKey,PermissionUsersViewKey,PermissionTeamsManageKey,PermissionTeamsListKey,PermissionComputersListKey,PermissionLaunchersViewKey,PermissionLaunchersManageKey,PermissionAuditLogViewKey,PermissionProjectsViewKey,PermissionParametersViewKey,PermissionLaunchersCreateKey,PermissionCredentialsManageKey,PermissionRolesManageKey,PermissionJobsListKey,PermissionCountersManageKey,PermissionParametersManageKey,PermissionGroupListKey,PermissionTenantConfigurationChangeOwnerKey,PermissionStorageProviderTypesViewKey,PermissionUsersManageKey,PermissionUsersListKey,PermissionGroupViewKey,PermissionCredentialsViewKey,PermissionRolesListKey,PermissionComputersManageKey,PermissionProjectsManageKey,PermissionCountersUseKey,PermissionGroupManageKey,PermissionProjectsCreateKey,PermissionTeamsViewKey,PermissionRolesViewKey,PermissionStorageProvidersManageKey,PermissionComputersViewKey,PermissionTenantConfigurationViewKey,PermissionQueueProvidersManageKey,PermissionParametersUseKey,PermissionStorageProvidersViewKey,PermissionActiveLocksReleaseKey,PermissionActiveLocksViewKey,PermissionStorageProviderTypesManageKey,PermissionTenantConfigurationUpdateKey\",\"TenantName\":\"IBM – UK1\",\"TenantCode\":5001,\"LCID\":1033,\"FipsComplianceState\":0}"; 

		String bearer = getBearerToken(jsonStr);
		
		System.out.println(bearer);
	}
}
