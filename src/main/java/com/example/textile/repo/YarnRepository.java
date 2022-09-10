package com.example.textile.repo;

import com.example.textile.entity.Yarn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YarnRepository extends JpaRepository<Yarn, Long> {
}
