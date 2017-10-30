package com.app;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@SpringBootApplication
public class SchedulerApplication {
	
	@Value("classpath:dbscript/insert.sql")
	private Resource dataScript;
	
	@javax.annotation.Resource
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}
	
	@Bean
	public Scheduler getScheduler() throws SchedulerException {
		
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.start();
		
		return sched;
		
	}
	
	@EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
		
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript( dataScript );
		populator.setContinueOnError(false);
		
		DatabasePopulatorUtils.execute( populator, dataSource );
		
    }
	
}
