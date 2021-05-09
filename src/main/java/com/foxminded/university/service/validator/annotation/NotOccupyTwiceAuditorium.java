package com.foxminded.university.service.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import com.foxminded.university.service.validator.NotOccupyTwiceAuditoriumValidator;

@Constraint(validatedBy = NotOccupyTwiceAuditoriumValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotOccupyTwiceAuditorium {

    String message() default "The auditorium is already occupied at this time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
