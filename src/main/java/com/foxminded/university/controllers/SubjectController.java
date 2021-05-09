package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.SubjectService;
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
@RequestMapping("/subjects")
public class SubjectController {
    private static final String REDIRECT_TO_ALL_SUBJECTS = "redirect:/subjects";
    private static final String REDIRECT_TO_SUBJECT = "redirect:/subjects/%s";
    private static final String VIEW_SUBJECT_EDIT = "subject/subject-edit";
    private static final String VIEW_A_SUBJECT = "subject/subject";
    private static final String VIEW_ALL_SUBJECTS = "subject/subjects";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_ATTRIBUTE = "fail.subject.operation";

    private MessageSource messageSource;
    private SubjectService subjectservice;

    @Autowired
    public SubjectController(SubjectService subjectservice, MessageSource messageSource) {
        this.subjectservice = subjectservice;
        this.messageSource = messageSource;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<SubjectDTO> subjects = subjectservice.getAllSubjectsDTO();
        model.addAttribute("subjects", subjects);

        return VIEW_ALL_SUBJECTS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        try {
            SubjectDTO subjectDTO = subjectservice.getByIdSubjectDTO(id);
            model.addAttribute("subject", subjectDTO);
        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("subject.not.found", new String[]{String.valueOf(id)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_SUBJECT;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        SubjectDTO subjectDTO;
        if (id == null) {
            subjectDTO = new SubjectDTO();
        } else {
            subjectDTO = subjectservice.getByIdSubjectDTO(id);
        }
        model.addAttribute("subject", subjectDTO);

        return VIEW_SUBJECT_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditSubject(@Valid @ModelAttribute("subject") SubjectDTO subjectDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        String redirectURL = VIEW_SUBJECT_EDIT;
        String message;

        if (!bindingResult.hasErrors()) {
            try {
                if (subjectDTO.getId() != null) {
                    subjectservice.update(subjectDTO);
                    message = createMessage("success.subject.update.template", subjectDTO);
                    redirectURL = format(REDIRECT_TO_SUBJECT, subjectDTO.getId());
                } else {
                    subjectservice.create(subjectDTO);
                    message = createMessage("success.subject.create.template", subjectDTO);
                    redirectURL = REDIRECT_TO_ALL_SUBJECTS;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_ATTRIBUTE, subjectDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_SUBJECTS;
            }
        }

        return redirectURL;
    }

    @PostMapping("/delete")
    public String deleteSubject(SubjectDTO subjectDTO, RedirectAttributes redirectAttributes) {
        String message;
        try {
            subjectservice.markDeleted(subjectDTO.getId(), true);
            message = createMessage("success.subject.delete.template", subjectDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            message = createMessage(FAIL_ATTRIBUTE, subjectDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_SUBJECTS;
    }

    @PostMapping("/restore")
    public String restoreSubject(SubjectDTO subjectDTO, RedirectAttributes redirectAttributes) {
        try {
            subjectservice.markDeleted(subjectDTO.getId(), false);
            String message = createMessage("success.subject.restore.template", subjectDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_ATTRIBUTE, subjectDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_SUBJECTS;
    }

    private String createMessage(String template, SubjectDTO subjectDTO) {
        return messageSource.getMessage(template, new String[]{buildSubjectTitle(subjectDTO)}, LocaleContextHolder.getLocale());
    }

    private String buildSubjectTitle(SubjectDTO subjectDTO) {
        return subjectDTO.getName();
    }
}
