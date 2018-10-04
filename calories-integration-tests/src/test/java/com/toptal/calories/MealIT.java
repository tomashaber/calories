package com.toptal.calories;

import com.toptal.calories.common.AbstractIT;
import com.toptal.calories.entity.Meal;
import com.toptal.calories.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Tomas Haber on 6.7.2015
 */
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CaloriesApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class MealIT extends AbstractIT {

    @Test
    public void test() {

        //register
        String username = "tomas.haber@gmail.com";
        String password = "secret";

        User user = register(username, password);
        assertNotNull(user.getId());

        login(username, password);

        user = restTemplate.post(absURI("/api/user/me"), user, User.class);
        assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now();

        Meal meal = new Meal();
        meal.setCalories(5);
        meal.setName("text");
        meal.setTime(now);
        meal.setUser(user);

        //save
        meal = restTemplate.post(absURI("/api/meal"), meal, Meal.class);
        Long id = meal.getId();
        assertNotNull(meal.getId());
        assertEquals(meal.getCalories(), 5);

        //get
        Meal retrievedMeal = restTemplate.get(absURI("/api/meal/" + id), Meal.class);
        assertNotNull(retrievedMeal.getId());
        assertEquals(meal.getCalories(), 5);

        //update
        retrievedMeal.setName("text+1");
        retrievedMeal = restTemplate.post(absURI("/api/meal/"+retrievedMeal.getId()), retrievedMeal, Meal.class);
        assertNotNull(retrievedMeal.getId());
        assertEquals("text+1", retrievedMeal.getName());

        //all
        assertEquals(restTemplate.get(absURI("/api/meal/all"), List.class).size(), 1);

        //find without params should find everything
        assertEquals(restTemplate.get(absURI("/api/meal/find"), List.class).size(), 1);

        //should find nothing
        LocalDateTime tomorrow = now.plusDays(1);
        assertEquals(restTemplate.get(absURI("/api/meal/find?date-from=" + fd(tomorrow)), List.class).size(),0);

        //find all for today - should be one
        assertEquals(restTemplate.get(absURI("/api/meal/find?date-from=" + fd(now) + "&date-to=" + fd(now)), List.class).size(), 1);

        //find all for today from the last 5 minutes
        assertEquals(restTemplate.get(absURI("/api/meal/find?date-from=" + fd(now) + "&date-to=" + fd(now)) + "&time-from=" + ft(now.minusMinutes(5)) + "&time-to=" + ft(now), List.class).size(), 1);

        //delete
        restTemplate.delete(absURI("/api/meal/"+id+"/"+retrievedMeal.getVersion()), null, String.class);

        //check if deleted
        assertEquals(restTemplate.get(absURI("/api/meal/all"), List.class).size(), 0);
    }

    private String fd(LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(dateTime);
    }

    private String ft(LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("HH:mm").format(dateTime);
    }
}
