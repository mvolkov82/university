package com.foxminded.university.repository.custom.impl;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.model.person.Student;
import com.foxminded.university.repository.StudentRepository;
import com.foxminded.university.repository.TestRepository;
import com.foxminded.university.repository.TestValuesInitializer;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomizedStudentRepositoryImplTest {
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
    void shouldSetDeletedFlagWhenDelete() {
        Student student = testRepository.getAllStudents().get(0);
        studentRepository.markDeleted(student.getId(), true);
        Student deletedStudent = studentRepository.findById(student.getId()).get();
        assertTrue(deletedStudent.isDeleted());
    }

    @Test
    void shouldGetAllNotDeletedDTOs() {
        List<StudentDTO> expected = testRepository.getNotDeletedStudentDTOList();
        List<StudentDTO> actual = studentRepository.getAllNotDeleted();
        assertEquals(expected, actual);
    }
}
