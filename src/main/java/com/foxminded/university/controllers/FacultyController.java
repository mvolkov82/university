package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.FacultyDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
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
@RequestMapping("/faculties")
public class FacultyController {
    private static final String REDIRECT_TO_ALL_FACULTIES = "redirect:/faculties";
    private static final String REDIRECT_TO_FACULTY = "redirect:/faculties/%s";
    private static final String VIEW_ALL_FACULTIES = "faculty/faculties";
    private static final String VIEW_A_FACULTY = "faculty/faculty";
    private static final String VIEW_FACULTY_EDIT = "faculty/faculty-edit";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_TEMPLATE = "fail.faculty.operation";

    private MessageSource messageSource;
    private FacultyService facultyService;

    @Autowired
    public FacultyController(MessageSource messageSource, FacultyService facultyService) {
        this.messageSource = messageSource;
        this.facultyService = facultyService;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<FacultyDTO> facultyDTOS = facultyService.getAllFacultyDTO();
        model.addAttribute("faculties", facultyDTOS);
        return VIEW_ALL_FACULTIES;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        try {
            FacultyDTO facultyDTO = facultyService.getByIdFacultyDTO(id);
            model.addAttribute("faculty", facultyDTO);
        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("faculty.not.found",
                    new String[]{String.valueOf(id)},
                    LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_FACULTY;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        FacultyDTO facultyDTO;
        if (id == null) {
            facultyDTO = new FacultyDTO();
        } else {
            facultyDTO = facultyService.getByIdFacultyDTO(id);
        }
        model.addAttribute("faculty", facultyDTO);
        return VIEW_FACULTY_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditFaculty(@Valid @ModelAttribute("faculty") FacultyDTO facultyDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        String message;
        String redirectURL = VIEW_FACULTY_EDIT;
        if (!bindingResult.hasErrors()) {
            try {
                if (facultyDTO.getId() != null) {
                    facultyService.update(facultyDTO);
                    message = createMessage("success.department.update.template", facultyDTO);
                    redirectURL = format(REDIRECT_TO_FACULTY, facultyDTO.getId());
                } else {
                    facultyService.create(facultyDTO);
                    message = createMessage("success.faculty.create.template", facultyDTO);
                    redirectURL = REDIRECT_TO_ALL_FACULTIES;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_TEMPLATE, facultyDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_FACULTIES;
            }
        }
        return redirectURL;
    }

    @PostMapping("/delete")
    public String deleteFaculty(FacultyDTO facultyDTO, RedirectAttributes redirectAttributes) {
        String message;
        try {
            facultyService.markDeleted(facultyDTO.getId(), true);
            message = createMessage("success.faculty.delete.template", facultyDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            message = createMessage(FAIL_TEMPLATE, facultyDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_FACULTIES;
    }

    @PostMapping("/restore")
    public String restoreFaculty(FacultyDTO facultyDTO, RedirectAttributes redirectAttributes) {
        try {
            facultyService.markDeleted(facultyDTO.getId(), false);
            String message = createMessage("success.faculty.restore.template", facultyDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, facultyDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_FACULTIES;
    }

    private String createMessage(String template, FacultyDTO facultyDTO) {
        return messageSource.getMessage(template, new String[]{facultyDTO.getName()}, LocaleContextHolder.getLocale());
    }
}
