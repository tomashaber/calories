package com.toptal.calories.service.model.impl;

import com.toptal.calories.dto.RegisterUserDto;
import com.toptal.calories.entity.User;
import com.toptal.calories.repository.UserRepository;
import com.toptal.calories.service.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Tomas Haber on 6.7.2015
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User update(User user) {
        return repository.save(user.toUser());
    }

    @Override
    public User register(RegisterUserDto userDto) {
        //hash password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return repository.save(userDto.toUser());
    }

    @Override
    public Optional<User> findByUsername(String login) {
        return Optional.ofNullable(repository.findByUsername(login));
    }
}
