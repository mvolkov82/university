package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import com.foxminded.university.model.Auditorium;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.TimetableItem;
import com.foxminded.university.model.TimetableItem_;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.model.dto.TimetableItemDTO;
import com.foxminded.university.model.person.Teacher;
import com.foxminded.university.repository.TimeTableItemsRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.LogicalException;
import com.foxminded.university.service.AuditoriumService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.TimetableItemService;
import com.google.inject.internal.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TimetableItemServiceImpl implements TimetableItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimetableItemServiceImpl.class);

    private TimeTableItemsRepository timeTableItemsRepository;
    private GroupService groupService;
    private LectureService lectureService;
    private TeacherService teacherService;
    private SubjectService subjectService;
    private AuditoriumService auditoriumService;

    @Autowired
    public TimetableItemServiceImpl(TimeTableItemsRepository timeTableItemsRepository, GroupService groupService, LectureService lectureService, TeacherService teacherService, SubjectService subjectService, AuditoriumService auditoriumService) {
        this.timeTableItemsRepository = timeTableItemsRepository;
        this.groupService = groupService;
        this.lectureService = lectureService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.auditoriumService = auditoriumService;
    }

    public TimetableItemServiceImpl() {
    }

    @Override
    public TimetableItem create(TimetableItem timeTableItem) {
        return timeTableItemsRepository.save(timeTableItem);
    }

    @Override
    public void create(TimetableItemDTO dto) {
        TimetableItem timeTableItem = convertFromDTO(dto);
        create(timeTableItem);
    }

    @Override
    public List<TimetableItem> getAll() {
        Sort deletedSort = Sort.by(TimetableItem_.DELETED);
        Sort dateSort = Sort.by(TimetableItem_.DATE);
        Sort lectureSort = Sort.by(TimetableItem_.LECTURE);
        Sort sort = deletedSort.descending().and(dateSort).ascending().and(lectureSort).ascending();

        return timeTableItemsRepository.findAll(sort);
    }

    @Override
    public List<TimetableItemDTO> getAllTimetableDTOS() {
        List<TimetableItem> timetableItems = getAll();
        return convertToDTO(timetableItems);
    }

    @Override
    public TimetableItem getById(Long id) {
        return timeTableItemsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Timetable item with id '%s' not found", id)));
    }

    public List<TimetableItemDTO> getAllNotDeletedDTO() {
        List<TimetableItem> timetableItems = timeTableItemsRepository.getAllNotDeleted();
        return convertToDTO(timetableItems);
    }

    public TimetableItemDTO getByIdTimeTableItemDTO(Long id) {
        TimetableItem timeTableItem = getById(id);
        return convertToDTO(timeTableItem);
    }

    @Override
    @Transactional
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [id={}, deleted={}])", id, deleted);
        TimetableItem timetableItem = getById(id);
        if (deleted) {
            if (timetableItem.getGroups() == null || timetableItem.getGroups().size() == 0) {
                timeTableItemsRepository.markDeleted(id, deleted);
            } else {
                throw new LogicalException("Can't delete timetable item while it has a group.");
            }
        } else {
            timeTableItemsRepository.markDeleted(id, deleted);
        }
    }

    @Transactional
    public void update(TimetableItem timeTableItem) {
        LOGGER.debug("update() [timeTableItem={}])", timeTableItem);
        timeTableItemsRepository.save(timeTableItem);
    }

    public void update(TimetableItemDTO tableItemDTO) {
        TimetableItem timeTableItem = convertFromDTO(tableItemDTO);
        update(timeTableItem);
    }

    @Override
    public void deleteGroupFromTimetableItem(GroupDTO groupDTO, TimetableItemDTO timeTableItemDTO) {
        List<GroupDTO> groupDTOS = Lists.newArrayList(timeTableItemDTO.getGroups());
        groupDTOS.remove(groupDTO);

        timeTableItemDTO.setGroups(new HashSet<>(groupDTOS));
        update(timeTableItemDTO);
    }

    @Override
    public void addGroupToTimetableItem(GroupDTO groupDTO, TimetableItemDTO timeTableItemDTO) {
        boolean isGroupBusy = isGroupBusy(timeTableItemDTO.getDate(), timeTableItemDTO.getLectureId(), groupDTO.getId());
        if (!isGroupBusy) {
            Set<GroupDTO> groups = timeTableItemDTO.getGroups();
            groups.add(groupDTO);
            update(timeTableItemDTO);
        } else {
            throw new LogicalException("The group is already busy at this time");
        }
    }

    @Override
    public boolean isTeacherBusy(LocalDate date, Long lectureId, Long teacherId) {
        Lecture lecture = lectureService.getById(lectureId);
        Teacher teacher = teacherService.getById(teacherId);
        return timeTableItemsRepository.isTeacherBusy(date, lecture, teacher);
    }

    @Override
    public boolean isAuditoriumOccupied(LocalDate date, Long lectureId, Long auditoriumId) {
        Lecture lecture = lectureService.getById(lectureId);
        Auditorium auditorium = auditoriumService.getById(auditoriumId);
        return timeTableItemsRepository.isAuditoriumOccupied(date, lecture, auditorium);
    }

    @Override
    public List<TimetableItemDTO> getAllForTeacher(LocalDate dateFrom, LocalDate dateTo, Long teacherId) {
        Teacher teacher = teacherService.getById(teacherId);
        List<TimetableItem> timetableItems = timeTableItemsRepository.getAllNotDeletedForTeacher(dateFrom, dateTo, teacher);
        return convertToDTO(timetableItems);
    }

    @Override
    public List<TimetableItemDTO> getAllForGroup(LocalDate from, LocalDate to, Long groupId) {
        Group group = groupService.getById(groupId);
        List<TimetableItem> timetableItems = timeTableItemsRepository.getAllNotDeletedForGroup(from, to, group);
        return convertToDTO(timetableItems);
    }

    @Override
    public List<TimetableItemDTO> getAllNotDeletedByDateAndGroupAndTeacherDTOS(String dateFrom, String dateTo, String groupId, String teacherId) {
        LocalDate from = null;
        if (dateFrom != null) {
            from = LocalDate.parse(dateFrom);
        }

        LocalDate to = null;
        if (dateTo != null) {
            to = LocalDate.parse(dateTo);
        }

        Group group = null;
        if (groupId != null) {
            group = groupService.getById(Long.valueOf(groupId));
        }

        Teacher teacher = null;
        if (teacherId != null) {
            teacher = teacherService.getById(Long.valueOf(teacherId));
        }

        List<TimetableItem> timetableItems = timeTableItemsRepository.getAllNotDeletedByDateAndGroupAndTeacher(from, to, group, teacher);
        return convertToDTO(timetableItems);
    }

    private boolean isGroupBusy(LocalDate date, Long lectureId, Long groupId) {
        Lecture lecture = lectureService.getById(lectureId);
        List<TimetableItem> timetableItems = timeTableItemsRepository.getTimetableItemsByDateAndLecture(date, lecture);

        List<Group> allGroupsAtThisTime = new ArrayList<>();
        if (timetableItems != null & !timetableItems.isEmpty()) {
            for (TimetableItem item : timetableItems) {
                for (Group group : item.getGroups()) {
                    allGroupsAtThisTime.add(group);
                }
            }
        }

        Group group = groupService.getById(groupId);
        boolean groupIsBusy = allGroupsAtThisTime.indexOf(group) != -1;
        return groupIsBusy;
    }

    private List<TimetableItemDTO> convertToDTO(List<TimetableItem> timetableItems) {
        return timetableItems.stream()
                .map(t -> (convertToDTO(t)))
                .collect(toList());
    }

    private TimetableItem convertFromDTO(TimetableItemDTO tableItemDTO) {
        TimetableItem timeTableItem;
        if (tableItemDTO.getId() != null) {
            timeTableItem = getById(tableItemDTO.getId());
        } else {
            timeTableItem = new TimetableItem();
        }

        LocalDate date = tableItemDTO.getDate();
        Lecture lecture = lectureService.getById(tableItemDTO.getLectureId());
        Teacher teacher = teacherService.getById(tableItemDTO.getTeacherId());
        Subject subject = subjectService.getById(tableItemDTO.getSubjectId());
        Auditorium auditorium = auditoriumService.getById(tableItemDTO.getAuditoriumId());

        timeTableItem.setDate(date);
        timeTableItem.setLecture(lecture);
        timeTableItem.setTeacher(teacher);
        timeTableItem.setSubject(subject);
        timeTableItem.setAuditorium(auditorium);

        if (timeTableItem.getGroups() != null && tableItemDTO.getGroups() != null) {
            Set<Group> groups = groupService.convertFromDto(tableItemDTO.getGroups());
            timeTableItem.setGroups(groups);
        }

        return timeTableItem;
    }

    private TimetableItemDTO convertToDTO(TimetableItem timeTableItem) {
        TimetableItemDTO dto = new TimetableItemDTO();
        dto.setId(timeTableItem.getId());
        dto.setDate(timeTableItem.getDate());
        Lecture lecture = timeTableItem.getLecture();
        dto.setLectureId(lecture.getId());
        dto.setLectureName(lecture.getName());
        dto.setNum(lecture.getNum());
        dto.setStart(lecture.getStart());
        dto.setFinish(lecture.getFinish());
        Teacher teacher = timeTableItem.getTeacher();
        dto.setTeacherId(teacher.getId());
        dto.setTeacherName(teacher.getFirstName() + " " + teacher.getLastName());
        dto.setTeacherDegree(teacher.getDegree().getDisplayValue());

        Department teacherDepartment = teacher.getDepartment();
        dto.setTeacherDepartmentId(teacherDepartment.getId());
        dto.setTeacherDepartmentName(teacherDepartment.getName());

        Faculty teacherDepartmentFaculty = teacherDepartment.getFaculty();
        dto.setTeacherDepartmentFacultyId(teacherDepartmentFaculty.getId());
        dto.setTeacherDepartmentFacultyName(teacherDepartmentFaculty.getName());

        Subject subject = timeTableItem.getSubject();
        dto.setSubjectId(subject.getId());
        dto.setSubjectName(subject.getName());

        Auditorium auditorium = timeTableItem.getAuditorium();
        dto.setAuditoriumId(auditorium.getId());
        dto.setAuditoriumName(auditorium.getName());

        Department departmentAuditorium = auditorium.getDepartment();
        dto.setAuditoriumDepartmentId(departmentAuditorium.getId());
        dto.setAuditoriumDepartmentName(departmentAuditorium.getName());

        Faculty facultyAuditorium = departmentAuditorium.getFaculty();
        dto.setAuditoriumDepartmentFacultyId(facultyAuditorium.getId());
        dto.setAuditoriumDepartmentFacultyName(facultyAuditorium.getName());

        Set<GroupDTO> groupDTOS = groupService.convertToDtoSet(timeTableItem.getGroups());
        dto.setGroups(groupDTOS);

        dto.setDeleted(timeTableItem.isDeleted());

        return dto;
    }
}
