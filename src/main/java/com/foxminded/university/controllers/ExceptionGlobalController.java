package com.foxminded.university.controllers;

import com.foxminded.university.repository.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionGlobalController {
    public static final String EXCEPTION_MESSAGE = "Application error. Reason: %s!";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionGlobalController.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        LOGGER.error("{} -> {}", e.getClass().getSimpleName(), e.getMessage());
        String reasonMessage = "Unexpected error";

        if (e instanceof EntityNotFoundException) {
            reasonMessage = String.format(EXCEPTION_MESSAGE, "The entity not found!");
        }

        if (e instanceof MethodArgumentTypeMismatchException) {
            reasonMessage = String.format(EXCEPTION_MESSAGE, "Incorrect argument!");
        }

        model.addAttribute("errorMessage", reasonMessage);
        return "application-exception";
    }
}
