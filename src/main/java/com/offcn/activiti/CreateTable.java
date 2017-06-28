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
	 * 创建流程表
	 */
	@Before
	public void createTable() {
		processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
				.buildProcessEngine();
		System.out.println("-------------processEngine:" + processEngine);
	}

	/**
	 * 将流程图部署到activiti的数据表中
	 */
	@Test
	public void deployFlow() {
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("helloworld入门程序")
				.addClasspathResource("diagram/HelloWorld.bpmn").addClasspathResource("diagram/HelloWorld.png")
				.deploy();
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void flowStart() {
		// 获取正在执行的流程实例和执行对象相关的Service
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// 使用流程定义key启动流程实例，key对象helloworld.bpmn文件中的id属性，对应act_re_procdef表的key字段
		// 用key启动时按照最新的流程版本定义启动
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorldKey");
		// 流程实例id
		System.out.println(processInstance.getId());
		// 流程定义id
		System.out.println(processInstance.getDeploymentId());

		RepositoryService repositoryService = processEngine.getRepositoryService();
		// 根据流程定义ID获取流程定义对象
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processInstance.getDeploymentId());
		// 流程定义ID
		System.out.println(processDefinition.getId());
		System.out.println(processDefinition.getKey());
	}

	/**
	 * 查询代办业务
	 */
	@Test
	public void findPersonTask() {
		String assignee = "李四";
		List<Task> taskList = this.processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();

		if (taskList != null && taskList.size() > 0) {
			for (Task task : taskList) {
				System.out.println("代办任务ID：" + task.getId() + "  ");
				System.out.println("任务名称：" + task.getName() + "  ");
				System.out.println("任务创建时间：" + task.getCreateTime() + "  ");
				System.out.println("任务办理人；" + task.getAssignee() + "  ");
				System.out.println("流程实例ID：" + task.getProcessInstanceId() + "  ");
				System.out.println("执行对象ID：" + task.getExecutionId() + "  ");
				System.out.println("流程定义ID：" + task.getProcessDefinitionId() + "  ");
				System.out.println("=================================================");
			}
		}
	}

	@Test
	public void completeTask() {
		String taskId = "7504";
		this.processEngine.getTaskService().complete(taskId);
		System.out.println("完成任务，任务ID：" + taskId);
	}

}
