package com.foxminded.university.model.dto;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class GroupDTO {
    private Long id;

    @NotEmpty(message = "Name can't be empty")
    private String name;

    private Long departmentId;
    private String departmentName;
    private Long facultyId;
    private String facultyName;
    private boolean deleted;

    public GroupDTO() {
    }

    public GroupDTO(Long id, String name, boolean deleted, Long departmentId, String departmentName) {
        super();
        this.id = id;
        this.name = name;
        this.deleted = deleted;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public GroupDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
        if (!(o instanceof GroupDTO)) {
            return false;
        }
        GroupDTO groupDTO = (GroupDTO) o;
        return Objects.equals(getId(), groupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "GroupDTO{"
                + "id=" + id
                + ", name='" + name + '\'' + '}';
    }
}
