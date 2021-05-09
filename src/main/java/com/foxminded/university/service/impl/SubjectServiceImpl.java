package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Subject_;
import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.model.dto.TeacherDTO;
import com.foxminded.university.repository.SubjectRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {
    public static final Logger LOGGER = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional
    public Subject create(Subject subject) {
        LOGGER.debug("create() [subject={}])", subject);
        try {
            subjectRepository.save(subject);
        } catch (DataAccessException e) {
            String message = format("Operation for subject %s failed.", subject);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return null;
    }

    @Override
    public void create(SubjectDTO subjectDTO) {
        Subject subject = convertFromDto(subjectDTO);
        create(subject);
    }

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Subject with id '%s' not found", id)));
    }

    @Override
    public List<SubjectDTO> getAllSubjectsDTO() {
        Sort deletedSort = Sort.by(Subject_.DELETED);
        Sort nameSort = Sort.by(Subject_.NAME);
        Sort sort = deletedSort.ascending().and(nameSort).ascending();
        List<Subject> subjects = subjectRepository.findAll(sort);
        return convertToDto(subjects);
    }

    public List<SubjectDTO> getAllNotDeletedSubjectsDTO() {
        return subjectRepository.getAllNotDeleted();
    }

    public List<SubjectDTO> getNotTeachersSubjectDTOs(TeacherDTO teacherDTO) {
        List<SubjectDTO> teacherSubjectDTOS = teacherDTO.getSubjectDTOS();
        List<Subject> teacherSubjects = convertFromDto(teacherSubjectDTOS);
        List<Subject> subjectsNotInTeacher = subjectRepository.findSubjectNotInListAndNotDeleted(teacherSubjects);

        return convertToDto(subjectsNotInTeacher);
    }


    @Override
    public SubjectDTO getByIdSubjectDTO(Long id) {
        Subject subject = getById(id);
        return convertToDto(subject);
    }

    @Override
    @Transactional
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [id={}, deleted={}])", id, deleted);
        subjectRepository.markDeleted(id, deleted);
    }


    @Override
    @Transactional
    public void update(Subject subject) {
        LOGGER.debug("update() [subject={}])", subject);
        try {
            subjectRepository.save(subject);
        } catch (DataAccessException e) {
            String message = format("Operation for subject %s failed.", subject);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Override
    public void update(SubjectDTO subjectDTO) {
        Subject subject = convertFromDto(subjectDTO);
        update(subject);
    }

    @Override
    public List<Subject> convertFromDto(List<SubjectDTO> subjectsDTOS) {
        return subjectsDTOS.stream()
                .map(s -> convertFromDto(s))
                .collect(toList());
    }

    @Override
    public List<SubjectDTO> getDTOSAsList(Set<Subject> subjectSet) {
        List<Subject> list = new ArrayList<>(subjectSet);
        return (convertToDto(list));
    }

    private Subject convertFromDto(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setId(subjectDTO.getId());
        subject.setName(subjectDTO.getName());
        subject.setDeleted(subjectDTO.isDeleted());
        return subject;
    }

    public List<SubjectDTO> convertToDto(List<Subject> subjects) {
        return subjects.stream()
                .map(s -> convertToDto(s))
                .collect(toList());
    }

    public List<SubjectDTO> convertToDto(Set<Subject> subjects) {
        return subjects.stream()
                .map(s -> convertToDto(s))
                .collect(toList());
    }

    private SubjectDTO convertToDto(Subject subject) {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(subject.getId());
        subjectDTO.setName(subject.getName());
        subjectDTO.setDeleted(subject.isDeleted());
        return subjectDTO;
    }
}
