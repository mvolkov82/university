package com.foxminded.university.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class TeacherDTO {
    private Long id;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    private String lastName;

    @NotNull(message = "Birthday can't be empty")
    @Past(message = "Birthday can't be future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    private String address;

    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Use only + and digits")
    private String phone;

    @Email(message = "Incorrect email")
    private String email;

    private String degree;
    private boolean deleted;
    private Long departmentId;
    private String departmentName;
    private List<SubjectDTO> subjectDTOS;

    public TeacherDTO() {
    }

    public TeacherDTO(Long id,
                      @NotEmpty(message = "First name can't be empty") String firstName,
                      @NotEmpty(message = "Last name can't be empty") String lastName,
                      @NotNull(message = "Birthday can't be empty")
                      @Past(message = "Birthday can't be future") LocalDate birthDay,
                      String address,
                      @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Use only + and digits")
                              String phone,
                      @Email(message = "Incorrect email")
                              String email,
                      Long departmentId,
                      String departmentName,
                      boolean deleted) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.deleted = deleted;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<SubjectDTO> getSubjectDTOS() {
        return subjectDTOS;
    }

    public void setSubjectDTOS(List<SubjectDTO> subjectDTOS) {
        this.subjectDTOS = subjectDTOS;
    }
}
