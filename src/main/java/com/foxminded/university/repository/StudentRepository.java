package com.foxminded.university.repository;

import com.foxminded.university.model.person.Student;
import com.foxminded.university.repository.custom.CustomizedStudentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CustomizedStudentRepository, JpaRepository<Student, Long> {
    boolean existsStudentByIdAndGroupId(Long id, Long groupId);
    int countByGroupId(Long id);
}
