package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.DepartmentDTO;
import com.foxminded.university.service.DepartmentService;
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
public class DepartmentRestController {
    private DepartmentService departmentService;

    @Autowired
    public DepartmentRestController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/departments")
    public List<DepartmentDTO> getAll() {
        return departmentService.getAllNotDeletedDepartmentDTO();
    }

    @GetMapping("/departments/not-deleted")
    public List<DepartmentDTO> getAllNotDeleted() {
        return departmentService.getAllNotDeletedDepartmentDTO();
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(departmentService.getByIdDepartmentDTO(id), HttpStatus.OK);
    }

    @PostMapping("/departments/add")
    public ResponseEntity<DepartmentDTO> create(@Valid @RequestBody DepartmentDTO departmentDTO) {
        departmentService.create(departmentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/departments/delete")
    public ResponseEntity<Void> delete(@RequestBody DepartmentDTO dto) {
        departmentService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/departments/restore")
    public ResponseEntity<Void> restore(@RequestBody DepartmentDTO dto) {
        departmentService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/departments/edit")
    public ResponseEntity<DepartmentDTO> update(@Valid @RequestBody DepartmentDTO dto) {
        departmentService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
