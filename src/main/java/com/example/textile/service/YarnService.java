package com.example.textile.service;

import com.example.textile.dto.YarnDto;
import com.example.textile.entity.Yarn;

import java.util.List;

public interface YarnService {

    List<YarnDto> findAll();

    YarnDto findById(Long id);

    YarnDto save(Yarn yarn);

    YarnDto updateYarn(Long id, YarnDto yarnDto);

    void deleteYarn(Long id);
}
