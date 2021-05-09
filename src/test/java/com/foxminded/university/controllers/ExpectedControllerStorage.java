package com.foxminded.university.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.model.dto.StudentDTO;

public class ExpectedControllerStorage {
    public List<GroupDTO> getExpectedGroupDTOList() {
        List<GroupDTO> expectedList = new ArrayList<>();
        expectedList.add(new GroupDTO(1L, "GR-1"));
        expectedList.add(new GroupDTO(2L, "GR-2"));
        expectedList.add(new GroupDTO(3L, "GR-3"));
        return expectedList;
    }

    public Group getExpectedGroup() {
        return new Group(1L, "AA-11");
    }

    public GroupDTO getExpectedGroupDTO() {
        Group group = getExpectedGroup();
        return new GroupDTO(group.getId(), group.getName());
    }

    public Group getExpectedGroupForDelete() {
        return new Group(3L, "GR-3");
    }

    public Group getExpectedGroupForRestore() {
        Group group = getExpectedGroupForDelete();
        group.setDeleted(true);
        return group;
    }

    public GroupDTO getNewGroupDTO() {
        return new GroupDTO(null, "GR-4");
    }

    public StudentDTO getNewStudentDTO() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("NewFirstName");
        studentDTO.setLastName("NewLastName");
        studentDTO.setBirthDay(LocalDate.of(2021, 01, 01));
        return studentDTO;
    }

    public List<StudentDTO> getExpectedStudentDTOList() {
        List<StudentDTO> expectedList = new ArrayList<>();
        expectedList.add(new StudentDTO(1L,
                "Ivan",
                "Ivanov",
                LocalDate.of(2020, 12, 31),
                "g. Ivanovo ul.Pushkina 1",
                "+71234567890",
                "ivan@gmail.com",
                1L,
                "GR-1",
                LocalDate.of(2020, 01, 01)
        ));

        expectedList.add(new StudentDTO(2L,
                "Petr",
                "Petrov",
                LocalDate.of(2020, 11, 30),
                "g. Habarovsk ul.Lermontova 1",
                "+72234567890",
                "petr@mail.ru",
                1L,
                "GR-1",
                LocalDate.of(2020, 01, 01)
        ));

        expectedList.add(new StudentDTO(3L,
                "Elena",
                "Smirnova",
                LocalDate.of(2020, 10, 19),
                "g. Bobruysk ul.Nekrasova 4",
                "+73234567890",
                "smirnova.e@yandex.ru",
                2L,
                "GR-2",
                LocalDate.of(2020, 01, 01)
        ));
        return expectedList;
    }

    public StudentDTO getExpectedStudentDTO() {
        return new StudentDTO(1L,
                "Ivan",
                "Ivanov",
                LocalDate.of(2020, 12, 31),
                "g. Ivanovo ul.Pushkina 1",
                "+71234567890",
                "ivan@gmail.com",
                1L,
                "GR-1",
                LocalDate.of(2020, 01, 01)
        );
    }
}
