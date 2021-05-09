package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.dto.FacultyDTO;

public interface FacultyService extends Service<Faculty> {

    List<FacultyDTO> getAllFacultyDTO();

    List<FacultyDTO> getAllNotDeletedFacultyDTO();

    FacultyDTO getByIdFacultyDTO(Long id);

    void update(FacultyDTO facultyDTO);

    void create(FacultyDTO facultyDTO);

    void markDeleted(Long id, boolean deleted);
}
