package converter.bwl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import converter.common.BpmnTask;
import converter.common.StringUtils;
import converter.common.TaskType;

public class BwlBpmnParser implements IBwlParser {

	private Map<String, BpmnTask> taskMap = new HashMap<String, BpmnTask>();
	private Map<String, String> sequenceMap = new HashMap<String, String>();
	private Stack<BpmnTask> startNodes = new Stack<BpmnTask>();

	public BwlBpmnParser(File inputFile, String outputFilename)
			throws SAXException, IOException, ParserConfigurationException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();

		startNodes = getStartIds(doc, outputFilename);
		getTasks(doc);
	}

	public Map<String, BpmnTask> getTaskMap() {
		return taskMap;
	}

	public Map<String, String> getSequenceMap() {
		return sequenceMap;
	}

	public Stack<BpmnTask> getStartIds() {
		return startNodes;
	}

	public BpmnTask getTask(String taskId) {
		return taskMap.get(taskId);
	}

	private void getTasks(Document doc) {
		NodeList nList = doc.getElementsByTagName("task");
		getTasksGeneric(nList);

		nList = doc.getElementsByTagName("businessRuleTask");
		getTasksGeneric(nList);

		nList = doc.getElementsByTagName("serviceTask");
		getTasksGeneric(nList);

		nList = doc.getElementsByTagName("userTask");
		getTasksGeneric(nList);
	}

	private void getTasksGeneric(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String isRobotStr = eElement.getAttribute("ns2:isRobot");
				String name = eElement.getAttribute("name");
				String id = eElement.getAttribute("id");

				boolean isRobot = Boolean.parseBoolean(isRobotStr);

				System.out.println("IsRobotStr :'" + isRobotStr + "'");
				System.out.println("IsRobot :" + isRobot);
				System.out.println("Name :" + name);
				System.out.println("Id :" + id);

				NodeList childNodes = eElement.getChildNodes();

				if (isRobot) {
					setTaskNodeAttributes(name, id, childNodes);
				}
			}
		}
	}

	private void setTaskNodeAttributes(String name, String id, NodeList childNodes) {

		String documentation = "";
		BpmnTask task = new BpmnTask();

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node link = childNodes.item(i);

			if (link.getNodeType() == Node.ELEMENT_NODE) {
				if (link.getNodeName().equals("documentation")) {
					documentation = link.getTextContent();
				}
				if (link.getNodeName().equals("propertyName")) {
					documentation = link.getTextContent();
				}
				
				documentation = StringUtils.html2text(documentation);
				task.setDocumentation(documentation);

				if (link.getNodeName().equals("ioSpecification")) {
					NodeList ioSpecification = link.getChildNodes();
					for (int m = 0; m < ioSpecification.getLength(); m++) {
						link = ioSpecification.item(m);

						if (link.getNodeName().equals("dataInput")) {
							NamedNodeMap nodeMap = link.getAttributes();

							Node inputVar = nodeMap.getNamedItem("name");
							System.out.println("Input:" + inputVar);
							task.addInputParam(inputVar.getTextContent());
						}
						
						if (link.getNodeName().equals("dataOutput")) {
							NamedNodeMap nodeMap = link.getAttributes();

							Node outputVar = nodeMap.getNamedItem("name");
							System.out.println("Output:" + outputVar);
							task.addOutputParam(outputVar.getTextContent());
						}						
					}
				}
			}
		}

		id = "1";
		task.setId(id);
		task.setType(TaskType.TASK);
		task.setName(name);

		taskMap.put(id, task);
	}

	private Stack<BpmnTask> getStartIds(Document doc, String outputFileName) {

		NodeList startEvents = doc.getElementsByTagName("startEvent");
		for (int i = 0; i < startEvents.getLength(); i++) {
			Node nNode = startEvents.item(i);
			System.out.println("\nstartEvent :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String id = eElement.getAttribute("id");

				BpmnTask startNode = taskMap.get(id);
				if (startNode == null) {
					startNode = new BpmnTask();
					startNode.setId(id);
					startNode.setName(outputFileName); // This will be overridden if its a sub task
					startNode.setType(TaskType.START);

					this.taskMap.put(id, startNode);
				} else {
					System.out.println("Parent Name :" + startNode.getName());
				}

				startNodes.push(startNode);
			}
		}

		return startNodes;
	}
}
