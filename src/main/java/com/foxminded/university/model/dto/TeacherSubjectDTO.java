package com.foxminded.university.model.dto;

public class TeacherSubjectDTO {
    private Long teacherId;
    private Long subjectId;

    public TeacherSubjectDTO() {
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
