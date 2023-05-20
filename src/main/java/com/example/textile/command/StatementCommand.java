package com.example.textile.command;

import com.example.textile.entity.Employee;
import com.example.textile.entity.BankStatement;

import java.util.List;

public class StatementCommand {

    private Employee employee;

    private List<BankStatement> statements;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<BankStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<BankStatement> statements) {
        this.statements = statements;
    }
}
