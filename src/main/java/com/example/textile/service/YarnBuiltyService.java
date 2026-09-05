package com.example.textile.service;

import com.example.textile.entity.YarnBuilty;

import java.util.List;

public interface YarnBuiltyService {
    YarnBuilty save(YarnBuilty yarnBuilty);
    YarnBuilty findById(Long id);
    List<YarnBuilty> findAll();
}
