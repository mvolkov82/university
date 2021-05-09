package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Department;
import com.foxminded.university.model.dto.DepartmentDTO;

public interface DepartmentService extends Service<Department> {

    DepartmentDTO getByIdDepartmentDTO(Long id);

    List<DepartmentDTO> getAllDepartmentsDTOs();

    List<DepartmentDTO> getAllNotDeletedDepartmentDTO();

    void update(DepartmentDTO departmentDTO);

    void create(DepartmentDTO departmentDTO);

    void markDeleted(Long id, boolean deleted);
}
