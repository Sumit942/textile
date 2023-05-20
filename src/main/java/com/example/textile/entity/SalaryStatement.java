package com.example.textile.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name = "salary_statement")
@PrimaryKeyJoinColumn(name = "salary_statement_id")
public class SalaryStatement extends ExpenseStatement {

    private Employee employee;

    @ManyToOne
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "SalaryStatement{" +
                "employee=" + employee.getId() +
                "expense=[" + super.toString() +
                "]}";
    }
}
