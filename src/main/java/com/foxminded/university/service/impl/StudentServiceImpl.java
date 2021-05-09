package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.model.person.Student;
import com.foxminded.university.model.person.Student_;
import com.foxminded.university.repository.StudentRepository;
import com.foxminded.university.repository.custom.impl.CustomizedStudentRepositoryImpl;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    public static final Logger LOGGER = LoggerFactory.getLogger(CustomizedStudentRepositoryImpl.class);
    private StudentRepository studentRepository;
    private GroupService groupService;


    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, GroupService groupService) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
    }

    public StudentServiceImpl() {
    }

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    @Override
    public Student create(Student student) {
        LOGGER.debug("create() [student={}])", student);
        try {
            studentRepository.save(student);
        } catch (DataAccessException e) {
            String message = format("Operation for student %s failed.", student);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return student;
    }


    public void create(StudentDTO studentDTO) {
        Student student = convertFromDTO(studentDTO);
        create(student);
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public List<StudentDTO> getAllStudentsDTO() {
        Sort lastNameSort = Sort.by(Student_.LAST_NAME);
        Sort firstNameSort = Sort.by(Student_.FIRST_NAME);
        Sort deletedSort = Sort.by(Student_.DELETED);
        Sort sorting = deletedSort.ascending().and(firstNameSort).and(lastNameSort);
        List<Student> students = studentRepository.findAll(sorting);
        return convertToDto(students);
    }

    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Student with id '%s' not found", id)));
    }

    public StudentDTO getByIdStudentDTO(Long id) {
        Student student = getById(id);
        return convertToDto(student);
    }

    @Override
    public void update(Student student) {
        LOGGER.debug("update() [student={}])", student);
        try {
            studentRepository.save(student);
        } catch (DataAccessException e) {
            String message = format("Operation for student %s failed.", student);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    public void update(StudentDTO studentDTO) {
        Student student = convertFromDTO(studentDTO);
        update(student);
    }

    @Override
    public int countByGroupId(Long id) {
        return studentRepository.countByGroupId(id);
    }

    @Override
    public boolean existsStudentByIdAndGroupId(Long id, Long groupId) {
        return studentRepository.existsStudentByIdAndGroupId(id, groupId);
    }

    @Transactional
    @Override
    public void markDeleted(Long id, boolean deleted) {
        studentRepository.markDeleted(id, deleted);
    }

    private StudentDTO convertToDto(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setBirthDay(student.getBirthDay());
        studentDTO.setAddress(student.getAddress());
        studentDTO.setPhone(student.getPhone());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setStartDate(student.getStartDate());
        if (student.getGroup() != null) {
            studentDTO.setGroupId(student.getGroup().getId());
            studentDTO.setGroupName(student.getGroup().getName());
        }
        studentDTO.setDeleted(student.isDeleted());

        return studentDTO;
    }

    private List<StudentDTO> convertToDto(List<Student> students) {
        return students.stream()
                .map(s -> convertToDto(s))
                .collect(toList());
    }

    private Student convertFromDTO(StudentDTO studentDTO) {
        Student student;
        if (studentDTO.getId() != null) {
            student = getById(studentDTO.getId());
        } else {
            student = new Student();
        }
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setBirthDay(studentDTO.getBirthDay());
        student.setAddress(studentDTO.getAddress());
        student.setPhone(studentDTO.getPhone());
        student.setEmail(studentDTO.getEmail());
        student.setStartDate(studentDTO.getStartDate());

        Group group = null;
        if (studentDTO.getGroupId() != null) {
            group = groupService.getById(studentDTO.getGroupId());
        }
        student.setGroup(group);

        return student;
    }
}

