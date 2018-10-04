package com.toptal.calories.service.model;

import com.toptal.calories.entity.Meal;
import com.toptal.calories.entity.User;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tomas Haber on 6.7.2015
 */
public interface MealService {

    Optional<Meal> findById(Long mealId);

    List<Meal> find(User user, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo);

    @Transactional
    Meal save(Meal meal);
    @Transactional
    void delete(Meal meal);

    List<Meal> findByUser(User user);

    Meal update(Meal meal);
}
