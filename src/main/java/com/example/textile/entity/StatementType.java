package com.example.textile.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class StatementType implements Serializable {
    private Long id;

    private String type;

    private String creditDebit;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column
    public String getCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(String creditDebit) {
        this.creditDebit = creditDebit;
    }

    @Override
    public String toString() {
        return "StatementType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", creditDebit='" + creditDebit + '\'' +
                '}';
    }
}
