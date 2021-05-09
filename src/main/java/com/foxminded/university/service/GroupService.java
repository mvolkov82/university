package com.foxminded.university.service;

import java.util.List;
import java.util.Set;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.dto.GroupDTO;

public interface GroupService extends Service<Group> {
    List<GroupDTO> getAllGroupsDTO();

    List<GroupDTO> getAllNotDeletedDTOS();

    List<GroupDTO> findGroupDTOSNotInListAndNotDeleted(Set<GroupDTO> attachedGroups);

    GroupDTO getByIdGroupDTO(Long id);

    void update(GroupDTO groupDTO);

    void create(GroupDTO groupDTO);

    void markDeleted(Long id, boolean deleted);

    GroupDTO convertToDto(Group g);

    Set<GroupDTO> convertToDtoSet(Set<Group> groups);

    Set<Group> convertFromDto(Set<GroupDTO> groups);
}
