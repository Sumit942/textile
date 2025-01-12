package com.example.textile.dto;

import lombok.Data;

@Data
public class YarnDto {
    private Long id;
    private String type;
    private CompanyDto company;
    private String description;
    private String rate;
}
