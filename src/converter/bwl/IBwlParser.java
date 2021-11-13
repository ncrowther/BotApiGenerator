package converter.bwl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import converter.common.BpmnTask;


public interface IBwlParser {

	Map<String, BpmnTask> taskMap = new HashMap<String, BpmnTask>();
	Map<String, String> sequenceMap = new HashMap<String, String>();
	Stack<BpmnTask> startNodes = new Stack<BpmnTask>();

	public Map<String, BpmnTask> getTaskMap();

	public Map<String, String> getSequenceMap();

	public Stack<BpmnTask> getStartIds();

	public BpmnTask getTask(String taskId);
}
