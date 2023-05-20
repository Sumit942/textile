package com.example.textile.entity;

import com.example.textile.enums.StatementTypeEnum;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name = "expense_statement")
@PrimaryKeyJoinColumn(name = "expense_statement_id")
public class ExpenseStatement extends BankStatement {

    private String crDr;

    public ExpenseStatement() {
        this.crDr = StatementTypeEnum.DEBIT.name();
    }

    public String getCrDr() {
        return crDr;
    }

    public void setCrDr(String crDr) {
        this.crDr = crDr;
    }

    @Override
    public String toString() {
        return "ExpenseStatement{" +
                "bankStatementId='" + this.getBankStatementId() + '\'' +
                "crDr='" + crDr + '\'' +
                '}';
    }
}
