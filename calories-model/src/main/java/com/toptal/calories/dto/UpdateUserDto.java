package com.toptal.calories.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toptal.calories.entity.User;

/**
 * Created by Tomas Haber on 8.7.2015
 */
@JsonIgnoreProperties({"id","login"})
public class UpdateUserDto extends User {
}
