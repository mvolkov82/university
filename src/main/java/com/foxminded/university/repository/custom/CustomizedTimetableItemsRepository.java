package com.foxminded.university.repository.custom;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.Auditorium;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.TimetableItem;
import com.foxminded.university.model.person.Teacher;

public interface CustomizedTimetableItemsRepository extends CustomizedRepository<TimetableItem> {
    boolean isTeacherBusy(LocalDate date, Lecture lecture, Teacher teacher);

    boolean isAuditoriumOccupied(LocalDate date, Lecture lecture, Auditorium auditorium);

    List<TimetableItem> getTimetableItemsByDateAndLecture(LocalDate date, Lecture lecture);

    List<TimetableItem> getAllNotDeletedForTeacher(LocalDate dateFrom, LocalDate dateTo, Teacher teacher);

    List<TimetableItem> getAllNotDeletedForGroup(LocalDate from, LocalDate to, Group group);

    List<TimetableItem> getAllNotDeletedByDateAndGroupAndTeacher(LocalDate dateFrom, LocalDate dateTo, Group group, Teacher teacher);
}
