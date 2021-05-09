package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.TimetableItemDTO;
import com.foxminded.university.service.TimetableItemService;
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
public class TimetableRestController {
    private TimetableItemService timetableItemService;

    @Autowired
    public TimetableRestController(TimetableItemService timetableItemService) {
        this.timetableItemService = timetableItemService;
    }

    @GetMapping("/timetables")
    public List<TimetableItemDTO> getAll() {
        return timetableItemService.getAllTimetableDTOS();
    }

    @GetMapping("/timetables/not-deleted")
    public List<TimetableItemDTO> getAllNotDeleted() {
        return timetableItemService.getAllNotDeletedDTO();
    }

    @GetMapping("/timetables/{id}")
    public ResponseEntity<TimetableItemDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(timetableItemService.getByIdTimeTableItemDTO(id), HttpStatus.OK);
    }

    @PostMapping("/timetables/add")
    public ResponseEntity<TimetableItemDTO> create(@Valid @RequestBody TimetableItemDTO dto) {
        timetableItemService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/timetables/delete")
    public ResponseEntity<Void> delete(@RequestBody TimetableItemDTO dto) {
        timetableItemService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/timetables/restore")
    public ResponseEntity<Void> restore(@RequestBody TimetableItemDTO dto) {
        timetableItemService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/timetables/edit")
    public ResponseEntity<TimetableItemDTO> update(@Valid @RequestBody TimetableItemDTO dto) {
        timetableItemService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
