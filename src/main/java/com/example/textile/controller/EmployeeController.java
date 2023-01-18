package com.example.textile.controller;

import com.example.textile.constants.CommandConstants;
import com.example.textile.entity.Employee;
import com.example.textile.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    EmployeeRepository employeeRepo;

    @GetMapping
    public ModelAndView viewEmployee(@ModelAttribute(CommandConstants.EMPLOYEE_COMMAND) Employee employee) {
        ModelAndView modelAndView = new ModelAndView("/employees");
        List<Employee> employees = employeeRepo.findAll();
        modelAndView.addObject("employees",employees);

        return modelAndView;
    }
}
