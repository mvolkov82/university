package com.foxminded.university.service.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import com.foxminded.university.service.validator.MaxStudentInGroupValidator;

@Constraint(validatedBy = MaxStudentInGroupValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxStudentsInGroup {
    String message() default "Limit of students: Group is full!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
