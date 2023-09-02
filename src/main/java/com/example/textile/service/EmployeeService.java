package com.example.textile.service;

import com.example.textile.entity.Designation;
import com.example.textile.entity.Employee;
import com.example.textile.entity.State;

import java.util.List;

public interface EmployeeService {

    Employee saveOrUpdate(Employee employee);

    List<Employee> findAll();

    Employee findById(Long id);

    Employee findByPanCard(String panNo);

    void deleteById(Long id);

    List<Designation> findAllDesignations();

    List<State> findAllStates();
}
