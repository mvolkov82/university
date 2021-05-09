package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.LectureDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.LectureService;
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
@RequestMapping("/lectures")
public class LectureController {
    private static final String REDIRECT_TO_ALL_LECTURES = "redirect:/lectures";
    private static final String REDIRECT_TO_LECTURE = "redirect:/lectures/%s";
    private static final String VIEW_LECTURE_EDIT = "lecture/lecture-edit";
    private static final String VIEW_A_LECTURE = "lecture/lecture";
    private static final String VIEW_ALL_LECTURES = "lecture/lectures";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_ATTRIBUTE = "fail.lecture.operation";

    private MessageSource messageSource;
    private LectureService lectureService;

    @Autowired
    public LectureController(MessageSource messageSource, LectureService lectureService) {
        this.messageSource = messageSource;
        this.lectureService = lectureService;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<LectureDTO> lectureDTOs = lectureService.getAllLecturesDTO();
        model.addAttribute("lectures", lectureDTOs);

        return VIEW_ALL_LECTURES;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        try {
            LectureDTO lectureDTO = lectureService.getByIdLectureDTO(id);
            model.addAttribute("lecture", lectureDTO);
        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("lecture.not.found", new String[]{String.valueOf(id)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_LECTURE;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        LectureDTO lectureDTO;
        if (id == null) {
            lectureDTO = new LectureDTO();
        } else {
            lectureDTO = lectureService.getByIdLectureDTO(id);
        }
        model.addAttribute("lecture", lectureDTO);
        return VIEW_LECTURE_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditLecture(@Valid @ModelAttribute("lecture") LectureDTO lectureDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        String redirectURL = VIEW_LECTURE_EDIT;
        String message;

        if (!bindingResult.hasErrors()) {
            try {
                if (lectureDTO.getId() != null) {
                    lectureService.update(lectureDTO);
                    message = createMessage("success.lecture.update.template", lectureDTO);
                    redirectURL = format(REDIRECT_TO_LECTURE, lectureDTO.getId());
                } else {
                    lectureService.create(lectureDTO);
                    message = createMessage("success.lecture.create.template", lectureDTO);
                    redirectURL = REDIRECT_TO_ALL_LECTURES;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_ATTRIBUTE, lectureDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_LECTURES;
            }
        }

        return redirectURL;
    }

    private String createMessage(String template, LectureDTO lectureDTO) {
        return messageSource.getMessage(template, new String[]{buildLectureTitle(lectureDTO)}, LocaleContextHolder.getLocale());
    }

    private String buildLectureTitle(LectureDTO lectureDTO) {
        String lectureTitle = format("%s %s %s %s", lectureDTO.getNum(), lectureDTO.getName(), lectureDTO.getStart(), lectureDTO.getFinish());
        return lectureTitle;
    }
}
