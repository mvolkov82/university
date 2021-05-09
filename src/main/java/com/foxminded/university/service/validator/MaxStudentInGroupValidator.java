package com.foxminded.university.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.service.validator.annotation.MaxStudentsInGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MaxStudentInGroupValidator implements ConstraintValidator<MaxStudentsInGroup, StudentDTO> {

    private static final String GROUP_NAME = "groupName";

    @Autowired
    private StudentService studentService;

    @Value("${validation.maxStudentsInGroup}")
    private int limit;

    private String message;

    @Override
    public void initialize(MaxStudentsInGroup constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(StudentDTO studentDTO, ConstraintValidatorContext context) {
        boolean result = validate(studentDTO);
        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(GROUP_NAME)
                    .addConstraintViolation();
        }
        return result;
    }

    private boolean validate(StudentDTO studentDTO) {
        Long groupId = studentDTO.getGroupId();
        Long studentId = studentDTO.getId();
        int studentsInGroup = studentService.countByGroupId(groupId);
        boolean result = true;
        if (studentsInGroup >= limit) {
            boolean isStudentInGroup = studentService.existsStudentByIdAndGroupId(studentId, groupId);
            if (!isStudentInGroup) {
                result = false;
            }
        }
        return result;
    }
}
