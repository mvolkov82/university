package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.DepartmentDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.DepartmentService;
import com.foxminded.university.service.FacultyService;
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
@RequestMapping("/departments")
public class DepartmentController {
    private static final String REDIRECT_TO_ALL_DEPARTMENTS = "redirect:/departments";
    private static final String REDIRECT_TO_DEPARTMENT = "redirect:/departments/%s";
    private static final String VIEW_ALL_DEPARTMENTS = "department/departments";
    private static final String VIEW_A_DEPARTMENT = "department/department";
    private static final String VIEW_DEPARTMENT_EDIT = "department/department-edit";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_TEMPLATE = "fail.department.operation";

    private DepartmentService departmentService;
    private FacultyService facultyService;
    private MessageSource messageSource;

    @Autowired
    public DepartmentController(DepartmentService departmentService, FacultyService facultyService, MessageSource messageSource) {
        this.departmentService = departmentService;
        this.facultyService = facultyService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String getAll(Model model) {
        List<DepartmentDTO> departmentDTOS = departmentService.getAllDepartmentsDTOs();
        model.addAttribute("departments", departmentDTOS);
        return VIEW_ALL_DEPARTMENTS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        try {
            DepartmentDTO departmentDTO = departmentService.getByIdDepartmentDTO(id);
            model.addAttribute("department", departmentDTO);
        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("department.not.found",
                    new String[]{String.valueOf(id)},
                    LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_DEPARTMENT;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        DepartmentDTO departmentDTO;
        if (id == null) {
            departmentDTO = new DepartmentDTO();
        } else {
            departmentDTO = departmentService.getByIdDepartmentDTO(id);
        }
        model.addAttribute("department", departmentDTO);
        model.addAttribute("faculties", facultyService.getAllNotDeletedFacultyDTO());
        return VIEW_DEPARTMENT_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditDepartment(@Valid @ModelAttribute("department") DepartmentDTO departmentDTO,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        String message;
        String redirectURL = VIEW_DEPARTMENT_EDIT;
        if (!bindingResult.hasErrors()) {
            try {
                if (departmentDTO.getId() != null) {
                    departmentService.update(departmentDTO);
                    message = createMessage("success.department.update.template", departmentDTO);
                    redirectURL = format(REDIRECT_TO_DEPARTMENT, departmentDTO.getId());
                } else {
                    departmentService.create(departmentDTO);
                    message = createMessage("success.department.create.template", departmentDTO);
                    redirectURL = REDIRECT_TO_ALL_DEPARTMENTS;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_TEMPLATE, departmentDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_DEPARTMENTS;
            }
        }
        model.addAttribute("faculties", facultyService.getAllNotDeletedFacultyDTO());
        return redirectURL;

    }

    private String createMessage(String template, DepartmentDTO departmentDTO) {
        return messageSource.getMessage(template, new String[]{departmentDTO.getName()}, LocaleContextHolder.getLocale());
    }


}
