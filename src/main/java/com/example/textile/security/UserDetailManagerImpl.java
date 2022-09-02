package com.example.textile.security;

import com.example.textile.entity.User;
import com.example.textile.entity.UserProfile;
import com.example.textile.serviceImpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailManagerImpl implements UserDetailsManager {

    public UserDetailManagerImpl() {
        log.info(">>>textile UserDetailManagerImpl initialized");
    }

    @Autowired
    UserServiceImpl userService;

    @Override
    @Transactional  //for lazy initialization of userProfiles
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUserName(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        log.info("User: {}",user);

        return new org.springframework.security.core.userdetails
                .User(user.getUserName(),user.getPassword(),getGrantedAuthorities(user));
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        return user.getUserProfiles().stream()
                .map(userProfile -> new SimpleGrantedAuthority("ROLE_"+userProfile.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public void createUser(UserDetails userDetail) {
        User user = new User();
        user.setUserName(userDetail.getUsername());
        user.setPassword(userDetail.getPassword());
        Set<UserProfile> userProfiles = userDetail.getAuthorities().stream().map(e -> {
            UserProfile userProfile = new UserProfile();
            userProfile.setType(e.getAuthority());
            return userProfile;
        }).collect(Collectors.toSet());
        user.setUserProfiles(userProfiles);
        this.userService.saveOrUpdate(user);
    }

    @Override
    public void updateUser(UserDetails userDetail) {
        User user = userService.findByUserName(userDetail.getUsername());
        if (user == null)
            throw new UsernameNotFoundException(userDetail.getUsername());
        user.setUserName(userDetail.getUsername());
        user.setPassword(userDetail.getPassword());
        Set<UserProfile> userProfiles = userDetail.getAuthorities().stream().map(e -> {
            UserProfile userProfile = new UserProfile();
            userProfile.setType(e.getAuthority());
            return userProfile;
        }).collect(Collectors.toSet());
        user.setUserProfiles(userProfiles);
        userService.saveOrUpdate(user);
    }

    @Override
    public void deleteUser(String username) {
        userService.deleteByUserName(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userService.userExists(username);
    }
}
