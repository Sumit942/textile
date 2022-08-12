package com.example.textile.repo;

import com.example.textile.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {

    @Query("select s from State s where lower(s.name) like concat('%',lower(:name),'%')")
    List<State> findByNameLike(String name);
}
