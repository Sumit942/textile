package com.example.textile.entity;

import com.example.textile.enums.CompanyTypeEnum;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CompanyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String type = CompanyTypeEnum.CLIENT.name();
}
