package com.toptal.calories.repository;

import com.toptal.calories.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Tomas Haber on 6.7.2015
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String login);
}
