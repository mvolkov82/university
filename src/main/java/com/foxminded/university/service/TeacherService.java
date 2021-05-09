package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.model.dto.TeacherDTO;
import com.foxminded.university.model.person.Teacher;

public interface TeacherService extends Service<Teacher> {
    List<TeacherDTO> getAllTeachersDTO();

    List<TeacherDTO> getAllNotDeletedDTOS();

    TeacherDTO getByIdTeacherDTO(Long id);

    void update(TeacherDTO teacherDTO);

    void create(TeacherDTO teacherDTO);

    void markDeleted(Long id, boolean deleted);

    void deleteSubjectFromTeacher(SubjectDTO subjectDTO, TeacherDTO teacherDTO);

    void addSubjectToTeacher(SubjectDTO subjectDTO, TeacherDTO teacherDTO);
}
