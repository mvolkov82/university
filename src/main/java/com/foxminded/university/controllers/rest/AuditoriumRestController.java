package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.AuditoriumDTO;
import com.foxminded.university.service.AuditoriumService;
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
public class AuditoriumRestController {
    private AuditoriumService auditoriumService;

    @Autowired
    public AuditoriumRestController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @GetMapping("/auditoriums")
    public List<AuditoriumDTO> getAll() {
        return auditoriumService.getAllAuditoriumsDTO();
    }

    @GetMapping("/auditoriums/not-deleted")
    public List<AuditoriumDTO> getAllNotDeleted() {
        return auditoriumService.getAllNotDeletedAuditoriumsDTO();
    }

    @GetMapping("/auditoriums/{id}")
    public ResponseEntity<AuditoriumDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(auditoriumService.getByIdAuditoriumDTO(id), HttpStatus.OK);
    }

    @PostMapping("/auditoriums/add")
    public ResponseEntity<AuditoriumDTO> create(@Valid @RequestBody AuditoriumDTO dto) {
        auditoriumService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/auditoriums/delete")
    public ResponseEntity<Void> delete(@RequestBody AuditoriumDTO dto) {
        auditoriumService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auditoriums/restore")
    public ResponseEntity<Void> restore(@RequestBody AuditoriumDTO dto) {
        auditoriumService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/auditoriums/edit")
    public ResponseEntity<AuditoriumDTO> update(@Valid @RequestBody AuditoriumDTO dto) {
        auditoriumService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
