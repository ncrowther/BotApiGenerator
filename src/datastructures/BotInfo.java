package datastructures;

import java.util.List;

import rpa.api.parameters.RpaParameter;

public class BotInfo {

	String workspaceId;
	String processId;
	List<RpaParameter> botSignature;
	
	public String getWorkspaceId() {
		return workspaceId;
	}
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public List<RpaParameter> getBotSignature() {
		return botSignature;
	}
	public void setBotSignature(List<RpaParameter> botSignature) {
		this.botSignature = botSignature;
	}
	
	@Override
	public String toString() {
		return "BotInfo [workspaceId=" + workspaceId + ", processId=" + processId + ", botSignature=" + botSignature
				+ "]";
	}	
	
	
}
