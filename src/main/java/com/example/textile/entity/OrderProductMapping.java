package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class OrderProductMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CompanyYarnOrderProduct companyYarnOrderProduct;
    @OneToMany(mappedBy = "orderProductMapping", cascade = CascadeType.ALL)
    private List<ProductRawMaterial> rawMaterials;
    private Double quantity;
    @ManyToOne
    @JsonBackReference
    private Orders orders;

    public void setRawMaterials(List<ProductRawMaterial> rawMaterials) {
        if (Objects.isNull(rawMaterials)) return;
        if (Objects.isNull(this.rawMaterials)) {
            this.rawMaterials = new ArrayList<>();
        }
        for (ProductRawMaterial rawMaterial : rawMaterials) {
            rawMaterial.setOrderProductMapping(this);
        }
        this.rawMaterials.addAll(rawMaterials);
    }
}
