package com.foxminded.university.repository;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.foxminded.university.model.person.Student;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {
    private TestRepository testRepository = new TestRepository();
    private TestValuesInitializer testValuesInitializer = new TestValuesInitializer();

    @Autowired
    private Flyway flyway;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUpBeforeClass() {
        flyway.clean();
        flyway.migrate();
        testValuesInitializer.initialize(dataSource);
    }

    @Test
    void shouldCreateStudent() {
        Student expected = testRepository.getCreatedStudent();
        Long id = expected.getId();
        expected.setId(null);
        studentRepository.save(expected);
        Student actual = studentRepository.findById(id).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateStudent() {
        Student student = testRepository.getAllStudents().get(0);
        Student updatedStudent = testRepository.getUpdatedStudent(student);
        studentRepository.save(updatedStudent);
        Student actualStudent = studentRepository.findById(updatedStudent.getId()).get();
        compareStudentsByAllFields(student, actualStudent);
    }

    @Test
    void shouldReturnAllStudents() {
        assertEquals(testRepository.getAllStudents(), studentRepository.findAll());
    }

    private void compareStudentsByAllFields(Student expected, Student actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getBirthDay(), actual.getBirthDay());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getPhone(), actual.getPhone());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getGroup(), actual.getGroup());
        assertEquals(expected.getStartDate(), actual.getStartDate());
    }
}
