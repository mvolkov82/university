package com.foxminded.university.repository;

import com.foxminded.university.model.Faculty;
import com.foxminded.university.repository.custom.CustomizedFacultyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends CustomizedFacultyRepository, JpaRepository<Faculty, Long> {
}
