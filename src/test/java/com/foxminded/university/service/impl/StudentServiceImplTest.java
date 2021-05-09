package com.foxminded.university.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.foxminded.university.model.person.Student;
import com.foxminded.university.repository.StudentRepository;
import com.foxminded.university.repository.TestRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentServiceImplTest {

    private TestRepository testRepository = new TestRepository();
    @Mock
    private StudentRepository mockStudentRepository;
    @Mock
    private GroupServiceImpl mockGroupServiceImpl;
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    public void setUpBeforeClass() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCreateStudent() {
        Student expected = testRepository.getCreatedStudent();
        Student newStudent = new Student();
        newStudent.setId(4L);
        newStudent.setFirstName("John");
        newStudent.setLastName("Connor");
        newStudent.setBirthDay(LocalDate.of(1995, 05, 06));
        when(mockStudentRepository.save(newStudent)).thenReturn(expected);
        Student createdStudent = studentService.create(newStudent);
        assertEquals(expected, createdStudent);
    }

    @Test
    void shouldThrowQueryNotExecuteExceptionIfCreateStudentFailed() {
        Student createdStudent = testRepository.getCreatedStudent();
        when(mockStudentRepository.save(createdStudent)).thenThrow(new DataAccessException("") {
        });
        assertThrows(QueryNotExecuteException.class, () -> studentService.create(createdStudent));
    }

    @Test
    void shouldGetAll() {
        List<Student> expected = testRepository.getAllStudents();
        when(mockStudentRepository.findAll()).thenReturn(expected);
        assertEquals(expected, studentService.getAll());
    }

    @Test
    void shouldGetById() {
        Student expected = testRepository.getAllStudents().get(0);
        when(mockStudentRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        assertEquals(expected, studentService.getById(expected.getId()));
    }

    @Test
    void shouldUpdateStudent() {
        Student student = testRepository.getAllStudents().get(0);
        studentService.update(student);
        verify(mockStudentRepository, times(1)).save(student);
    }

    @Test
    void shouldThrowQueryNotExecuteExceptionIfUpdateStudentFailed() {
        Student student = testRepository.getStudent();
        when(mockStudentRepository.save(student)).thenThrow(new DataAccessException("") {
        });
        assertThrows(QueryNotExecuteException.class, () -> studentService.create(student));
    }

    @Test
    void shouldDeleteStudent() {
        Student student = testRepository.getStudent();
        studentService.markDeleted(student.getId(), true);
        verify(mockStudentRepository, times(1)).markDeleted(student.getId(), true);
    }

    @Test
    void shouldRestoreStudent() {
        Student deletedStudent = testRepository.getDeletedStudent();
        studentService.markDeleted(deletedStudent.getId(), false);
        verify(mockStudentRepository, times(1)).markDeleted(deletedStudent.getId(), false);
    }
}
