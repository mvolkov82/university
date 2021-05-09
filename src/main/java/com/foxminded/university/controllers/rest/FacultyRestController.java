package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.FacultyDTO;
import com.foxminded.university.service.FacultyService;
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
public class FacultyRestController {
    private FacultyService facultyService;

    @Autowired
    public FacultyRestController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/faculties")
    public List<FacultyDTO> getAll() {
        return facultyService.getAllFacultyDTO();
    }

    @GetMapping("/faculties/not-deleted")
    public List<FacultyDTO> getAllNotDeleted() {
        return facultyService.getAllNotDeletedFacultyDTO();
    }

    @GetMapping("/faculties/{id}")
    public ResponseEntity<FacultyDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(facultyService.getByIdFacultyDTO(id), HttpStatus.OK);
    }

    @PostMapping("/faculties/add")
    public ResponseEntity<FacultyDTO> create(@Valid @RequestBody FacultyDTO dto) {
        facultyService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/faculties/delete")
    public ResponseEntity<Void> delete(@RequestBody FacultyDTO dto) {
        facultyService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/faculties/restore")
    public ResponseEntity<Void> restore(@RequestBody FacultyDTO dto) {
        facultyService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/faculties/edit")
    public ResponseEntity<FacultyDTO> update(@Valid @RequestBody FacultyDTO dto) {
        facultyService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
