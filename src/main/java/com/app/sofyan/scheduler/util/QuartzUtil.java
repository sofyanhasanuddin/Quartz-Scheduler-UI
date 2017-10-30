package com.app.sofyan.scheduler.util;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.sofyan.scheduler.job.DefaultJob;
	

@Component
public class QuartzUtil {
	
	private static final String GROUP = "GROUP";
	
	@Autowired
	private Scheduler scheduler;
	
	public void createScheduler(com.app.sofyan.scheduler.model.Scheduler model) throws SchedulerException {
		
		Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
				.withIdentity( model.getId().toString() , GROUP )
				.withSchedule( org.quartz.CronScheduleBuilder.cronSchedule( model.getCronExpression() ) )
				.startNow()	
				.build();
			
		TriggerKey existTg = org.quartz.TriggerKey.triggerKey( model.getId().toString() , GROUP);
		
		if( scheduler.checkExists( existTg ) ) {
			scheduler.unscheduleJob( existTg );
			scheduler.deleteJob( org.quartz.JobKey.jobKey( model.getId().toString(), GROUP ) );
		}
			
		JobDetail job = org.quartz.JobBuilder.newJob( DefaultJob.class )
			    .withIdentity( model.getId().toString() , GROUP )
			    .usingJobData("jobClass", model.getJobClass())
			    .build();

		scheduler.scheduleJob( job, trigger );
		
	}
	
	public void stopJob( String id ) throws SchedulerException {
		
		TriggerKey tg = org.quartz.TriggerKey.triggerKey( id , GROUP);
		JobKey key = org.quartz.JobKey.jobKey( id, GROUP );
		
		if( scheduler.checkExists( tg ) ) {
			scheduler.unscheduleJob( tg );
			scheduler.deleteJob( key );
		}
		
	}
	
	public boolean getJobStatus( String id ) throws SchedulerException {
		
		TriggerKey tg = org.quartz.TriggerKey.triggerKey( id , GROUP);
		
		return scheduler.checkExists( tg );
		
	}

}
