package com.foxminded.university.service.impl;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.foxminded.university.model.Group;
import com.foxminded.university.repository.GroupRepository;
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
public class GroupServiceImplTest {

    private TestRepository testRepository = new TestRepository();

    @Mock
    private GroupRepository mockGroupRepository;
    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    public void setUpBeforeClass() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCreateGroup() {
        Group expected = testRepository.getGroup();
        Group createdGroup = new Group(1L, "GR-1");
        when(mockGroupRepository.save(createdGroup)).thenReturn(expected);
        assertEquals(expected, groupService.create(createdGroup));
    }

    @Test
    void shouldThrowQueryNotExecuteExceptionIfCreateGroupFailed() {
        Group createdGroup = testRepository.getCreatedGroup();
        when(mockGroupRepository.save(createdGroup)).thenThrow(new DataAccessException("") {
        });
        assertThrows(QueryNotExecuteException.class, () -> groupService.create(createdGroup));
    }

    @Test
    void shouldReturnAllGroups() {
        List<Group> expected = testRepository.getAllGroups();
        when(mockGroupRepository.findAll()).thenReturn(expected);
        assertEquals(expected, groupService.getAll());
    }

    @Test
    void shouldReturnGroupById() {
        Group expected = testRepository.getGroup();
        when(mockGroupRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        assertEquals(expected, groupService.getById(expected.getId()));
    }

    @Test
    void shouldUpdateGroup() {
        Group group = testRepository.getGroup();
        groupService.update(group);
        verify(mockGroupRepository, times(1)).save(group);
    }

    @Test
    void shouldThrowQueryNotExecuteExceptionIfUpdateGroupFailed() {
        Group group = testRepository.getGroup();
        when(mockGroupRepository.save(group)).thenThrow(new DataAccessException("") {
        });
        assertThrows(QueryNotExecuteException.class, () -> groupService.update(group));
    }

    @Test
    void shouldDeleteGroup() {
        Group group = testRepository.getGroup();
        groupService.markDeleted(group.getId(), true);
        verify(mockGroupRepository, times(1)).markDeleted(group.getId(), true);
    }

    @Test
    void shouldRestoreGroup() {
        Group deletedGroup = testRepository.getDeletedGroup();
        groupService.markDeleted(deletedGroup.getId(), false);
        verify(mockGroupRepository, times(1)).markDeleted(deletedGroup.getId(), false);
    }
}
