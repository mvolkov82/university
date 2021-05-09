package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.service.SubjectService;
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
public class SubjectRestController {
    private SubjectService subjectService;

    @Autowired
    public SubjectRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public List<SubjectDTO> getAll() {
        return subjectService.getAllSubjectsDTO();
    }

    @GetMapping("/subjects/not-deleted")
    public List<SubjectDTO> getAllNotDeleted() {
        return subjectService.getAllSubjectsDTO();
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<SubjectDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(subjectService.getByIdSubjectDTO(id), HttpStatus.OK);
    }

    @PostMapping("/subjects/add")
    public ResponseEntity<SubjectDTO> create(@Valid @RequestBody SubjectDTO dto) {
        subjectService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/subjects/delete")
    public ResponseEntity<Void> delete(@RequestBody SubjectDTO dto) {
        subjectService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/subjects/restore")
    public ResponseEntity<Void> restore(@RequestBody SubjectDTO dto) {
        subjectService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/subjects/edit")
    public ResponseEntity<SubjectDTO> update(@Valid @RequestBody SubjectDTO dto) {
        subjectService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
