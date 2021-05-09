package com.foxminded.university.repository;

import com.foxminded.university.model.Subject;
import com.foxminded.university.repository.custom.CustomizedSubjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CustomizedSubjectRepository, JpaRepository<Subject, Long> {
}
