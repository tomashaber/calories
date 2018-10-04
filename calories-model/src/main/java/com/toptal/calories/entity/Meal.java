package com.toptal.calories.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toptal.calories.persistence.LocalDateTimePersistenceConverter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by Tomas Haber on 6.7.2015
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = "user")
public class Meal extends DomainEntity {

    @NotNull
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime time;
    @Size(max = 512)
    @NotNull
    private String name;
    @Min(1)
    private int calories;

    @ManyToOne
    @NotNull
    @Valid
    private User user;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "time=" + time +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", user=" + user +
                '}';
    }
}
