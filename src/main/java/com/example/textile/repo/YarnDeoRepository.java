package com.example.textile.repo;

import com.example.textile.entity.YarnDeo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YarnDeoRepository extends JpaRepository<YarnDeo,Long> {

    List<YarnDeo> findByPartyName(String partyName);
}
