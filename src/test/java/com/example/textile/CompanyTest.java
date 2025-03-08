package com.example.textile;

import com.example.textile.entity.Company;
import com.example.textile.repo.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
public class CompanyTest {

    @Autowired
    CompanyRepository companyRepo;

    @Test
    public void updateCompanyCode() {
        log.info("CompanyTest.updateCompanyCode Entry");
        List<Company> companyList = companyRepo.findAll();
        for (Company company : companyList) {
                String name = company.getName();
                StringBuilder sbCode = new StringBuilder();
                for (String s : name.split("\\s+")) {
                    int i = 0;
                    char c = s.charAt(i);
                    if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                        log.info("not valid letter: {}" , c);
                        continue;
                    }
                    sbCode.append(c);
                }
                String code = sbCode.toString().toUpperCase();
            Optional<Company> byCode = companyRepo.findByCode(code);
            if (byCode.isPresent()) {
                log.info("company code already exist: {} ", byCode.get());
                throw new RuntimeException("company code already exist:" + byCode.get().getName());
            }
            log.info("updating company [name={} with code={}]", name, code);
                company.setCode(code);
        }
        log.info("persisting All");
        companyRepo.saveAll(companyList);
        log.info("CompanyTest.updateCompanyCode Exit");
    }

    @Test
    public void checkDuplicateCodes() {
        List<Company> companyList = companyRepo.findAll();
        Set<String> codoSet = companyList.stream()
                .map(Company::getCode)
                .collect(Collectors.toSet());
        log.info("originalSize: {}, setCodeSize: {}", companyList.size(),codoSet.size());
    }
}
