package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.AuditoriumDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.AuditoriumService;
import com.foxminded.university.service.DepartmentService;
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
@RequestMapping("/auditoriums")
public class AuditoriumController {

    private static final String REDIRECT_TO_ALL_AUDITORIUMS = "redirect:/auditoriums";
    private static final String REDIRECT_TO_AUDITORIUM = "redirect:/auditoriums/%s";
    private static final String VIEW_AUDITORIUM_EDIT = "auditorium/auditorium-edit";
    private static final String VIEW_A_AUDITORIUM = "auditorium/auditorium";
    private static final String VIEW_ALL_AUDITORIUMS = "auditorium/auditoriums";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_ATTRIBUTE = "fail.auditorium.operation";

    private MessageSource messageSource;
    private AuditoriumService auditoriumService;
    private DepartmentService departmentService;


    @Autowired
    public AuditoriumController(MessageSource messageSource, AuditoriumService auditoriumService, DepartmentService departmentService) {
        this.messageSource = messageSource;
        this.auditoriumService = auditoriumService;
        this.departmentService = departmentService;
    }


    @GetMapping()
    public String getAll(Model model) {
        List<AuditoriumDTO> auditoriums = auditoriumService.getAllAuditoriumsDTO();
        model.addAttribute("auditoriums", auditoriums);
        return VIEW_ALL_AUDITORIUMS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        try {
            AuditoriumDTO auditorium = auditoriumService.getByIdAuditoriumDTO(id);
            model.addAttribute("auditorium", auditorium);
        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("auditorium.not.found", new String[]{String.valueOf(id)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_AUDITORIUM;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        AuditoriumDTO auditoriumDTO;
        if (id == null) {
            auditoriumDTO = new AuditoriumDTO();
        } else {
            auditoriumDTO = auditoriumService.getByIdAuditoriumDTO(id);
        }
        model.addAttribute("auditorium", auditoriumDTO);
        model.addAttribute("departments", departmentService.getAllNotDeletedDepartmentDTO());

        return VIEW_AUDITORIUM_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditAuditorium(@Valid @ModelAttribute("auditorium") AuditoriumDTO auditoriumDTO,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        String redirectURL = VIEW_AUDITORIUM_EDIT;
        String message;

        if (!bindingResult.hasErrors()) {
            try {
                if (auditoriumDTO.getId() != null) {
                    auditoriumService.update(auditoriumDTO);
                    message = createMessage("success.auditorium.update.template", auditoriumDTO);
                    redirectURL = format(REDIRECT_TO_AUDITORIUM, auditoriumDTO.getId());
                } else {
                    auditoriumService.create(auditoriumDTO);
                    message = createMessage("success.auditorium.create.template", auditoriumDTO);
                    redirectURL = REDIRECT_TO_ALL_AUDITORIUMS;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_ATTRIBUTE, auditoriumDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_AUDITORIUMS;
            }
        }
        model.addAttribute("departments", departmentService.getAllNotDeletedDepartmentDTO());
        return redirectURL;
    }

    @PostMapping("/delete")
    public String deleteAuditorium(AuditoriumDTO auditoriumDTO, RedirectAttributes redirectAttributes) {
        String message;
        try {
            auditoriumService.markDeleted(auditoriumDTO.getId(), true);
            message = createMessage("success.auditorium.delete.template", auditoriumDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            message = createMessage(FAIL_ATTRIBUTE, auditoriumDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_AUDITORIUMS;
    }

    @PostMapping("/restore")
    public String restoreAuditorium(AuditoriumDTO auditoriumDTO, RedirectAttributes redirectAttributes) {
        try {
            auditoriumService.markDeleted(auditoriumDTO.getId(), false);
            String message = createMessage("success.auditorium.restore.template", auditoriumDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_ATTRIBUTE, auditoriumDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_AUDITORIUMS;
    }

    private String createMessage(String template, AuditoriumDTO auditoriumDTO) {
        return messageSource.getMessage(template, new String[]{buildAuditoriumTitle(auditoriumDTO)}, LocaleContextHolder.getLocale());
    }

    private String buildAuditoriumTitle(AuditoriumDTO auditoriumDTO) {
        return auditoriumDTO.getName();
    }
}


