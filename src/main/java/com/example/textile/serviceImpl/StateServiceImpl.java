package com.example.textile.serviceImpl;

import com.example.textile.entity.State;
import com.example.textile.repo.StateRepository;
import com.example.textile.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    @Autowired
    private StateRepository stateRepo;

    @Override
    public List<State> findByNameLike(String name) {
        return this.stateRepo.findByNameLike(name);
    }

    @Override
    public State findByCode(Integer code) {
        return this.stateRepo.findByCode(code);
    }
}
