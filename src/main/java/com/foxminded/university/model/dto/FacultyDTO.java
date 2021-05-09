package com.foxminded.university.model.dto;

import javax.validation.constraints.NotEmpty;

public class FacultyDTO {
    private Long id;

    @NotEmpty(message = "Name can't be empty")
    private String name;

    private boolean deleted;

    public FacultyDTO() {
    }

    public FacultyDTO(Long id, String name) {
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
        if (!(o instanceof FacultyDTO)) {
            return false;
        }

        FacultyDTO that = (FacultyDTO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
