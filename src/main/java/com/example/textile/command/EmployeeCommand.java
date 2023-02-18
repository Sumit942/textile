package com.example.textile.command;

import com.example.textile.entity.Employee;

import javax.validation.Valid;

public class EmployeeCommand {

    private Employee employee;

    @Valid
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}

