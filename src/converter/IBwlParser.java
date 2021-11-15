package converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import converter.bpmn.BpmnTask;

/* 
 * Licensed Materials - Property of IBM Corporation.
 * 
 * 5725-A20
 * 
 * Copyright IBM Corporation 2021. All Rights Reserved.
 * 
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corporation.
 */
public interface IBwlParser {

	Map<String, BpmnTask> taskMap = new HashMap<String, BpmnTask>();
	Map<String, String> sequenceMap = new HashMap<String, String>();
	Stack<BpmnTask> startNodes = new Stack<BpmnTask>();

	public Map<String, BpmnTask> getTaskMap();

	public Map<String, String> getSequenceMap();

	public Stack<BpmnTask> getStartIds();

	public BpmnTask getTask(String taskId);
}
