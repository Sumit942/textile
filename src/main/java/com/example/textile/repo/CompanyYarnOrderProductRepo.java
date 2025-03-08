package com.example.textile.repo;

import com.example.textile.entity.CompanyYarnOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyYarnOrderProductRepo extends JpaRepository<CompanyYarnOrderProduct, Long> {

    @Query("SELECT cyop.id,cyop.machine FROM CompanyYarnOrderProduct cyop where cyop.yarnFabricDesign.id=:yarnFabricProductId")
    List<Object[]> findIdAndMachineByYarnFabricProductId(Long yarnFabricProductId);
}
