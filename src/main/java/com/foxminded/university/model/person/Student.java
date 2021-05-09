package com.foxminded.university.model.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDate;

import com.foxminded.university.model.Group;


@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "person_id")
public class Student extends Person {

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "deleted")
    private boolean deleted;

    public Student() {
    }

    public Student(
            Long id,
            String firstName,
            String lastName,
            LocalDate birthDay,
            String address,
            String phone,
            String email,
            Group group,
            LocalDate startDate) {
        super(id, firstName, lastName, birthDay, address, phone, email);
        this.group = group;
        this.startDate = startDate;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Student{"
                + "group=" + group
                + ", startDate=" + startDate
                + "} " + super.toString();
    }


}
