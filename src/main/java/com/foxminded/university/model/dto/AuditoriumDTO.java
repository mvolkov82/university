package com.foxminded.university.model.dto;

import javax.validation.constraints.NotEmpty;

public class AuditoriumDTO {
    private Long id;

    @NotEmpty(message = "Name can't be empty")
    private String name;

    private Long departmentId;
    private String departmentName;
    private boolean deleted;
    private Long facultyID;
    private String facultyName;

    public AuditoriumDTO() {
    }

    public AuditoriumDTO(Long id, @NotEmpty(message = "Name can't be empty") String name, boolean deleted, Long departmentId, String departmentName) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean isDeleted() {
        return deleted;
    }



    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(Long facultyID) {
        this.facultyID = facultyID;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
