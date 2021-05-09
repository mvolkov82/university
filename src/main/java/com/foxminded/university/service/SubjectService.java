package com.foxminded.university.service;

import java.util.List;
import java.util.Set;

import com.foxminded.university.model.Subject;
import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.model.dto.TeacherDTO;

public interface SubjectService extends Service<Subject> {

    List<SubjectDTO> getAllSubjectsDTO();

    List<SubjectDTO> getAllNotDeletedSubjectsDTO();

    List<SubjectDTO> convertToDto(List<Subject> subjects);

    List<Subject> convertFromDto(List<SubjectDTO> subjectsDTOS);

    SubjectDTO getByIdSubjectDTO(Long id);

    void update(SubjectDTO subjectDTO);

    void create(SubjectDTO subjectDTO);

    void markDeleted(Long id, boolean deleted);

    List<SubjectDTO> getNotTeachersSubjectDTOs(TeacherDTO teacherDTO);

    List<SubjectDTO> getDTOSAsList(Set<Subject> subjectSet);
}
