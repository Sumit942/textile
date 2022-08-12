package com.example.textile.service;

import com.example.textile.entity.State;

import java.util.List;

public interface StateService {

    List<State> findByNameLike(String name);
}
