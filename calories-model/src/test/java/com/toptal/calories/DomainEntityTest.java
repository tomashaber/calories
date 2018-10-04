package com.toptal.calories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.toptal.calories.entity.Meal;
import com.toptal.calories.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Tomas Haber on 6.7.2015
 */
public class DomainEntityTest {
    private ObjectReader reader;
    private ObjectWriter writer;
    private ObjectMapper mapper;

    @Before
    public void before(){

        mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        writer = mapper.writer();
    }

    @Test
    public void testMeal() throws IOException {
        Meal meal = new Meal();
        meal.setCalories(5);
        meal.setName("12345");
        meal.setTime(LocalDateTime.now());

        reader = mapper.reader().forType(Meal.class);
        assertNotNull(reader.readValue(writer.writeValueAsString(meal)));
    }

    @Test
    public void testUser() throws IOException {
        User user = new User();
        user.setExpectedCalories(123);
        user.setUsername("1234");
        user.setPassword("%12312%");
        reader = mapper.reader().forType(User.class);
        assertNotNull(reader.readValue(writer.writeValueAsString(user)));
    }
}