package com.example.textile.service;

import com.example.textile.entity.User;

public interface UserService {

    User findByUserName(String userName);

    void delete(User user);

    void deleteByUserName(String userName);

    User save(User user);

    User saveOrUpdate(User user);

    boolean userExists(String username);
}
