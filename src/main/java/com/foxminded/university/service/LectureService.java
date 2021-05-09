package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.dto.LectureDTO;

public interface LectureService extends Service<Lecture> {
    List<LectureDTO> getAllLecturesDTO();

    LectureDTO getByIdLectureDTO(Long id);

    void update(LectureDTO lectureDTO);

    void create(LectureDTO lectureDTO);
}
