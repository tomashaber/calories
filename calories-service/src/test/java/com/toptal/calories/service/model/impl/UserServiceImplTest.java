package com.toptal.calories.service.model.impl;

import com.toptal.calories.dto.RegisterUserDto;
import com.toptal.calories.dto.UpdateUserDto;
import com.toptal.calories.entity.User;
import com.toptal.calories.repository.UserRepository;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

/**
 * Created by Tomas Haber on 9.7.2015
 */
public class UserServiceImplTest {

    @Test
    public void testUpdate() throws Exception {
        UserRepository repository = Mockito.mock(UserRepository.class);
        String username = "tomas.haber@gmail.com";
        String password = "secret";
        UserServiceImpl userService = new UserServiceImpl(repository, null);
        UpdateUserDto userDto = new UpdateUserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userService.update(userDto);
        verify(repository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testRegister() throws Exception {
        UserRepository repository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        String username = "tomas.haber@gmail.com";
        String password = "secret";
        when(passwordEncoder.encode(password)).thenReturn("encoded");
        UserServiceImpl userService = new UserServiceImpl(repository, passwordEncoder);
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userService.register(userDto);
        verify(passwordEncoder, times(1)).encode(password);
        verify(repository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testFindByUsername() throws Exception {
        UserRepository repository = Mockito.mock(UserRepository.class);
        String username = "tomas.haber@gmail.com";
        UserServiceImpl userService = new UserServiceImpl(repository, null);
        userService.findByUsername(username);
        verify(repository, times(1)).findByUsername(username);
    }
}