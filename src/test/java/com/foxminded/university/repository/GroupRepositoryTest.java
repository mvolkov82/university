package com.foxminded.university.repository;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Group;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupRepositoryTest {
    private TestRepository testRepository = new TestRepository();
    private TestValuesInitializer testValuesInitializer = new TestValuesInitializer();

    @Autowired
    private Flyway flyway;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupRepositoryTest(DataSource dataSource) {
    }

    @BeforeEach
    public void setUpBeforeClass() {
        flyway.clean();
        flyway.migrate();
        testValuesInitializer.initialize(dataSource);
    }

    @Test
    void shouldCreateGroup() {
        Group expected = testRepository.getCreatedGroup();
        Long nextIndex = testRepository.getAllGroups().size() + 1L;
        groupRepository.save(expected);
        Group actual = groupRepository.findById(nextIndex).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnAllGroups() {
        assertEquals(testRepository.getAllGroups(), groupRepository.findAll());
    }

    @Test
    void shouldUpdateGroup() {
        Group expectedGroup = testRepository.getGroup();
        Department newDepartment = testRepository.getAllDepartments().get(1);
        expectedGroup.setName("test");
        expectedGroup.setDepartment(newDepartment);
        groupRepository.save(expectedGroup);
        assertEquals(expectedGroup, groupRepository.findById(1L).get());
    }
}
