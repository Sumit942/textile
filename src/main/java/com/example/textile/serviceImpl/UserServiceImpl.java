package com.example.textile.serviceImpl;

import com.example.textile.entity.User;
import com.example.textile.repo.UserRepository;
import com.example.textile.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User findByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public void deleteByUserName(String userName) {
        userRepo.deleteByUserName(userName);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User saveOrUpdate(User user) {
        if (user.getId() == null)
            return save(user);

        return  userRepo.findById(user.getId())
                .map(saveUser -> {
                    log.info("user found by id-"+user.getId());
                    saveUser.setUserName(user.getUserName());
                    if (!passwordEncoder.matches(user.getPassword(),
                            passwordEncoder.encode(saveUser.getPassword()))) {
                        saveUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    saveUser.setUserProfiles(user.getUserProfiles());
                    return userRepo.save(saveUser);
                }).orElseGet(() -> {
                    log.info("user not found by id-"+user.getId());
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepo.save(user);
                });
    }

    @Override
    public boolean userExists(String username) {
        return this.userRepo.findByUserName(username) != null;
    }
}
