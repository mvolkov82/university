package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.model.dto.TeacherDTO;
import com.foxminded.university.model.person.Teacher;
import com.foxminded.university.model.person.Teacher_;
import com.foxminded.university.repository.TeacherRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.DegreeService;
import com.foxminded.university.service.DepartmentService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    public static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private TeacherRepository teacherRepository;
    private DepartmentService departmentService;
    private SubjectService subjectService;
    private DegreeService degreeService;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, DepartmentService departmentService, SubjectService subjectService, DegreeService degreeService) {
        this.teacherRepository = teacherRepository;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
        this.degreeService = degreeService;
    }

    public TeacherServiceImpl() {
    }

    @Transactional
    @Override
    public Teacher create(Teacher teacher) {
        LOGGER.debug("create() [teacher={}])", teacher);
        try {
            teacherRepository.save(teacher);
        } catch (DataAccessException e) {
            String message = format("Operation for teacher %s failed.", teacher);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return teacher;
    }


    public void create(TeacherDTO teacherDTO) {
        Teacher teacher = convertFromDTO(teacherDTO);
        create(teacher);
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public List<TeacherDTO> getAllTeachersDTO() {
        Sort lastNameSort = Sort.by(Teacher_.LAST_NAME);
        Sort firstNameSort = Sort.by(Teacher_.FIRST_NAME);
        Sort deletedSort = Sort.by(Teacher_.DELETED);
        Sort sorting = deletedSort.ascending().and(firstNameSort).and(lastNameSort);
        List<Teacher> teachers = teacherRepository.findAll(sorting);
        return convertToDto(teachers);
    }

    @Override
    public List<TeacherDTO> getAllNotDeletedDTOS() {
        return teacherRepository.getAllNotDeleted();
    }

    @Override
    public Teacher getById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Teacher with id '%s' not found", id)));
    }

    public TeacherDTO getByIdTeacherDTO(Long id) {
        Teacher teacher = getById(id);
        Set<Subject> notSortedSubjects = teacher.getLinkedSubjects();
        teacher.setLinkedSubjects(sortSubjectsByName(notSortedSubjects));
        return convertToDto(teacher);
    }

    @Transactional
    @Override
    public void update(Teacher teacher) {
        LOGGER.debug("update() [teacher={}])", teacher);
        try {
            teacherRepository.save(teacher);
        } catch (DataAccessException e) {
            String message = format("Operation for teacher %s failed.", teacher);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    public void update(TeacherDTO teacherDTO) {
        Teacher teacher = convertFromDTO(teacherDTO);
        update(teacher);
    }

    @Override
    @Transactional
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [id={}, deleted={}])", id, deleted);
        teacherRepository.markDeleted(id, deleted);
    }

    @Override
    public void deleteSubjectFromTeacher(SubjectDTO subjectDTO, TeacherDTO teacherDTO) {
        int indexDeletedSubject = teacherDTO.getSubjectDTOS().indexOf(subjectDTO);
        teacherDTO.getSubjectDTOS().remove(indexDeletedSubject);
        update(teacherDTO);
    }

    @Override
    public void addSubjectToTeacher(SubjectDTO subjectDTO, TeacherDTO teacherDTO) {
        teacherDTO.getSubjectDTOS().add(subjectDTO);
        update(teacherDTO);
    }

    private Set<Subject> sortSubjectsByName(Set<Subject> subjectSet) {
        List<Subject> subjects = new ArrayList<>(subjectSet);
        Collections.sort(subjects);
        Set<Subject> sortedSetSubjects = new TreeSet<>();
        for (Subject subject : subjects) {
            sortedSetSubjects.add(subject);
        }
        return sortedSetSubjects;
    }

    private TeacherDTO convertToDto(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setFirstName(teacher.getFirstName());
        teacherDTO.setLastName(teacher.getLastName());
        teacherDTO.setBirthDay(teacher.getBirthDay());
        teacherDTO.setAddress(teacher.getAddress());
        teacherDTO.setPhone(teacher.getPhone());
        teacherDTO.setEmail(teacher.getEmail());
        if (teacher.getDepartment() != null) {
            teacherDTO.setDepartmentId(teacher.getDepartment().getId());
            teacherDTO.setDepartmentName(teacher.getDepartment().getName());
        }
        teacherDTO.setDeleted(teacher.isDeleted());
        teacherDTO.setDegree(String.valueOf(teacher.getDegree().getDisplayValue()));

        Set<Subject> subjectSet = teacher.getLinkedSubjects();
        List<SubjectDTO> subjectDTOS = subjectService.getDTOSAsList(subjectSet);
        teacherDTO.setSubjectDTOS(subjectDTOS);

        return teacherDTO;
    }

    private List<TeacherDTO> convertToDto(List<Teacher> teachers) {
        return teachers.stream()
                .map(s -> convertToDto(s))
                .collect(toList());
    }

    private Teacher convertFromDTO(TeacherDTO teacherDTO) {
        Teacher teacher;
        if (teacherDTO.getId() != null) {
            teacher = getById(teacherDTO.getId());
        } else {
            teacher = new Teacher();
        }
        teacher.setFirstName(teacherDTO.getFirstName());
        teacher.setLastName(teacherDTO.getLastName());
        teacher.setBirthDay(teacherDTO.getBirthDay());
        teacher.setAddress(teacherDTO.getAddress());
        teacher.setPhone(teacherDTO.getPhone());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setDegree(degreeService.getDegree(teacherDTO.getDegree()));

        if (teacherDTO.getSubjectDTOS() != null) {
            List<SubjectDTO> subjectsDTOS = teacherDTO.getSubjectDTOS();
            List<Subject> subjects = subjectService.convertFromDto(subjectsDTOS);
            Set<Subject> subjectSet = new HashSet<>(subjects);
            teacher.setLinkedSubjects(subjectSet);
        }

        Department department = null;
        if (teacherDTO.getDepartmentId() != null) {
            department = departmentService.getById(teacherDTO.getDepartmentId());
        }
        teacher.setDepartment(department);

        return teacher;
    }
}
