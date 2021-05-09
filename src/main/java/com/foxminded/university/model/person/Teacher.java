package com.foxminded.university.model.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Set;

import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Subject;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "person_id")
public class Teacher extends Person {
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "deleted")
    private boolean deleted;

    @ManyToMany
    @JoinTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> linkedSubjects;

    public Teacher() {
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
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

    public Set<Subject> getLinkedSubjects() {
        return linkedSubjects;
    }

    public void setLinkedSubjects(Set<Subject> linkedSubjects) {
        this.linkedSubjects = linkedSubjects;
    }
}
