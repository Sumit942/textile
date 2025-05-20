package com.example.textile.service;

import com.example.textile.dto.LedgerResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface LedgerService {

    LedgerResponseDto processBankStatement(MultipartFile file, LocalDate startDt, LocalDate endDt);
}
