package com.toptal.calories.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tomas Haber on 8.7.2015
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UsernameNotUniqueException extends RuntimeException {
}
