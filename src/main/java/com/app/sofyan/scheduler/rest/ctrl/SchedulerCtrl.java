package com.app.sofyan.scheduler.rest.ctrl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.sofyan.scheduler.model.Scheduler;
import com.app.sofyan.scheduler.repository.SchedulerRepository;
import com.app.sofyan.scheduler.util.Message;
import com.app.sofyan.scheduler.util.QuartzUtil;
import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

@RestController	
public class SchedulerCtrl {
	
	@Resource
	private SchedulerRepository schedulerRepository;
	
	@Resource
	private QuartzUtil quartzUtil;
	
	@PostMapping( path="create", 
				  consumes = { MediaType.APPLICATION_JSON_VALUE },
				  produces = MediaType.APPLICATION_JSON_VALUE)	
	public Message create(@RequestBody Scheduler scheduler) {
		
		Scheduler saved = this.schedulerRepository.save( scheduler );
		
		try {
			this.quartzUtil.createScheduler( saved );
		} catch (SchedulerException e) {
			e.printStackTrace();
			this.schedulerRepository.delete( saved );
			return Message.error("fail to create scheduler");
		}
		
		return Message.success("scheduler is created and alread run");
		
	}
	
	@GetMapping( path="findone/{id}")	
	public Message findOne(@PathVariable("id") Long id) {
		
		return Message.success("data found", 
				this.schedulerRepository.findOne( id ) );
		
	}
	
	@GetMapping( path="start/{id}")	
	public Message start(@PathVariable("id") Long id) {
		
		Scheduler model = this.schedulerRepository.findOne( id );
		if( model == null ) 
			return Message.success( String.format("Cannot find scheduler with id : %d", id) );
		
		try {
			this.quartzUtil.createScheduler( model );
		} catch (SchedulerException e) {
			e.printStackTrace();
			return Message.error("error while starting scheduler");
		}	
		
		return Message.success("scheduler is started");
		
	}
	
	@GetMapping( path="stop/{id}")	
	public Message stop(@PathVariable("id") Long id) {
		
		Scheduler model = this.schedulerRepository.findOne( id );
		if( model == null ) 
			return Message.success( String.format("Cannot find scheduler with id : %d", id) );
		
		try {
			this.quartzUtil.stopJob( Long.toString( id ) );
		} catch (SchedulerException e) {
			e.printStackTrace();
			return Message.error("error while stoping scheduler");
		}
		
		return Message.success("scheduler is stoped");
		
	}
	
	@GetMapping( path="delete/{id}")	
	public Message delete(@PathVariable("id") Long id) {
		
		Scheduler model = this.schedulerRepository.findOne( id );
		if( model == null ) 
			return Message.success( String.format("Cannot find scheduler with id : %d", id) );
		
		try {
			
			this.quartzUtil.stopJob( Long.toString( id ) );
			this.schedulerRepository.delete( id );
			
		} catch (SchedulerException e) {
			e.printStackTrace();
			return Message.error("error while deleting scheduler");
		}	
		
		return Message.success("scheduler is deleted");
		
	}
	
	@PostMapping( path="list",
				consumes = { MediaType.APPLICATION_JSON_VALUE },
				produces = MediaType.APPLICATION_JSON_VALUE )	
	public Message listscheduler(@RequestBody HashMap<String, Object> param) {
		
		
		Pageable pageAble = new PageRequest( 
				Integer.valueOf( param.get("offset").toString() ), 
				Integer.valueOf( param.get("limit").toString() ) );
		
		Page<Scheduler> page = this.schedulerRepository.findAll( pageAble );
		
		if( CollectionUtils.isEmpty( page.getContent() ) ) 
			return Message.success("no data found", new HashMap<String, Object>());
		
		//get a predefined instance
		CronDefinition cronDefinition =	
		CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);

		//create a parser based on provided definition
		CronParser parser = new CronParser(cronDefinition);
	
		//create a descriptor for a specific Locale
		CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);
		
		page.getContent()
			.stream()
			.forEach( s ->  { 
				s.setCronHumanExpression( descriptor.describe( parser.parse( s.getCronExpression() ) ) );
				try {
					s.setActive( this.quartzUtil.getJobStatus( s.getId().toString() ));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		
		Map<String, Object> resp = new HashMap<String, Object>();
		resp.put("total", page.getTotalPages());
		resp.put("rows", page.getContent() );

		return Message.success("data found", resp);
		
	}

}
