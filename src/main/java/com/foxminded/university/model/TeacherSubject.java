package com.foxminded.university.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

import com.foxminded.university.model.person.Teacher;

@Table(name = "teacher_subjects")
public class TeacherSubject implements Serializable {

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private Teacher teacherId;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "subject_id")
    private Subject subjectId;

    public TeacherSubject() {
    }

    public TeacherSubject(Teacher teacherId, Subject subjectId) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
    }

    public Subject getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subject subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeacherSubject)) {
            return false;
        }

        TeacherSubject that = (TeacherSubject) o;

        if (!getTeacherId().equals(that.getTeacherId())) {
            return false;
        }
        return getSubjectId().equals(that.getSubjectId());
    }

    @Override
    public int hashCode() {
        int result = getTeacherId().hashCode();
        result = 31 * result + getSubjectId().hashCode();
        return result;
    }
}
