package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.model.person.Student;

public interface StudentService extends Service<Student> {
    List<StudentDTO> getAllStudentsDTO();

    StudentDTO getByIdStudentDTO(Long id);

    void update(StudentDTO student);

    void create(StudentDTO studentDTO);

    void markDeleted(Long id, boolean deleted);

    int countByGroupId(Long id);

    boolean existsStudentByIdAndGroupId(Long id, Long groupId);
}
