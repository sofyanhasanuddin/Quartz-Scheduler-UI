package com.app.sofyan.example.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.sofyan.example.model.Employee;
import com.app.sofyan.example.repository.EmployeeRepository;
import com.app.sofyan.example.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Resource
	private EmployeeRepository employeeRepository;

	@Override
	@Transactional(readOnly=false)
	public void save(Employee e) {
		this.employeeRepository.save(e);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Employee> findAll() {
		return this.employeeRepository.findAll();
	}

}
