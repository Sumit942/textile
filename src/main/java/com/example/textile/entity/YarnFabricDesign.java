package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
public class YarnFabricDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "yarn_fabric_design_id")
    @JsonManagedReference
    private List<FabricDesignYarnMapping> fabricDesignYarnMappings;
    @ManyToOne
    private FabricDesign fabricDesign;
    private String gsm;
    private String qualityName;

    @PrePersist
    public void perPersist() {
        String designYarns = "-";
        if (!CollectionUtils.isEmpty(fabricDesignYarnMappings)) {
            designYarns = fabricDesignYarnMappings.stream().map(designMap -> designMap.getYarn().getType())
                    .collect(Collectors.joining(" x "));
        }
        String designName = Objects.nonNull(fabricDesign) ? fabricDesign.getName() : "";
        String designGsm = Objects.nonNull(gsm) ? gsm : "";
        String quality = String.format("%s --%s (Gsm:%s)", designYarns, designName, designGsm );
        setQualityName(quality);
    }
}
