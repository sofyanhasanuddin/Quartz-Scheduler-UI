package com.app.sofyan.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.sofyan.example.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
