package com.example.textile.repo;

import com.example.textile.entity.FabricDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FabricDesignRepo extends JpaRepository<FabricDesign, Long> {

    @Query("FROM FabricDesign fd WHERE LOWER(fd.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FabricDesign> findByNameLikeIgnoreCase(String name);
}
