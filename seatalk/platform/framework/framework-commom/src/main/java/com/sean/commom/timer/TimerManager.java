package com.sean.commom.timer;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时器管理器
 * @author sean
 */
public class TimerManager
{
	private SchedulerFactory sf = new StdSchedulerFactory();
	private static final String JOB_GROUP_NAME = "DEFAULT_GROUP";
	private static final String TRIGGER_GROUP_NAME = "DEFAULT_TRIGGER_GROUP";
	private static TimerManager instance;

	public static synchronized TimerManager getInstance()
	{
		if (instance == null)
		{
			instance = new TimerManager();
		}
		return instance;
	}

	/**
	 * 添加定时任务
	 * @param jobName
	 * @param jobclass
	 * @param time
	 */
	public void addJob(String jobName, Class<?> jobclass, String time) throws Exception
	{
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, jobclass);
		CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);
		trigger.setCronExpression(time);
		sched.scheduleJob(jobDetail, trigger);
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * 删除定时任务
	 * @param jobName
	 * @throws Exception
	 */
	public void removeJob(String jobName) throws Exception
	{
		Scheduler sched = sf.getScheduler();
		sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
		sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
		sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
	}
	
	public static void main(String[] args) throws Exception
	{
		TimerManager.getInstance().addJob("job", Hello.class, "0/5 * * * * ?");
		System.out.println("over");
	}
}
