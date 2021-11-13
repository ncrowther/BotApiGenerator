package converter.common;

import java.util.ArrayList;
import java.util.List;

public class BpmnTask {
	String id;
	String startId;
	TaskType type;
	String name;
	String documentation;	

	List<String> inputParams = new ArrayList<String>();
    List<String> outputParams = new ArrayList<String>();
    
	public String getId() {
		return id;
	}
	
	public TaskType getType() {
		return type;
	}
	public void setType(TaskType type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStartId() {
		return startId;
	}

	public void setStartId(String startId) {
		this.startId = startId;
	}

	public List<String> getInputParams() {
		return inputParams;
	}	

	public void addInputParam(String inputParam) {
		this.inputParams.add(inputParam);
	}

	public List<String> getoutputParams() {
		return outputParams;
	}

	public void addOutputParam(String outputParam) {
		this.outputParams.add(outputParam);
	}
	
    public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}	

	@Override
	public String toString() {
		return "BpmnTask [id=" + id + ", type=" + type + ", name=" + name + ", inputParams=" + inputParams
				+ ", outputParams=" + outputParams + "]";
	}	
}
