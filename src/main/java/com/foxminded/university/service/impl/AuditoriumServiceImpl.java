package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import com.foxminded.university.model.Auditorium;
import com.foxminded.university.model.Auditorium_;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.dto.AuditoriumDTO;
import com.foxminded.university.repository.AuditoriumRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.AuditoriumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AuditoriumServiceImpl implements AuditoriumService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditoriumServiceImpl.class);
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    public AuditoriumServiceImpl(AuditoriumRepository auditoriumRepository) {
        this.auditoriumRepository = auditoriumRepository;
    }

    @Override
    @Transactional
    public Auditorium create(Auditorium auditorium) {
        LOGGER.debug("create() [auditorium={}])", auditorium);
        try {
            auditoriumRepository.save(auditorium);
        } catch (DataAccessException e) {
            String message = format("Operation for auditorium %s failed.", auditorium);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return auditorium;
    }

    @Override
    @Transactional
    public void create(AuditoriumDTO auditoriumDTO) {
        Auditorium auditorium = convertFromDTO(auditoriumDTO);
        create(auditorium);
    }

    @Override
    public List<AuditoriumDTO> getAllAuditoriumsDTO() {
        Sort deletedSort = Sort.by(Auditorium_.DELETED);
        Sort nameSort = Sort.by(Auditorium_.NAME);
        Sort sort = deletedSort.ascending().and(nameSort).ascending();
        List<Auditorium> auditoriums = auditoriumRepository.findAll(sort);
        return convertToDto(auditoriums);
    }

    @Override
    public List<Auditorium> getAll() {
        return auditoriumRepository.findAll();
    }

    @Override
    public List<AuditoriumDTO> getAllNotDeletedAuditoriumsDTO() {
        return auditoriumRepository.getAllNotDeleted();
    }

    @Override
    public AuditoriumDTO getByIdAuditoriumDTO(Long id) {
        Auditorium auditorium = getById(id);
        return convertToDto(auditorium);
    }

    @Override
    @Transactional
    public void update(Auditorium auditorium) {
        LOGGER.debug("update() [auditorium={}])", auditorium);
        try {
            auditoriumRepository.save(auditorium);
        } catch (DataAccessException e) {
            String message = format("Operation for auditorium %s failed.", auditorium);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Override
    public void update(AuditoriumDTO auditoriumDTO) {
        Auditorium auditorium = convertFromDTO(auditoriumDTO);
        update(auditorium);
    }

    @Override
    @Transactional
    public void markDeleted(Long id, boolean deleted) {
        auditoriumRepository.markDeleted(id, deleted);
    }

    @Override
    public Auditorium getById(Long id) {
        return auditoriumRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(format("Auditorium with id '%s' not found", id)));
    }

    private AuditoriumDTO convertToDto(Auditorium auditorium) {
        AuditoriumDTO auditoriumDTO = new AuditoriumDTO();
        auditoriumDTO.setId(auditorium.getId());
        auditoriumDTO.setName(auditorium.getName());
        auditoriumDTO.setDeleted(auditorium.isDeleted());
        auditoriumDTO.setDepartmentId(auditorium.getDepartment().getId());
        auditoriumDTO.setDepartmentName(auditorium.getDepartment().getName());
        auditoriumDTO.setFacultyID(auditorium.getDepartment().getFaculty().getId());
        auditoriumDTO.setFacultyName(auditorium.getDepartment().getFaculty().getName());
        return auditoriumDTO;
    }

    private List<AuditoriumDTO> convertToDto(List<Auditorium> auditoriums) {
        return auditoriums.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Auditorium convertFromDTO(AuditoriumDTO auditoriumDTO) {
        Auditorium auditorium;
        if (auditoriumDTO.getId() != null) {
            auditorium = getById(auditoriumDTO.getId());
        } else {
            auditorium = new Auditorium();
        }
        auditorium.setName(auditoriumDTO.getName());
        auditorium.setDeleted(auditorium.isDeleted());

        Department department = getDepartmentFromDto(auditoriumDTO);
        auditorium.setDepartment(department);

        return auditorium;
    }

    private Department getDepartmentFromDto(AuditoriumDTO auditoriumDTO) {
        Department department = new Department();
        department.setId(auditoriumDTO.getDepartmentId());
        department.setName(auditoriumDTO.getDepartmentName());
        return department;
    }
}
