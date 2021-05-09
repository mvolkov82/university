package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Group_;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.repository.GroupRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.DepartmentService;
import com.foxminded.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    public static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    private GroupRepository groupRepository;
    private DepartmentService departmentService;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, DepartmentService departmentService) {
        this.groupRepository = groupRepository;
        this.departmentService = departmentService;
    }

    @Transactional
    @Override
    public Group create(Group group) {
        LOGGER.debug("create() [group={}])", group);
        try {
            groupRepository.save(group);
        } catch (DataAccessException e) {
            String message = format("Operation for group %s failed.", group);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return group;
    }

    @Transactional
    public void create(GroupDTO groupDTO) {
        Group group = convertFromDto(groupDTO);
        create(group);
    }

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public List<GroupDTO> getAllGroupsDTO() {
        Sort deletedSort = Sort.by(Group_.DELETED);
        Sort nameSort = Sort.by(Group_.NAME);
        Sort sort = deletedSort.ascending().and(nameSort).ascending();
        List<Group> groups = groupRepository.findAll(sort);
        return convertToDto(groups);
    }

    public List<GroupDTO> getAllNotDeletedDTOS() {
        return groupRepository.getAllNotDeleted();
    }

    @Override
    public List<GroupDTO> findGroupDTOSNotInListAndNotDeleted(Set<GroupDTO> attachedGroupDTOS) {
        Set<Group> groups = convertFromDto(attachedGroupDTOS);
        return convertToDto(groupRepository.findGroupsNotInListAndNotDeleted(groups));
    }

    @Override
    public Group getById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Group with id '%s' not found", id)));
    }

    @Override
    public GroupDTO getByIdGroupDTO(Long id) {
        Group group = getById(id);
        return convertToDto(group);
    }

    @Transactional
    @Override
    public void markDeleted(Long id, boolean deleted) {
        groupRepository.markDeleted(id, deleted);
    }

    @Transactional
    @Override
    public void update(Group group) {
        try {
            groupRepository.save(group);
        } catch (DataAccessException e) {
            String message = format("Operation for group %s failed.", group);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Transactional
    @Override
    public void update(GroupDTO groupDTO) {
        Group group = convertFromDto(groupDTO);
        update(group);
    }

    public GroupDTO convertToDto(Group group) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        Department department = group.getDepartment();
        if (department != null) {
            groupDTO.setDepartmentId(department.getId());
            groupDTO.setDepartmentName(department.getName());

            Faculty faculty = department.getFaculty();
            if (faculty != null) {
                groupDTO.setFacultyId(faculty.getId());
                groupDTO.setFacultyName(faculty.getName());
            }
        }
        groupDTO.setDeleted(group.isDeleted());
        return groupDTO;
    }

    @Override
    public Set<GroupDTO> convertToDtoSet(Set<Group> groups) {
        return groups.stream()
                .map(s -> convertToDto(s))
                .collect(toSet());
    }

    @Override
    public Set<Group> convertFromDto(Set<GroupDTO> groups) {
        return groups.stream()
                .map(s -> convertFromDto(s))
                .collect(toSet());
    }

    private List<GroupDTO> convertToDto(List<Group> groups) {
        return groups.stream()
                .map(s -> convertToDto(s))
                .collect(toList());
    }

    private Group convertFromDto(GroupDTO groupDTO) {
        Group group;
        if (groupDTO.getId() != null) {
            group = getById(groupDTO.getId());
        } else {
            group = new Group();
        }
        group.setName(groupDTO.getName());
        group.setDepartment(departmentService.getById(groupDTO.getDepartmentId()));
        return group;
    }
}
