package com.toptal.calories.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Created by Tomas Haber on 6.7.2015
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends DomainEntity {

    @Size(min = 5, max = 255)
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    @Size(min = 5, max = 255)
    private String password;
    private int expectedCalories;

    public User() {

    }

    public User(Long id, int version, String username, String password, int expectedCalories) {
        this.setId(id);
        this.setVersion(version);
        this.username = username;
        this.password = password;
        this.expectedCalories = expectedCalories;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getExpectedCalories() {
        return expectedCalories;
    }

    public void setExpectedCalories(int expectedCalories) {
        this.expectedCalories = expectedCalories;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + username + '\'' +
                ", password='" + password + '\'' +
                ", expectedCalories=" + expectedCalories +
                '}';
    }

    public User toUser() {
        return new User(getId(), getVersion(), getUsername(), getPassword(), getExpectedCalories());
    }
}
