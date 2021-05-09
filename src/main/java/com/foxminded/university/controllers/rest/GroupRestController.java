package com.foxminded.university.controllers.rest;

import javax.validation.Valid;
import java.util.List;

import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.service.GroupService;
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
public class GroupRestController {
    private GroupService groupService;

    @Autowired
    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public List<GroupDTO> getAll() {
        return groupService.getAllGroupsDTO();
    }

    @GetMapping("/groups/not-deleted")
    public List<GroupDTO> getAllNotDeleted() {
        return groupService.getAllNotDeletedDTOS();
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(groupService.getByIdGroupDTO(id), HttpStatus.OK);
    }

    @PostMapping("/groups/add")
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupDTO dto) {
        groupService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/groups/delete")
    public ResponseEntity<Void> delete(@RequestBody GroupDTO dto) {
        groupService.markDeleted(dto.getId(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/groups/restore")
    public ResponseEntity<Void> restore(@RequestBody GroupDTO dto) {
        groupService.markDeleted(dto.getId(), false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/groups/edit")
    public ResponseEntity<GroupDTO> update(@Valid @RequestBody GroupDTO dto) {
        groupService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
