package com.example.textile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDropdownDto {
    private Long id;
    private String name;
    private String gst;
}
