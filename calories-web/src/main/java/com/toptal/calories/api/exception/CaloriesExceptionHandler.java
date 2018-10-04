package com.toptal.calories.api.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tomas Haber on 12.7.2015
 */
@ControllerAdvice
public class CaloriesExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private EntityManager em;

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<Object> optimisticLockException(HttpServletRequest req, Exception exception) {
        ObjectOptimisticLockingFailureException e = (ObjectOptimisticLockingFailureException) exception;
        Object entity = em.find(classForName(e.getPersistentClassName()), e.getIdentifier());
        return new ResponseEntity<Object>(entity, HttpStatus.CONFLICT);
    }

    private Class classForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
