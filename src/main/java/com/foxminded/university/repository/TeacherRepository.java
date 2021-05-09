package com.foxminded.university.repository;

import com.foxminded.university.model.person.Teacher;
import com.foxminded.university.repository.custom.CustomizedTeacherRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends CustomizedTeacherRepository, JpaRepository<Teacher, Long> {

}
