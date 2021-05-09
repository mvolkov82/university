package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Department_;
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.dto.DepartmentDTO;
import com.foxminded.university.repository.DepartmentRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public Department create(Department department) {
        LOGGER.debug("create() [department={}])", department);
        try {
            departmentRepository.save(department);
        } catch (DataAccessException e) {
            String message = format("Operation for department %s failed.", department);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return department;
    }

    @Override
    public void create(DepartmentDTO departmentDTO) {
        Department department = convertFromDto(departmentDTO);
        create(department);

    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public List<DepartmentDTO> getAllDepartmentsDTOs() {
        Sort deletedSort = Sort.by(Department_.DELETED);
        Sort nameSort = Sort.by(Department_.NAME);
        Sort sort = deletedSort.ascending().and(nameSort).ascending();
        List<Department> departments = departmentRepository.findAll(sort);
        return convertToDto(departments);
    }

    public List<DepartmentDTO> getAllNotDeletedDepartmentDTO() {
        return departmentRepository.getAllNotDeleted();
    }

    private List<DepartmentDTO> convertToDto(List<Department> departments) {
        return departments.stream()
                .map(d -> convertToDto(d)).collect(toList());
    }

    @Override
    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Department with id '%s' not found", id)));
    }

    @Override
    public DepartmentDTO getByIdDepartmentDTO(Long id) {
        Department department = getById(id);
        return convertToDto(department);
    }

    @Override
    @Transactional
    public void update(Department department) {
        LOGGER.debug("update() [department={}])", department);
        try {
            departmentRepository.save(department);
        } catch (DataAccessException e) {
            String message = format("Operation for department %s failed.", department);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Override
    public void update(DepartmentDTO departmentDTO) {
        Department department = convertFromDto(departmentDTO);
        update(department);
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        departmentRepository.markDeleted(id, deleted);
    }

    private DepartmentDTO convertToDto(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setDeleted(department.isDeleted());
        departmentDTO.setFacultyId(department.getFaculty().getId());
        departmentDTO.setFacultyName(department.getFaculty().getName());
        return departmentDTO;
    }

    private Department convertFromDto(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setName(departmentDTO.getName());
        department.setFaculty(new Faculty(departmentDTO.getFacultyId(), departmentDTO.getFacultyName()));
        department.setDeleted(departmentDTO.isDeleted());
        return department;
    }
}
