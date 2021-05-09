package com.foxminded.university.model.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class TimetableSearchingFilterDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    private Long teacherId;

    private Long groupId;

    public TimetableSearchingFilterDTO() {
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
