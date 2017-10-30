package com.app.sofyan.scheduler.job;

import org.quartz.JobExecutionContext;


public abstract class BaseJob {
	
	public abstract void execute(JobExecutionContext context);

}
