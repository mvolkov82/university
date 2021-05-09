package com.foxminded.university.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "auditoriums")
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    public Auditorium() {
    }

    public Auditorium(Department department, String name) {
        this.department = department;
        this.name = name;
    }

    public Auditorium(Long id, Department department, String name) {
        this.id = id;
        this.department = department;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
