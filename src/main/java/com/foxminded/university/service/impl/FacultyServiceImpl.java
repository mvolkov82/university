package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Faculty_;
import com.foxminded.university.model.dto.FacultyDTO;
import com.foxminded.university.repository.FacultyRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.FacultyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FacultyServiceImpl implements FacultyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyServiceImpl.class);

    private FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    @Transactional
    public Faculty create(Faculty faculty) {
        LOGGER.debug("create() [faculty={}])", faculty);
        try {
            facultyRepository.save(faculty);
        } catch (DataAccessException e) {
            String message = format("Operation for faculty %s failed.", faculty);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return faculty;
    }

    @Transactional
    public void create(FacultyDTO facultyDTO) {
        Faculty faculty = convertFromDto(facultyDTO);
        create(faculty);
    }

    @Override
    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    @Override
    public List<FacultyDTO> getAllFacultyDTO() {
        Sort deletedSort = Sort.by(Faculty_.DELETED);
        Sort nameSort = Sort.by(Faculty_.NAME);
        Sort finalSort = deletedSort.ascending().and(nameSort).ascending();
        List<Faculty> faculties = facultyRepository.findAll(finalSort);
        return convertToDTO(faculties);
    }

    @Override
    public List<FacultyDTO> getAllNotDeletedFacultyDTO() {
        return facultyRepository.getAllNotDeleted();
    }

    @Override
    public Faculty getById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Faculty with id '%s' not found", id)));
    }

    public FacultyDTO getByIdFacultyDTO(Long id) {
        Faculty faculty = getById(id);
        return convertToDTO(faculty);
    }

    @Transactional
    @Override
    public void markDeleted(Long id, boolean deleted) {
        facultyRepository.markDeleted(id, deleted);
    }

    @Override
    @Transactional
    public void update(Faculty faculty) {
        try {
            facultyRepository.save(faculty);
        } catch (DataAccessException e) {
            String message = format("Operation for faculty %s failed.", faculty);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Override
    public void update(FacultyDTO facultyDTO) {
        Faculty faculty = convertFromDto(facultyDTO);
        update(faculty);
    }

    private Faculty convertFromDto(FacultyDTO facultyDTO) {
        Faculty faculty = new Faculty();
        faculty.setId(facultyDTO.getId());
        faculty.setName(facultyDTO.getName());
        faculty.setDeleted(facultyDTO.isDeleted());
        return faculty;
    }

    private FacultyDTO convertToDTO(Faculty faculty) {
        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setId(faculty.getId());
        facultyDTO.setName(faculty.getName());
        facultyDTO.setDeleted(faculty.isDeleted());
        return facultyDTO;
    }

    private List<FacultyDTO> convertToDTO(List<Faculty> faculties) {
        return faculties.stream().map(this::convertToDTO).collect(toList());
    }
}
