package com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.TimetableItem;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.model.dto.TimetableItemDTO;

public interface TimetableItemService extends Service<TimetableItem> {

    List<TimetableItemDTO> getAllNotDeletedDTO();

    List<TimetableItemDTO> getAllTimetableDTOS();

    List<TimetableItemDTO> getAllForTeacher(LocalDate dateFrom, LocalDate dateTo, Long teacherId);

    List<TimetableItemDTO> getAllForGroup(LocalDate from, LocalDate to, Long groupId);

    List<TimetableItemDTO> getAllNotDeletedByDateAndGroupAndTeacherDTOS(String dateFrom, String dateTo, String groupId, String teacherId);

    TimetableItemDTO getByIdTimeTableItemDTO(Long id);

    void create(TimetableItemDTO dto);

    void update(TimetableItemDTO dto);

    void deleteGroupFromTimetableItem(GroupDTO groupDTO, TimetableItemDTO timeTableItemDTO);

    void addGroupToTimetableItem(GroupDTO groupDTO, TimetableItemDTO timeTableItemDTO);

    boolean isTeacherBusy(LocalDate date, Long lectureId, Long teacherId);

    boolean isAuditoriumOccupied(LocalDate date, Long lectureId, Long auditoriumId);

    void markDeleted(Long id, boolean deleted);
}
