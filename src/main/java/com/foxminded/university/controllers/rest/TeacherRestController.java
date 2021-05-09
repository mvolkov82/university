package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.TeacherDTO;
import com.foxminded.university.service.TeacherService;
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
public class TeacherRestController {
    private TeacherService teacherService;

    @Autowired
    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/teachers")
    public List<TeacherDTO> getAll() {
        return teacherService.getAllNotDeletedDTOS();
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<TeacherDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(teacherService.getByIdTeacherDTO(id), HttpStatus.OK);
    }

    @PostMapping("/teachers/add")
    public ResponseEntity<TeacherDTO> create(@Valid @RequestBody TeacherDTO dto) {
        teacherService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/teachers/delete")
    public ResponseEntity<Void> delete(@RequestBody TeacherDTO dto) {
        teacherService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/teachers/restore")
    public ResponseEntity<Void> restore(@RequestBody TeacherDTO dto) {
        teacherService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/teachers/edit")
    public ResponseEntity<TeacherDTO> update(@Valid @RequestBody TeacherDTO dto) {
        teacherService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
