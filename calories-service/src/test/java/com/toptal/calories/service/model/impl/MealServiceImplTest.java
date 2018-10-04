package com.toptal.calories.service.model.impl;

import com.mysema.query.types.Predicate;
import com.toptal.calories.entity.Meal;
import com.toptal.calories.entity.User;
import com.toptal.calories.repository.MealRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * Created by Tomas Haber on 9.7.2015
 */
public class MealServiceImplTest {

    @Test
    public void testFindById() throws Exception {
        MealRepository repository = Mockito.mock(MealRepository.class);
        MealServiceImpl mealService = new MealServiceImpl(repository);
        mealService.findById(1l);
        verify(repository,times(1)).findOne(1l);
    }

    @Test
    public void testFind() throws Exception {
        MealRepository repository = Mockito.mock(MealRepository.class);
        when(repository.findAll(Mockito.any(Predicate.class))).thenReturn(Collections.emptyList());
        MealServiceImpl mealService = new MealServiceImpl(repository);
        mealService.find(new User(), LocalDate.now().minusDays(1), LocalDate.now(), LocalTime.now().minusMinutes(5), LocalTime.now());
        verify(repository, times(1)).findAll(Mockito.any(Predicate.class));
    }

    @Test
    public void testSave() throws Exception {
        MealRepository repository = Mockito.mock(MealRepository.class);
        MealServiceImpl mealService = new MealServiceImpl(repository);
        Meal meal = new Meal();
        mealService.save(meal);
        verify(repository, times(1)).save(meal);
    }

    @Test
    public void testUpdate() throws Exception {
        MealRepository repository = Mockito.mock(MealRepository.class);
        MealServiceImpl mealService = new MealServiceImpl(repository);
        Meal meal = new Meal();
        meal.setId(1l);
        meal.setVersion(1);
        try {
            mealService.update(meal);
            Assert.fail();
        } catch (ObjectOptimisticLockingFailureException e) {
        }

        verify(repository, times(1)).findOne(1l);
    }

    @Test
    public void testDelete() throws Exception {
        Meal meal = new Meal();
        MealRepository repository = Mockito.mock(MealRepository.class);
        MealServiceImpl mealService = new MealServiceImpl(repository);
        mealService.delete(meal);
        verify(repository, times(1)).delete(meal);
    }

    @Test
    public void testAll() throws Exception {
        MealRepository repository = Mockito.mock(MealRepository.class);
        MealServiceImpl mealService = new MealServiceImpl(repository);
        User user = new User();
        mealService.findByUser(user);
        verify(repository, times(1)).findByUser(user);
    }
}