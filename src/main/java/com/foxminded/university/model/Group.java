package com.foxminded.university.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "deleted")
    private boolean deleted;

    public Group() {
    }

    public Group(Long id, String name, Department department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Group(Long id, String name, Department department, boolean deleted) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.deleted = deleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Group{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", department=" + department
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        Group group = (Group) o;
        return getId().equals(group.getId()) && getName().equals(group.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
