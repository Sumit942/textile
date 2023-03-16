package com.example.textile.serviceImpl;

import com.example.textile.entity.Designation;
import com.example.textile.entity.Employee;
import com.example.textile.entity.State;
import com.example.textile.repo.DesignationRepository;
import com.example.textile.repo.EmployeeRepository;
import com.example.textile.repo.StateRepository;
import com.example.textile.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    DesignationRepository designationRepo;

    @Autowired
    StateRepository stateRepo;

    @Override
    public Employee saveOrUpdate(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepo.findById(id).orElse(null);
    }

    @Override
    public Employee findByPanCard(String panNo) {
        return employeeRepo.findByPanCardNo(panNo);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepo.deleteById(id);
    }

    @Override
    public List<Designation> findAllDesignations() {
        return designationRepo.findAll();
    }

    @Override
    public List<State> findAllStates() {
        return stateRepo.findAll();
    }
}
