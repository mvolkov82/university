package com.foxminded.university.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.foxminded.university.model.TimetableItem;
import com.foxminded.university.model.dto.TimetableItemDTO;
import com.foxminded.university.service.TimetableItemService;
import com.foxminded.university.service.validator.annotation.NotBusyTwiceTeacher;
import org.springframework.beans.factory.annotation.Autowired;

public class NotBusyTwiceTeacherValidator implements ConstraintValidator<NotBusyTwiceTeacher, TimetableItemDTO> {

    private static final String WEB_LINK_FOR_MESSAGE = "teacherId";

    private String message;

    @Autowired
    private TimetableItemService timeTableItemService;

    @Override
    public void initialize(NotBusyTwiceTeacher constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(TimetableItemDTO timeTableItemDTO, ConstraintValidatorContext context) {
        boolean result = validate(timeTableItemDTO);
        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(WEB_LINK_FOR_MESSAGE)
                    .addConstraintViolation();
        }
        return result;
    }

    private boolean validate(TimetableItemDTO dto) {
        boolean isTeacherBusy = timeTableItemService.isTeacherBusy(dto.getDate(), dto.getLectureId(), dto.getTeacherId());
        if (dto.getId() != null && valuesFromDbAndDtoAreEquals(dto)) {
            isTeacherBusy = false;
        }
        return !isTeacherBusy;
    }

    private boolean valuesFromDbAndDtoAreEquals(TimetableItemDTO dto) {
        TimetableItem itemFromDB = timeTableItemService.getById(dto.getId());

        if (dto.getTeacherId() != itemFromDB.getTeacher().getId()) {
            return false;
        }

        if (dto.getLectureId() != itemFromDB.getLecture().getId()) {
            return false;
        }

        if (!dto.getDate().equals(itemFromDB.getDate())) {
            return false;
        }
        return true;
    }
}
