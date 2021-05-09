package com.foxminded.university.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num")
    private int num;

    @Column(name = "name")
    private String name;

    @Column(name = "start")
    private LocalTime start;

    @Column(name = "finish")
    private LocalTime finish;

    public Lecture() {
    }

    public Lecture(Long id, int num, String name, LocalTime start, LocalTime finish) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.start = start;
        this.finish = finish;
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

    public void setFinish(LocalTime end) {
        this.finish = end;
    }

    @Override
    public String toString() {
        return "Lecture{"
                + "id=" + id
                + ", num=" + num
                + ", name='" + name + '\''
                + ", start=" + start
                + ", finish=" + finish
                + '}';
    }
}
