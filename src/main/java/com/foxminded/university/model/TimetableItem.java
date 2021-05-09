package com.foxminded.university.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

import com.foxminded.university.model.person.Teacher;

@Entity
@Table(name = "timetable_items")
public class TimetableItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToOne
    @JoinColumn(name = "auditorium_id")
    private Auditorium auditorium;

    @ManyToMany
    @JoinTable(
            name = "timetable_items_groups",
            joinColumns = @JoinColumn(name = "timetable_item_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> groups;

    @Column(name = "deleted")
    private boolean deleted;

    public TimetableItem() {
    }

    public TimetableItem(Long id, LocalDate date, Lecture lecture, Teacher teacher, Subject subject, Auditorium auditorium, Set<Group> groups, boolean deleted) {
        this.id = id;
        this.date = date;
        this.lecture = lecture;
        this.teacher = teacher;
        this.subject = subject;
        this.auditorium = auditorium;
        this.groups = groups;
        this.deleted = deleted;
    }

    public TimetableItem(Long id, LocalDate date, Lecture lecture, Teacher teacher, Subject subject, Auditorium auditorium, boolean deleted) {
        this.id = id;
        this.date = date;
        this.lecture = lecture;
        this.teacher = teacher;
        this.subject = subject;
        this.auditorium = auditorium;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "TimetableItem{"
                + "id=" + id
                + ", date=" + date
                + ", lecture=" + lecture
                + ", teacher=" + teacher
                + ", subject=" + subject
                + ", auditorium=" + auditorium
                + ", deleted=" + deleted
                + '}';
    }
}
