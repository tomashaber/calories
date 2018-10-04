package com.toptal.calories;

import com.toptal.calories.configuration.PersistenceConfiguration;
import com.toptal.calories.configuration.SecurityConfiguration;
import com.toptal.calories.configuration.WebSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Created by Tomas Haber on 4.7.2015
 */
@EnableAutoConfiguration
@Import({PersistenceConfiguration.class, WebSecurityConfiguration.class, SecurityConfiguration.class})
@ComponentScan("com.toptal.calories.service")
public class CaloriesApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CaloriesApplication.class, args);
    }
}
