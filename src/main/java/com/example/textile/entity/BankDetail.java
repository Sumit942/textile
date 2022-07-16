package com.example.textile.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
public class BankDetail implements Serializable {
    @Id
    private Long id;
    private String bankName;
    private BigInteger accountNo;
    private String ifsc;
    private String branch;
}
