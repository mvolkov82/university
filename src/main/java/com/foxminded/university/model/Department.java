package com.foxminded.university.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Column(name = "deleted")
    private boolean deleted;

    public Department() {
    }

    public Department(String name, Faculty faculty) {
        this.name = name;
        this.faculty = faculty;
    }

    public Department(Long id, String name, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }

        Department that = (Department) o;

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

