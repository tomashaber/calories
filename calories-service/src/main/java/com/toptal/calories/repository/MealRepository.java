package com.toptal.calories.repository;

import com.toptal.calories.entity.Meal;
import com.toptal.calories.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tomas Haber on 6.7.2015
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long>, QueryDslPredicateExecutor {
    List<Meal> findByUser(User user);
}
