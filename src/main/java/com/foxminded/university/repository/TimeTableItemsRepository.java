package com.foxminded.university.repository;

import com.foxminded.university.model.TimetableItem;
import com.foxminded.university.repository.custom.CustomizedTimetableItemsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableItemsRepository extends CustomizedTimetableItemsRepository, JpaRepository<TimetableItem, Long> {

}
