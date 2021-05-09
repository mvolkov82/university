package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.service.StudentService;
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
public class StudentRestController {
    private StudentService studentService;

    @Autowired
    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<StudentDTO> getAll() {
        return studentService.getAllStudentsDTO();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(studentService.getByIdStudentDTO(id), HttpStatus.OK);
    }

    @PostMapping("/students/add")
    public ResponseEntity<StudentDTO> create(@Valid @RequestBody StudentDTO dto) {
        studentService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/students/delete")
    public ResponseEntity<Void> delete(@RequestBody StudentDTO dto) {
        studentService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/students/restore")
    public ResponseEntity<Void> restore(@RequestBody StudentDTO dto) {
        studentService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/students/edit")
    public ResponseEntity<StudentDTO> update(@Valid @RequestBody StudentDTO dto) {
        studentService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
