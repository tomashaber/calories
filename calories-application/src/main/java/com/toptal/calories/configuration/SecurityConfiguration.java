package com.toptal.calories.configuration;

import com.toptal.calories.entity.Meal;
import com.toptal.calories.service.model.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Tomas Haber on 4.7.2015
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Bean
    public PermissionEvaluator permissionEvaluator(MealService mealService) {
        return new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                if ("Meal".equals(permission)) {
                    Optional<Meal> optional = mealService.findById(Long.parseLong(targetDomainObject.toString()));
                    if (!optional.isPresent()) {
                        return true;
                    } else {
                        return optional.get().getUser().getUsername().equals(((UserDetails)authentication.getPrincipal()).getUsername());
                    }
                }
                throw new IllegalArgumentException("This evaluator evaluates only Meal permissions");
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                throw new IllegalArgumentException("This evaluator evaluates only Meal permissions");
            }
        };
    }


    @Bean
    @Autowired
    public DefaultWebSecurityExpressionHandler expressionHandler(PermissionEvaluator permissionEvaluator) {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

}
