package com.foxminded.university.service.impl;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Lecture_;
import com.foxminded.university.model.dto.LectureDTO;
import com.foxminded.university.repository.LectureRepository;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.LectureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LectureServiceImpl implements LectureService {
    public static final Logger LOGGER = LoggerFactory.getLogger(LectureServiceImpl.class);
    private LectureRepository lectureRepository;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    @Transactional
    public Lecture create(Lecture lecture) {
        LOGGER.debug("create() [group={}]", lecture);
        try {
            lectureRepository.save(lecture);
        } catch (DataAccessException e) {
            String message = format("Operation for lecture %s failed.", lecture);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
        return lecture;
    }

    @Transactional
    public void create(LectureDTO lectureDTO) {
        Lecture lecture = convertFromDto(lectureDTO);
        create(lecture);
    }

    @Override
    public List<Lecture> getAll() {
        return lectureRepository.findAll();
    }

    public List<LectureDTO> getAllLecturesDTO() {
        Sort numSort = Sort.by(Lecture_.NUM);
        Sort timeSort = Sort.by(Lecture_.START);
        Sort sort = numSort.ascending().and(timeSort).ascending();
        List<Lecture> lectures = lectureRepository.findAll(sort);
        return convertToDTO(lectures);
    }

    @Override
    public LectureDTO getByIdLectureDTO(Long id) {
        Lecture lecture = getById(id);
        return convertToDto(lecture);
    }

    @Override
    public Lecture getById(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Lecture with id '%s' not found", id)));
    }

    @Override
    @Transactional
    public void update(Lecture lecture) {
        try {
            lectureRepository.save(lecture);
        } catch (DataAccessException e) {
            String message = format("Operation for lecture %s failed.", lecture);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Transactional
    public void update(LectureDTO lectureDTO) {
        Lecture lecture = convertFromDto(lectureDTO);
        update(lecture);
    }

    private Lecture convertFromDto(LectureDTO lectureDTO) {
        Lecture lecture;

        if (lectureDTO.getId() != null) {
            lecture = getById(lectureDTO.getId());
        } else {
            lecture = new Lecture();
        }

        lecture.setNum(lectureDTO.getNum());
        lecture.setName(lectureDTO.getName());
        lecture.setStart(lectureDTO.getStart());
        lecture.setFinish(lectureDTO.getFinish());

        return lecture;
    }

    private LectureDTO convertToDto(Lecture lecture) {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(lecture.getId());
        lectureDTO.setNum(lecture.getNum());
        lectureDTO.setName(lecture.getName());
        lectureDTO.setStart(lecture.getStart());
        lectureDTO.setFinish(lecture.getFinish());
        return lectureDTO;
    }

    private List<LectureDTO> convertToDTO(List<Lecture> lectures) {
        return lectures.stream()
                .map(s -> convertToDto(s))
                .collect(toList());
    }
}
