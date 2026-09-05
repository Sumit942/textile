package com.example.textile.repo;

import com.example.textile.entity.YarnBuilty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YarnBuiltyRepository extends JpaRepository<YarnBuilty, Long> {
}
