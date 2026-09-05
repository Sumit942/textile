package com.example.textile.serviceimpl;

import com.example.textile.entity.AuditLog;
import com.example.textile.repo.AuditLogRepository;
import com.example.textile.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public void save(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}
