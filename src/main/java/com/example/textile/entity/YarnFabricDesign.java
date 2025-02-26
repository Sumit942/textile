package com.example.textile.entity;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
public class YarnFabricDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private Set<FabricDesignYarnMapping> yarns;
    @ManyToOne
    private FabricDesign fabricDesign;
    private String gsm;
    @Transient
    private String quality;

    public String getQuality() {
        String designYarns = "-";
        if (!CollectionUtils.isEmpty(yarns)) {
            designYarns = yarns.stream().map(designMap -> designMap.getYarn().getType())
                    .collect(Collectors.joining(" x "));
        }
        String designName = Objects.nonNull(fabricDesign) ? fabricDesign.getName() : "";
        String designGsm = Objects.nonNull(gsm) ? gsm : "";
        this.quality = String.format("%s - %s (%s)", designYarns, designName, designGsm );
        return quality;
    }
}
