package com.toptal.calories.service.model.impl;

import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.toptal.calories.entity.Meal;
import com.toptal.calories.entity.QMeal;
import com.toptal.calories.entity.User;
import com.toptal.calories.repository.MealRepository;
import com.toptal.calories.service.model.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tomas Haber on 6.7.2015
 */
@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Meal> findById(Long mealId) {
        return Optional.ofNullable(repository.findOne(mealId));
    }

    @Override
    public List<Meal> find(User user, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {

        if (timeFrom == null) {
            timeFrom = LocalTime.of(0, 0);
        }
        if (timeTo == null) {
            timeTo = LocalTime.of(23, 59, 59);
        }

        int minuteOfDayBottom = timeFrom.getHour() * 60 + timeFrom.getMinute();
        int minuteOfDayTop = timeTo.getHour() * 60 + timeTo.getMinute();
        BooleanExpression timeExpression = QMeal.meal.time.hour().multiply(60).add(QMeal.meal.time.minute()).between(minuteOfDayBottom, minuteOfDayTop);

        BooleanExpression query = QMeal.meal.user.eq(user).and(timeExpression);

        if (dateFrom != null) {
            LocalDateTime dateTimeFrom = LocalDateTime.of(dateFrom, LocalTime.of(0, 0, 0));
            query = query.and(QMeal.meal.time.goe(dateTimeFrom));
        }

        if (dateTo != null) {
            LocalDateTime dateTimeTo = LocalDateTime.of(dateTo, LocalTime.of(23, 59, 59));
            query = query.and(QMeal.meal.time.loe(dateTimeTo));
        }

        return Lists.newArrayList(repository.findAll(query).iterator());
    }

    @Override
    public Meal save(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(Meal meal) {
        repository.delete(meal);
    }

    @Override
    public List<Meal> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public Meal update(Meal meal) {
        Meal existing = repository.findOne(meal.getId());

        if (existing == null) {
            throw new ObjectOptimisticLockingFailureException(Meal.class.getName(),meal.getId());
        }

        return repository.save(meal);
    }
}
