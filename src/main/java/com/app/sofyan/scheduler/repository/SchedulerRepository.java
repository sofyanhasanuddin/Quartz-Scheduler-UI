package com.app.sofyan.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.sofyan.scheduler.model.Scheduler;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

}
