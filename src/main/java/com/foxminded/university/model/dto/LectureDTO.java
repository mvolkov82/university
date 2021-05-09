package com.foxminded.university.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

public class LectureDTO {
    private Long id;

    @Min(value = 0L, message = "The value must be positive")
    private int num;

    @NotEmpty(message = "Name can't be empty")
    private String name;

    @NotNull(message = "Start time can't be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime start;

    @NotNull(message = "Finish time can't be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime finish;

    public LectureDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getFinish() {
        return finish;
    }

    public void setFinish(LocalTime finish) {
        this.finish = finish;
    }
}
