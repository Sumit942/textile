package com.example.textile.command;

import com.example.textile.entity.BankStatement;
import com.example.textile.entity.Employee;
import com.example.textile.entity.SalaryStatement;

import java.util.List;

public class SalaryStatementCommand {

    private Employee employee;

    private List<SalaryStatement> statements;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<SalaryStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<SalaryStatement> statements) {
        this.statements = statements;
    }
}
