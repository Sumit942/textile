package com.example.textile.serviceImpl;

import com.example.textile.entity.YarnDeo;
import com.example.textile.repo.YarnDeoRepository;
import com.example.textile.service.YarnDeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YarnDeoServiceImpl implements YarnDeoService {

    @Autowired
    YarnDeoRepository yarnDeoRepo;

    @Override
    public YarnDeo saveOrUpdate(YarnDeo yarnDeo) {
        return yarnDeoRepo.save(yarnDeo);
    }

    @Override
    public List<YarnDeo> findAll() {
        return yarnDeoRepo.findAll();
    }

    @Override
    public List<YarnDeo> findByPartyName(String partyName) {
        return yarnDeoRepo.findByPartyName(partyName);
    }
}
