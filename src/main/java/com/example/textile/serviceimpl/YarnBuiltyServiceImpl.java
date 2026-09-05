package com.example.textile.serviceimpl;

import com.example.textile.entity.YarnBuilty;
import com.example.textile.repo.YarnBuiltyRepository;
import com.example.textile.service.YarnBuiltyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@Service
public class YarnBuiltyServiceImpl implements YarnBuiltyService {

    @Autowired
    private YarnBuiltyRepository yarnBuiltyRepository;

    @Override
    @Transactional
    public YarnBuilty save(YarnBuilty yarnBuilty) {
        String logPrefix = "save()";
        log.info(createEntryLog(logPrefix));
        YarnBuilty saved = yarnBuiltyRepository.save(yarnBuilty);
        log.info(createExitLog(logPrefix, createNameValue("id", saved.getId())));
        return saved;
    }

    @Override
    public YarnBuilty findById(Long id) {
        String logPrefix = "findById()";
        log.info(createEntryLog(logPrefix));
        YarnBuilty yarnBuilty = yarnBuiltyRepository.findById(id).orElse(null);
        log.info(createExitLog(logPrefix, createNameValue("id", id)));
        return yarnBuilty;
    }

    @Override
    public List<YarnBuilty> findAll() {
        String logPrefix = "findAll()";
        log.info(createEntryLog(logPrefix));
        List<YarnBuilty> all = yarnBuiltyRepository.findAll();
        log.info(createExitLog(logPrefix, createNameValue("count", all.size())));
        return all;
    }
}
