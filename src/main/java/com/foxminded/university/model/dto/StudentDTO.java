package com.foxminded.university.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import com.foxminded.university.service.validator.annotation.MaxStudentsInGroup;
import org.springframework.format.annotation.DateTimeFormat;

@MaxStudentsInGroup(message = "The group is overfilled")
public class StudentDTO {
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

    private Long groupId;
    private String groupName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private boolean deleted;

    public StudentDTO() {
    }

    public StudentDTO(Long id, String firstName, String lastName, LocalDate birthDay, String address, String phone, String email, Long groupId, String groupName, LocalDate startDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.groupId = groupId;
        this.groupName = groupName;
        this.startDate = startDate;
    }

    public StudentDTO(Long id, String firstName, String lastName, LocalDate birthDay, String address, String phone, String email, Long groupId, String groupName, LocalDate startDate, boolean deleted) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.groupId = groupId;
        this.groupName = groupName;
        this.startDate = startDate;
        this.deleted = deleted;
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

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentDTO)) {
            return false;
        }

        StudentDTO that = (StudentDTO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null) {
            return false;
        }
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null) {
            return false;
        }
        return getBirthDay() != null ? getBirthDay().equals(that.getBirthDay()) : that.getBirthDay() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getBirthDay() != null ? getBirthDay().hashCode() : 0);
        return result;
    }
}
