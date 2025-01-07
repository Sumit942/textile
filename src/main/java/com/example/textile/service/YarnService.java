package com.example.textile.service;

import com.example.textile.dto.YarnDto;

import java.util.List;

public interface YarnService {

    List<YarnDto> findAll();

    YarnDto findById(Long id);

    YarnDto save(YarnDto yarn);

    YarnDto updateYarn(Long id, YarnDto yarnDto);

    void deleteYarn(Long id);

    List<YarnDto> findByType(String type);

    List<YarnDto> findByTypeAndCompanyName(String type, String companyName);

    boolean existByYarnTypeAndCompanyNameIgnoreCase(String type, String companyName);
}
