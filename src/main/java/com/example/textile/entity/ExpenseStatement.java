package com.example.textile.entity;

import com.example.textile.enums.StatementTypeEnum;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name = "expense_statement")
@PrimaryKeyJoinColumn(name = "expense_statement_id")
public class ExpenseStatement extends BankStatement {

    private String statementType;

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    @Override
    public String toString() {
        return "ExpenseStatement{" +
                "statementType='" + statementType + '\'' +
                '}';
    }
}
