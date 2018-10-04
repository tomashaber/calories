package com.toptal.calories.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Created by Tomas Haber on 6.7.2015
 */

@Configuration
@EnableJpaRepositories("com.toptal.calories.repository")
public class PersistenceConfiguration extends JpaRepositoryConfigExtension {
    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }



}
