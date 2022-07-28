package datastructures;

import rpa.api.parameters.BotSignature;

public class BotInfo {

	String workspaceId;
	String processId;
	BotSignature botSignature;
	
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
	public BotSignature getBotSignature() {
		return botSignature;
	}
	public void setBotSignature(BotSignature botSignature) {
		this.botSignature = botSignature;
	}
	
	@Override
	public String toString() {
		return "BotInfo [workspaceId=" + workspaceId + ", processId=" + processId + ", botSignature=" + botSignature
				+ "]";
	}	
	
	
}
