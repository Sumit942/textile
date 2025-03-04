package com.example.textile.dto;

import com.example.textile.entity.FabricDesign;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class YarnFabricDesignDto {
    private Long id;
    private FabricDesign fabricDesign;
    private String gsm;
    private String qualityName;
}
