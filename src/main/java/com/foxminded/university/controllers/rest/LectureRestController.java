package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.LectureDTO;
import com.foxminded.university.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LectureRestController {
    private LectureService lectureService;

    @Autowired
    public LectureRestController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/lectures")
    public List<LectureDTO> getAll() {
        return lectureService.getAllLecturesDTO();
    }

    @GetMapping("/lectures/{id}")
    public ResponseEntity<LectureDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(lectureService.getByIdLectureDTO(id), HttpStatus.OK);
    }

    @PostMapping("/lectures/add")
    public ResponseEntity<LectureDTO> create(@Valid @RequestBody LectureDTO dto) {
        lectureService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/lectures/edit")
    public ResponseEntity<LectureDTO> update(@Valid @RequestBody LectureDTO dto) {
        lectureService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
