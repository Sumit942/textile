package com.example.textile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class YarnDto {
    public YarnDto(Long id, String type) {
        this.id = id;
        this.type = type;
    }
    private Long id;
    private String type;
    private CompanyDto company;
    private String description;
    private String rate;
}
