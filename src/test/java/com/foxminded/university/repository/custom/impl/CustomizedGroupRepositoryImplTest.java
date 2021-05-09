package com.foxminded.university.repository.custom.impl;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.repository.GroupRepository;
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
class CustomizedGroupRepositoryImplTest {
    private TestRepository testRepository = new TestRepository();
    private TestValuesInitializer testValuesInitializer = new TestValuesInitializer();

    @Autowired
    private Flyway flyway;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CustomizedGroupRepositoryImplTest(DataSource dataSource) {

    }

    @BeforeEach
    public void setUpBeforeClass() {
        flyway.clean();
        flyway.migrate();
        testValuesInitializer.initialize(dataSource);
    }

    @Test
    void shouldGetAllNotDeletedDTOs() {
        List<GroupDTO> expected = testRepository.getNotDeletedGroupDTOList();
        List<GroupDTO> actual = groupRepository.getAllNotDeleted();
        assertEquals(expected, actual);
    }

    @Test
    void shouldSetFlagDeletedWhenDeleteGroup() {
        Group group = testRepository.getGroup();
        groupRepository.markDeleted(group.getId(), true);
        Group deletedGroup = groupRepository.findById(group.getId()).get();
        assertTrue(deletedGroup.isDeleted());
    }
}
