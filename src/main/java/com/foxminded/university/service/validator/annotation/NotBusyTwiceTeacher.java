package com.foxminded.university.service.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import com.foxminded.university.service.validator.NotBusyTwiceTeacherValidator;

@Constraint(validatedBy = NotBusyTwiceTeacherValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBusyTwiceTeacher {

    String message() default "The teacher is already busy at this time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
