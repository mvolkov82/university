package com.foxminded.university.model.dto;

import javax.validation.constraints.NotEmpty;

public class DepartmentDTO {
    private Long id;

    @NotEmpty(message = "Name can't be empty")
    private String name;

    private Long facultyId;
    private String facultyName;
    private boolean deleted;

    public DepartmentDTO() {
    }

    public DepartmentDTO(Long id, @NotEmpty String name, Long facultyId, String facultyName) {
        this.id = id;
        this.name = name;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
    }

    public DepartmentDTO(Long id, @NotEmpty(message = "Name can't be empty") String name, boolean deleted, Long facultyId, String facultyName) {
        this.id = id;
        this.name = name;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
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

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDTO)) {
            return false;
        }

        DepartmentDTO that = (DepartmentDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return facultyId != null ? facultyId.equals(that.facultyId) : that.facultyId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (facultyId != null ? facultyId.hashCode() : 0);
        return result;
    }
}
