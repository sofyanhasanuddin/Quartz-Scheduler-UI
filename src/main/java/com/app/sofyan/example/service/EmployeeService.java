package com.app.sofyan.example.service;

import java.util.List;

import com.app.sofyan.example.model.Employee;

public interface EmployeeService {
	
	void save(Employee e);
	
	List<Employee> findAll();

}
