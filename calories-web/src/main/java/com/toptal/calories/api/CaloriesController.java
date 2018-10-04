package com.toptal.calories.api;

import com.toptal.calories.service.security.CaloriesUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Tomas Haber on 6.7.2015
 */

public abstract class CaloriesController {
    protected CaloriesUserDetails currentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (CaloriesUserDetails)auth.getPrincipal();
    }

}
