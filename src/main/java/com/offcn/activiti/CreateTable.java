package com.offcn.activiti;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

public class CreateTable {

	ProcessEngine processEngine;

	/**
	 * �������̱�
	 */
	@Before
	public void createTable() {
		processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
				.buildProcessEngine();
		System.out.println("-------------processEngine:" + processEngine);
	}

	/**
	 * ������ͼ����activiti�����ݱ���
	 */
	@Test
	public void deployFlow() {
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("helloworld���ų���")
				.addClasspathResource("diagram/HelloWorld.bpmn").addClasspathResource("diagram/HelloWorld.png")
				.deploy();
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}

	/**
	 * ��������ʵ��
	 */
	@Test
	public void flowStart() {
		// ��ȡ����ִ�е�����ʵ����ִ�ж�����ص�Service
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// ʹ�����̶���key��������ʵ����key����helloworld.bpmn�ļ��е�id���ԣ���Ӧact_re_procdef���key�ֶ�
		// ��key����ʱ�������µ����̰汾��������
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorldKey");
		// ����ʵ��id
		System.out.println(processInstance.getId());
		// ���̶���id
		System.out.println(processInstance.getDeploymentId());

		RepositoryService repositoryService = processEngine.getRepositoryService();
		// �������̶���ID��ȡ���̶������
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processInstance.getDeploymentId());
		// ���̶���ID
		System.out.println(processDefinition.getId());
		System.out.println(processDefinition.getKey());
	}

	/**
	 * ��ѯ����ҵ��
	 */
	@Test
	public void findPersonTask() {
		String assignee = "����";
		List<Task> taskList = this.processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();

		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				System.out.println("��������ID��" + task.getId() + "  ");
				System.out.println("�������ƣ�" + task.getName() + "  ");
				System.out.println("���񴴽�ʱ�䣺" + task.getCreateTime() + "  ");
				System.out.println("��������ˣ�" + task.getAssignee() + "  ");
				System.out.println("����ʵ��ID��" + task.getProcessInstanceId() + "  ");
				System.out.println("ִ�ж���ID��" + task.getExecutionId() + "  ");
				System.out.println("���̶���ID��" + task.getProcessDefinitionId() + "  ");
				System.out.println("=================================================");
			}
		}
	}

	@Test
	public void completeTask() {
		String taskId = "7504";
		this.processEngine.getTaskService().complete(taskId);
		System.out.println("�����������ID��" + taskId);
	}

}
