package com.example.textile.repo;

import com.example.textile.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByGst(String gst);

    @Query("select c from Company c where lower(c.name) like concat('%',lower(:name),'%')")
    List<Company> findByNameLike(String name);

    Company findByName(String name);
}
