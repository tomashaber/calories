package com.toptal.calories.api;

import com.toptal.calories.api.exception.ResourceNotFoundException;
import com.toptal.calories.entity.Meal;
import com.toptal.calories.service.model.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tomas Haber on 4.7.2015
 */

@RestController
@RequestMapping("/api/meal")
@PreAuthorize("hasRole('ROLE_USER')")
public class MealController extends CaloriesController {

    @Autowired
    private MealService service;

    @RequestMapping(method = RequestMethod.POST)
    public Meal save(@RequestBody Meal meal) {
        meal.setUser(currentUser().getUser());
        return service.save(meal);
    }

    @PreAuthorize("hasPermission(#mealId, 'Meal')")
    @RequestMapping(value = "/{mealId}", method = RequestMethod.POST)
    public Meal update(@RequestBody Meal meal, @PathVariable("mealId") Long mealId ) {
        meal.setUser(currentUser().getUser());
        return service.update(meal);
    }

    @PreAuthorize("hasPermission(#mealId, 'Meal')")
    @RequestMapping(value = "/{mealId}/{version}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "mealId") Long mealId, @PathVariable(value = "version") int version) {
        Meal meal = new Meal();
        meal.setId(mealId);
        meal.setVersion(version);
        service.delete(meal);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Meal> all() {
        return service.findByUser(currentUser().getUser());
    }

    @PreAuthorize("hasPermission(#mealId, 'Meal')")
    @RequestMapping(value = "/{mealId}", method = RequestMethod.GET)
    public Meal get(@PathVariable("mealId") Long mealId) {
        Optional<Meal> meal = service.findById(mealId);
        if (!meal.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return meal.get();
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<Meal> find(@RequestParam(value = "date-from", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate dateFrom,
                           @RequestParam(value = "date-to", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate dateTo,
                           @RequestParam(value = "time-from", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime timeFrom,
                           @RequestParam(value = "time-to", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime timeTo) {
        return service.find(currentUser().getUser(), dateFrom, dateTo, timeFrom, timeTo);
    }
}
