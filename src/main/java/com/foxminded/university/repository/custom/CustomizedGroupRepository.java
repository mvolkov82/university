package com.foxminded.university.repository.custom;

import java.util.List;
import java.util.Set;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.dto.GroupDTO;

public interface CustomizedGroupRepository extends CustomizedRepository<GroupDTO> {
    List<Group> findGroupsNotInListAndNotDeleted(Set<Group> timetableItemGroups);
}
