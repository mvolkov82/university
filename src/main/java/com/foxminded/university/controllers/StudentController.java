package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {
    private static final String REDIRECT_TO_ALL_STUDENTS = "redirect:/students";
    private static final String REDIRECT_TO_STUDENT = "redirect:/students/%s";
    private static final String VIEW_ALL_STUDENTS = "student/students";
    private static final String VIEW_A_STUDENT = "student/student";
    private static final String VIEW_STUDENT_EDIT = "student/student-edit";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_TEMPLATE = "fail.student.operation";

    private StudentService studentService;
    private GroupService groupService;
    private MessageSource messageSource;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService, MessageSource messageSource) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String getAll(Model model) {
        List<StudentDTO> students = studentService.getAllStudentsDTO();
        model.addAttribute("students", students);
        return VIEW_ALL_STUDENTS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") long id, Model model) {
        try {
            StudentDTO student = studentService.getByIdStudentDTO(id);
            model.addAttribute("student", student);

        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("student.not.found", new String[]{String.valueOf(id)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_STUDENT;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        StudentDTO studentDTO;
        if (id == null) {
            studentDTO = new StudentDTO();
        } else {
            studentDTO = studentService.getByIdStudentDTO(id);
        }
        model.addAttribute("student", studentDTO);
        model.addAttribute("groups", groupService.getAllNotDeletedDTOS());
        return VIEW_STUDENT_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditStudent(@Valid @ModelAttribute("student") StudentDTO studentDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        String message;
        String redirectURL = VIEW_STUDENT_EDIT;
        if (!bindingResult.hasErrors()) {
            try {
                if (studentDTO.getId() != null) {
                    studentService.update(studentDTO);
                    message = createMessage("success.student.update.template", studentDTO);
                    redirectURL = format(REDIRECT_TO_STUDENT, studentDTO.getId());
                } else {
                    studentService.create(studentDTO);
                    message = createMessage("success.student.create.template", studentDTO);
                    redirectURL = REDIRECT_TO_ALL_STUDENTS;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_TEMPLATE, studentDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_STUDENTS;
            }
        }
        model.addAttribute("groups", groupService.getAllNotDeletedDTOS());
        return redirectURL;
    }

    @PostMapping("/delete")
    public String deleteStudent(StudentDTO studentDTO, RedirectAttributes redirectAttributes) {
        try {
            studentService.markDeleted(studentDTO.getId(), true);
            String message = createMessage("success.student.delete.template", studentDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, studentDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_STUDENTS;
    }

    @PostMapping("/restore")
    public String restoreStudent(StudentDTO studentDTO, RedirectAttributes redirectAttributes) {
        try {
            studentService.markDeleted(studentDTO.getId(), false);
            String message = createMessage("success.student.restore.template", studentDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, studentDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_STUDENTS;
    }

    private String createMessage(String template, StudentDTO studentDTO) {
        String studentTitle = buildStudentTitle(studentDTO);
        return messageSource.getMessage(template, new String[]{studentTitle}, LocaleContextHolder.getLocale());
    }

    private String buildStudentTitle(StudentDTO studentDTO) {
        return format("%s %s", studentDTO.getFirstName(), studentDTO.getLastName());
    }
}
