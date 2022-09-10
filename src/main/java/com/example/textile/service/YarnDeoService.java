package com.example.textile.service;

import com.example.textile.entity.YarnDeo;

import java.util.List;

public interface YarnDeoService {

    YarnDeo saveOrUpdate(YarnDeo yarnDeo);

    List<YarnDeo> findAll();

    List<YarnDeo> findByPartyName(String partyName);
}
