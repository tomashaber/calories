package com.toptal.calories;

import com.toptal.calories.common.AbstractIT;
import com.toptal.calories.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Tomas Haber on 7.7.2015
 */
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CaloriesApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class UserIT extends AbstractIT {

    @Test
    public void test() {
        String username = "tomas.haber@gmail.com";
        String password = "secret";

        //register new user
        User user = register(username, password);
        assertNotNull(user.getId());
        assertNotNull(user.getUsername(), username);

        //login as the new user
        login(username, password);

        //update expected calories with a random number
        int expectedCalories = new Random().nextInt(100);
        user.setExpectedCalories(expectedCalories);
        user = restTemplate.post(absURI("/api/user/me"), user, User.class);

        assertNotNull(user.getId());
        assertEquals(user.getExpectedCalories(), expectedCalories);

        //check getting user info
        user = restTemplate.get(absURI("/api/user/me"), User.class);
        String oldPassword = user.getPassword();
        assertNotNull(oldPassword);

        //change password
        MultiValueMap<String, String> formData = new LinkedMultiValueMap();
        formData.add("oldPassword", password);
        formData.add("newPassword", "salt");
        user = restTemplate.post(absURI("/api/user/password"), formData,User.class);

        assertNotEquals(oldPassword, user.getPassword());
        assertNotNull(user.getId());

        //logout
        restTemplate.get(absURI("/auth/logout"), String.class);

        //should throw 403 Forbidden
        try {
            restTemplate.get(absURI("/api/user/me"), User.class);
            fail();
        } catch (Exception e) {}
    }

}
