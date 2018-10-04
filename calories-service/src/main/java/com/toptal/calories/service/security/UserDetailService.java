package com.toptal.calories.service.security;

import com.toptal.calories.entity.User;
import com.toptal.calories.service.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Tomas Haber on 8.7.2015
 */
@Service
@Primary
public class UserDetailService implements UserDetailsService {

    private final UserService service;

    @Autowired
    public UserDetailService(UserService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> optional = service.findByUsername(login);
        return optional.map(user ->
                        new CaloriesUserDetails(user)
        ).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    public UserService getService() {
        return service;
    }
}
