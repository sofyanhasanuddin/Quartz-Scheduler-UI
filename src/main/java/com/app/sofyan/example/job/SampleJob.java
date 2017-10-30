package com.app.sofyan.example.job;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.springframework.util.CollectionUtils;

import com.app.sofyan.example.model.Employee;
import com.app.sofyan.example.service.EmployeeService;
import com.app.sofyan.scheduler.job.BaseJob;

public class SampleJob extends BaseJob {
	
	@Resource
	private EmployeeService employeeService;

	@Override
	public void execute(JobExecutionContext context) {
		
		if( CollectionUtils.isEmpty(this.employeeService.findAll()) ) {
		
			System.out.println("Start saving employee...");
			
			Employee e = new Employee();
			e.setName("Sofyan Hasanuddin");
			
			this.employeeService.save(e);
		
		}
		
		System.out.println("Print db employee...");
		
		this.employeeService.findAll().forEach( em-> {
			System.out.println( "ID : " + em.getId() + ", Name : " + em.getName());
		});
		
	}

}
