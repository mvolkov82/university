package com.foxminded.university.model.dto;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.foxminded.university.service.validator.annotation.NotBusyTwiceTeacher;
import com.foxminded.university.service.validator.annotation.NotOccupyTwiceAuditorium;
import org.springframework.format.annotation.DateTimeFormat;


@NotBusyTwiceTeacher
@NotOccupyTwiceAuditorium
@GroupSequence({NotBusyTwiceTeacher.class, NotOccupyTwiceAuditorium.class, TimetableItemDTO.class})

public class TimetableItemDTO {
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date can't be empty", groups = {NotBusyTwiceTeacher.class, NotOccupyTwiceAuditorium.class})
    private LocalDate date;

    @NotNull(message = "Lecture order can't be empty", groups = {NotBusyTwiceTeacher.class, NotOccupyTwiceAuditorium.class})
    private Long lectureId;
    private String lectureName;
    private int num;
    private LocalTime start;
    private LocalTime finish;

    @NotNull(message = "Teacher can't be empty", groups = {NotBusyTwiceTeacher.class, NotOccupyTwiceAuditorium.class})
    private Long teacherId;
    private String teacherName;
    private String teacherDegree;
    private Long teacherDepartmentId;
    private String teacherDepartmentName;
    private Long teacherDepartmentFacultyId;
    private String teacherDepartmentFacultyName;

    @NotNull(message = "Subject can't be empty", groups = {NotBusyTwiceTeacher.class, NotOccupyTwiceAuditorium.class})
    private Long subjectId;
    private String subjectName;

    @NotNull(message = "Auditorium can't be empty", groups = {NotBusyTwiceTeacher.class, NotOccupyTwiceAuditorium.class})
    private Long auditoriumId;
    private String auditoriumName;
    private Long auditoriumDepartmentId;
    private String auditoriumDepartmentName;
    private Long auditoriumDepartmentFacultyId;
    private String auditoriumDepartmentFacultyName;

    private boolean deleted;

    private Set<GroupDTO> groups;

    public TimetableItemDTO() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getFinish() {
        return finish;
    }

    public void setFinish(LocalTime finish) {
        this.finish = finish;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Long getTeacherDepartmentId() {
        return teacherDepartmentId;
    }

    public void setTeacherDepartmentId(Long teacherDepartmentId) {
        this.teacherDepartmentId = teacherDepartmentId;
    }

    public String getTeacherDepartmentName() {
        return teacherDepartmentName;
    }

    public void setTeacherDepartmentName(String teacherDepartmentName) {
        this.teacherDepartmentName = teacherDepartmentName;
    }

    public Long getTeacherDepartmentFacultyId() {
        return teacherDepartmentFacultyId;
    }

    public void setTeacherDepartmentFacultyId(Long teacherDepartmentFacultyId) {
        this.teacherDepartmentFacultyId = teacherDepartmentFacultyId;
    }

    public String getTeacherDepartmentFacultyName() {
        return teacherDepartmentFacultyName;
    }

    public void setTeacherDepartmentFacultyName(String teacherDepartmentFacultyName) {
        this.teacherDepartmentFacultyName = teacherDepartmentFacultyName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getAuditoriumId() {
        return auditoriumId;
    }

    public void setAuditoriumId(Long auditoriumId) {
        this.auditoriumId = auditoriumId;
    }

    public String getAuditoriumName() {
        return auditoriumName;
    }

    public void setAuditoriumName(String auditoriumName) {
        this.auditoriumName = auditoriumName;
    }

    public Long getAuditoriumDepartmentId() {
        return auditoriumDepartmentId;
    }

    public void setAuditoriumDepartmentId(Long auditoriumDepartmentId) {
        this.auditoriumDepartmentId = auditoriumDepartmentId;
    }

    public String getAuditoriumDepartmentName() {
        return auditoriumDepartmentName;
    }

    public void setAuditoriumDepartmentName(String auditoriumDepartmentName) {
        this.auditoriumDepartmentName = auditoriumDepartmentName;
    }

    public Long getAuditoriumDepartmentFacultyId() {
        return auditoriumDepartmentFacultyId;
    }

    public void setAuditoriumDepartmentFacultyId(Long auditoriumDepartmentFacultyId) {
        this.auditoriumDepartmentFacultyId = auditoriumDepartmentFacultyId;
    }

    public String getAuditoriumDepartmentFacultyName() {
        return auditoriumDepartmentFacultyName;
    }

    public void setAuditoriumDepartmentFacultyName(String auditoriumDepartmentFacultyName) {
        this.auditoriumDepartmentFacultyName = auditoriumDepartmentFacultyName;
    }

    public Set<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupDTO> groups) {
        this.groups = groups;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTeacherDegree() {
        return teacherDegree;
    }

    public void setTeacherDegree(String teacherDegree) {
        this.teacherDegree = teacherDegree;
    }
}

