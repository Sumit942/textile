package com.example.textile.dto;

import com.example.textile.entity.FabricDesign;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class YarnFabricDesignDto {
    public YarnFabricDesignDto(Long id, String qualityName) {
        this.id = id;
        this.qualityName = qualityName;
    }

    private Long id;
    private FabricDesign fabricDesign;
    private String gsm;
    private String qualityName;
}
