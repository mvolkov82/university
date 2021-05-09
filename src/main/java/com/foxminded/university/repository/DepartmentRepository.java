package com.foxminded.university.repository;

import com.foxminded.university.model.Department;
import com.foxminded.university.repository.custom.CustomizedDepartmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends CustomizedDepartmentRepository, JpaRepository<Department, Long> {
}
