package com.toptal.calories.api;

import com.toptal.calories.api.exception.PasswordInvalidException;
import com.toptal.calories.api.exception.ResourceNotFoundException;
import com.toptal.calories.api.exception.UsernameNotUniqueException;
import com.toptal.calories.dto.RegisterUserDto;
import com.toptal.calories.dto.UpdateUserDto;
import com.toptal.calories.entity.User;
import com.toptal.calories.service.model.UserService;
import com.toptal.calories.service.security.CaloriesUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by Tomas Haber on 4.7.2015
 */

@RestController
@RequestMapping("/api/user")
public class UserController extends CaloriesController {

    @Autowired
    private UserService service;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User save(@RequestBody RegisterUserDto userDto) {
        Optional<User> user = service.findByUsername(userDto.getUsername());
        if (user.isPresent()) {
            throw new UsernameNotUniqueException();
        }
        return service.register(userDto);
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public User save(@RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword) {
        boolean passwordValid = passwordEncoder.matches(oldPassword,currentUser().getPassword());

        if (passwordValid) {
            User user = service.findByUsername(currentUser().getUsername()).get();
            user.setPassword(passwordEncoder.encode(newPassword));
            return service.update(user);
        } else {
            throw new PasswordInvalidException();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public User update(@RequestBody UpdateUserDto user) {
        user.setUsername(currentUser().getUsername());
        user.setId(currentUser().getId());
        User updated = service.update(user);
        updatePrincipal(updated);
        return updated;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public User get() {
        Optional<User> user = service.findByUsername(currentUser().getUsername());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return user.get();
    }

    private void updatePrincipal(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(new CaloriesUserDetails(user), user.getPassword(),
                Collections.<GrantedAuthority>singletonList(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
