package com.foxminded.university.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Department;
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.model.person.Student;

public class TestRepository {
    public Faculty getFaculty() {
        return new Faculty(1L, "Computer science");
    }

    public Department getDepartment() {
        Faculty testFaculty = getFaculty();
        return new Department(1L, "Programming", testFaculty);
    }

    public Group getGroup() {
        Department testDepartment = getDepartment();
        return new Group(1L, "GR-1", testDepartment);
    }

    public Group getDeletedGroup() {
        Group group = getGroup();
        group.setDeleted(true);
        return group;
    }

    public Group getNewGroupForStudent() {
        Department testDepartment = getDepartment();
        return new Group(2L, "GR-2", testDepartment);
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        Department department = getDepartment();
        groups.add(new Group(1L, "GR-1", department));
        groups.add(new Group(2L, "GR-2", department));
        groups.add(new Group(3L, "GR-3", department));
        groups.add(new Group(4L, "GR-DELETED", department, false));
        return groups;
    }

    public Student getCreatedStudent() {
        Group testGroup = getGroup();
        Student createdStudent = new Student(
                4L,
                "John",
                "Connor",
                LocalDate.of(1995, 05, 06),
                "USA California 1st street 1",
                "+1234567890",
                "john@amazon.com",
                testGroup,
                LocalDate.of(2020, 9, 1)
        );
        return createdStudent;
    }

    public Group getCreatedGroup() {
        Department testDepartment = getDepartment();
        return new Group("new test group", testDepartment);
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(
                1L,
                "Ivan",
                "Ivanov",
                LocalDate.of(2020, 12, 31),
                "g. Ivanovo ul.Pushkina 1",
                "+71234567890",
                "ivan@gmail.com",
                new Group(1L, "GR-1"),
                LocalDate.of(2019, 9, 1)));

        students.add(new Student(
                2L,
                "Petr",
                "Petrov",
                LocalDate.of(2020, 11, 30),
                "g. Habarovsk ul.Lermontova 1",
                "+72234567890",
                "petr@mail.ru",
                new Group(1L, "GR-1"),
                LocalDate.of(2019, 9, 1)));

        students.add(new Student(
                3L,
                "Elena",
                "Smirnova",
                LocalDate.of(2020, 10, 19),
                "g. Bobruysk ul.Nekrasova 4",
                "+73234567890",
                "smirnova.e@yandex.ru",
                new Group(2L, "GR-2"),
                LocalDate.of(2020, 9, 1)));

        return students;
    }

    public List<Department> getAllDepartments() {
        Faculty testFaculty = new Faculty(1L, "Computer science");
        List<Department> departments = new ArrayList<>();
        departments.add(new Department(1L, "Programming", testFaculty));
        departments.add(new Department(2L, "Security", testFaculty));
        return departments;
    }

    public Student getUpdatedStudent(Student student) {
        student.setFirstName("testFirstName");
        student.setLastName("testLastName");
        student.setBirthDay(LocalDate.of(2020, 12, 31));
        student.setAddress("testAddress");
        student.setPhone("testPhone");
        student.setEmail("test@email");
        Group newGroup = getNewGroupForStudent();
        student.setGroup(newGroup);
        student.setStartDate(LocalDate.of(2000, 01, 01));
        return student;
    }

    public Student getStudent() {
        return getAllStudents().get(0);
    }

    public Student getDeletedStudent() {
        Student student = getStudent();
        student.setDeleted(true);
        return student;
    }

    public List<GroupDTO> getNotDeletedGroupDTOList() {
        List<GroupDTO> expectedList = new ArrayList<>();
        expectedList.add(new GroupDTO(1L, "GR-1"));
        expectedList.add(new GroupDTO(2L, "GR-2"));
        expectedList.add(new GroupDTO(3L, "GR-3"));
        return expectedList;
    }

    public List<StudentDTO> getNotDeletedStudentDTOList() {
        List<StudentDTO> studentDTOs = new ArrayList<>();
        studentDTOs.add(new StudentDTO(
                1L,
                "Ivan",
                "Ivanov",
                LocalDate.of(2020, 12, 31),
                "g. Ivanovo ul.Pushkina 1",
                "+71234567890",
                "ivan@gmail.com",
                1L,
                "GR-1",
                LocalDate.of(2019, 9, 1)));

        studentDTOs.add(new StudentDTO(
                2L,
                "Petr",
                "Petrov",
                LocalDate.of(2020, 11, 30),
                "g. Habarovsk ul.Lermontova 1",
                "+72234567890",
                "petr@mail.ru",
                1L,
                "GR-1",
                LocalDate.of(2019, 9, 1)));
        return studentDTOs;
    }
}
