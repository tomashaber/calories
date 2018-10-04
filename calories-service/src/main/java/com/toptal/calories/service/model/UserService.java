package com.toptal.calories.service.model;

import com.toptal.calories.dto.RegisterUserDto;
import com.toptal.calories.entity.User;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Tomas Haber on 6.7.2015
 */
public interface UserService  {
    @Transactional
    User update(User user);

    @Transactional
    User register(RegisterUserDto userDto);

    Optional<User> findByUsername(String login);

}
