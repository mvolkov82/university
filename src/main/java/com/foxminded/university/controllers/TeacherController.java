package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.model.dto.TeacherDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.DegreeService;
import com.foxminded.university.service.DepartmentService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private static final String REDIRECT_TO_ALL_TEACHERS = "redirect:/teachers";
    private static final String REDIRECT_TO_TEACHER = "redirect:/teachers/%s";
    private static final String REDIRECT_TO_TEACHER_SUBJECTS = "redirect:/teachers/edit-subjects/?id=%s";
    private static final String VIEW_ALL_TEACHERS = "teacher/teachers";
    private static final String VIEW_A_TEACHER = "teacher/teacher";
    private static final String VIEW_TEACHER_EDIT = "teacher/teacher-edit";
    private static final String VIEW_TEACHER_SUBJECTS_EDIT = "teacher/teacher-subjects-edit";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_TEMPLATE = "fail.teacher.operation";

    private TeacherService teacherService;
    private DepartmentService departmentService;
    private SubjectService subjectService;
    private MessageSource messageSource;
    private DegreeService degreeService;

    @Autowired
    public TeacherController(TeacherService teacherService, DepartmentService departmentService, SubjectService subjectService, MessageSource messageSource, DegreeService degreeService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
        this.messageSource = messageSource;
        this.degreeService = degreeService;
    }

    @GetMapping
    public String getAll(Model model) {
        List<TeacherDTO> teachersDTO = teacherService.getAllTeachersDTO();
        model.addAttribute("teachers", teachersDTO);
        return VIEW_ALL_TEACHERS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") long id, Model model) {
        try {
            TeacherDTO teacherDTO = teacherService.getByIdTeacherDTO(id);
            model.addAttribute("teacher", teacherDTO);

        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("teacher.not.found", new String[]{String.valueOf(id)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_TEACHER;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        TeacherDTO teacherDTO;
        if (id == null) {
            teacherDTO = new TeacherDTO();
        } else {
            teacherDTO = teacherService.getByIdTeacherDTO(id);
        }
        model.addAttribute("teacher", teacherDTO);
        model.addAttribute("departments", departmentService.getAllNotDeletedDepartmentDTO());
        model.addAttribute("degree", degreeService.getDegreeAsList());
        model.addAttribute("subjects", subjectService.getAllSubjectsDTO());
        return VIEW_TEACHER_EDIT;
    }

    @GetMapping("/edit-subjects")
    public String getEditSubjectsPage(Long id, Model model) {
        TeacherDTO teacherDTO = teacherService.getByIdTeacherDTO(id);
        model.addAttribute("teacher", teacherDTO);
        model.addAttribute("departments", departmentService.getAllNotDeletedDepartmentDTO());
        model.addAttribute("subjects", subjectService.getNotTeachersSubjectDTOs(teacherDTO));
        return VIEW_TEACHER_SUBJECTS_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditTeacher(@Valid @ModelAttribute("teacher") TeacherDTO teacherDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        String message;
        String redirectURL = VIEW_TEACHER_EDIT;
        if (!bindingResult.hasErrors()) {
            try {
                if (teacherDTO.getId() != null) {
                    teacherService.update(teacherDTO);
                    message = createMessage("success.teacher.update.template", teacherDTO);
                    redirectURL = format(REDIRECT_TO_TEACHER, teacherDTO.getId());
                } else {
                    teacherService.create(teacherDTO);
                    message = createMessage("success.teacher.create.template", teacherDTO);
                    redirectURL = REDIRECT_TO_ALL_TEACHERS;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_TEMPLATE, teacherDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_TEACHERS;
            }
        }
        model.addAttribute("departments", departmentService.getAllNotDeletedDepartmentDTO());
        return redirectURL;
    }


    @PostMapping("/delete-subject")
    public String deleteSubjectFromTeacher(@RequestParam(name = "id") Long id,
                                           @RequestParam(name = "subj") Long subject,
                                           RedirectAttributes redirectAttributes) {
        TeacherDTO teacherDTO;
        SubjectDTO subjectDTO;

        try {
            teacherDTO = teacherService.getByIdTeacherDTO(id);
            subjectDTO = subjectService.getByIdSubjectDTO(subject);
            teacherService.deleteSubjectFromTeacher(subjectDTO, teacherDTO);
            String message = createMessageForSubject("success.teacher.delete.subject", subjectDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);

        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, teacherService.getByIdTeacherDTO(id));
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return format(REDIRECT_TO_TEACHER_SUBJECTS, id);
    }

    @PostMapping("/add-subject")
    public String addSubjectToTeacher(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "subj") Long subject,
                                      RedirectAttributes redirectAttributes) {
        TeacherDTO teacherDTO;
        SubjectDTO subjectDTO;

        try {
            teacherDTO = teacherService.getByIdTeacherDTO(id);
            subjectDTO = subjectService.getByIdSubjectDTO(subject);
            teacherService.addSubjectToTeacher(subjectDTO, teacherDTO);
            String message = createMessageForSubject("success.teacher.add.subject", subjectDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, teacherService.getByIdTeacherDTO(id));
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return format(REDIRECT_TO_TEACHER_SUBJECTS, id);
    }

    @PostMapping("/delete")
    public String deleteTeacher(TeacherDTO teacherDTO, RedirectAttributes redirectAttributes) {
        try {
            teacherService.markDeleted(teacherDTO.getId(), true);
            String message = createMessage("success.teacher.delete.template", teacherDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, teacherDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_TEACHERS;
    }

    @PostMapping("/restore")
    public String restoreTeacher(TeacherDTO teacherDTO, RedirectAttributes redirectAttributes) {
        try {
            teacherService.markDeleted(teacherDTO.getId(), false);
            String message = createMessage("success.teacher.restore.template", teacherDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, teacherDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_TEACHERS;
    }

    private String createMessage(String template, TeacherDTO teacherDTO) {
        String teacherTitle = buildTeacherTitle(teacherDTO);
        return messageSource.getMessage(template, new String[]{teacherTitle}, LocaleContextHolder.getLocale());
    }

    private String buildTeacherTitle(TeacherDTO teacherDTO) {
        return format("%s %s %s", teacherDTO.getDegree(), teacherDTO.getFirstName(), teacherDTO.getLastName());
    }

    private String createMessageForSubject(String template, SubjectDTO subjectDTO) {
        return messageSource.getMessage(template, new String[]{subjectDTO.getName()}, LocaleContextHolder.getLocale());
    }
}

