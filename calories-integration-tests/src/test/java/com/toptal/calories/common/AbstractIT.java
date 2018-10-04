package com.toptal.calories.common;

import com.toptal.calories.dto.RegisterUserDto;
import com.toptal.calories.entity.User;
import com.toptal.calories.repository.MealRepository;
import com.toptal.calories.repository.UserRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by Tomas Haber on 8.7.2015
 */
public abstract class AbstractIT {

    protected StatefullRestTemplate restTemplate = new StatefullRestTemplate();

    protected String absURI(String relative) {
        return "http://localhost:8080" + relative;
    }

    protected void login(String username, String password) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap();
            map.add("username", username);
            map.add("password", password);
            restTemplate.post(absURI("/auth/login"), map, String.class);
    }

    protected User register(String username, String password) {
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setUsername(username);
        registerDto.setPassword(password);
        return restTemplate.post(absURI("/api/user/register"), registerDto, User.class);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MealRepository mealRepository;

    @Before
    public void purge(){
        mealRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
}
