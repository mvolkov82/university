package com.foxminded.university.repository;

import com.foxminded.university.model.Group;
import com.foxminded.university.repository.custom.CustomizedGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CustomizedGroupRepository, JpaRepository<Group, Long> {
}
