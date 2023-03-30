package com.example.textile.command;

import com.example.textile.entity.Employee;
import com.example.textile.entity.Statement;

import java.util.List;

public class StatementCommand {

    private Employee employee;

    private List<Statement> statements;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }
}
