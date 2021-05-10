package com.foxminded.university.controllers;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.model.dto.TimetableItemDTO;
import com.foxminded.university.model.dto.TimetableSearchingFilterDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.LogicalException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.AuditoriumService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.TimetableItemService;
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
@RequestMapping("/timetables")
public class TimetableController {
    private static final String REDIRECT_TO_ALL_TIMETABLES = "redirect:/timetables";
    private static final String REDIRECT_TO_TIMETABLE = "redirect:/timetables/%s";
    private static final String REDIRECT_TO_TIMETABLE_GROUPS = "redirect:/timetables/groups-edit/?id=%s";
    private static final String REDIRECT_TO_FILTERED_TIMETABLE = "redirect:/timetables/filter/?from=%s&to=%s&groupId=%s&teacherId=%s";
    private static final String VIEW_ALL_TIMETABLES = "timetable/timetables";
    private static final String VIEW_A_TIMETABLE = "timetable/timetable-item";
    private static final String VIEW_TIMETABLE_EDIT = "timetable/timetable-edit";
    private static final String VIEW_TIMETABLE_GROUPS_EDIT = "timetable/timetable-groups-edit";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_TEMPLATE = "fail.timetable.operation";
    private static final String GROUP_IS_ALREADY_BUSY_TEMPLATE = "fail.timetable.operation.group_is_busy";
    private static final String GROUPS_NOT_EMPTY_TEMPLATE = "fail.timetable.delete.groups_not_empty";

    private GroupService groupService;
    private MessageSource messageSource;
    private TeacherService teacherService;
    private SubjectService subjectService;
    private LectureService lectureService;
    private AuditoriumService auditoriumService;
    private TimetableItemService timetableItemService;

    public TimetableController(TimetableItemService timetableItemService, TeacherService teacherService, GroupService groupService, AuditoriumService auditoriumService, SubjectService subjectService, LectureService lectureService, MessageSource messageSource) {
        this.timetableItemService = timetableItemService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.auditoriumService = auditoriumService;
        this.subjectService = subjectService;
        this.lectureService = lectureService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String getAll(Model model) {
        List<TimetableItemDTO> dto = timetableItemService.getAllTimetableDTOS();
        model.addAttribute("timetables", dto);
        TimetableSearchingFilterDTO filterDTO = new TimetableSearchingFilterDTO();
        model.addAttribute("filter", filterDTO);
        model.addAttribute("teachers", teacherService.getAllNotDeletedDTOS());
        model.addAttribute("groups", groupService.getAllNotDeletedDTOS());
        return VIEW_ALL_TIMETABLES;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") long id, Model model) {
        try {
            TimetableItemDTO dto = timetableItemService.getByIdTimeTableItemDTO(id);
            model.addAttribute("timetable_item", dto);

        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("timetable_item.not.found",
                    new String[]{String.valueOf(id)},
                    LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_A_TIMETABLE;
    }

    @GetMapping("/teacher")
    public String getTimetableForTeacher(@RequestParam(name = "id") Long teacherId,
                                         @RequestParam(name = "from") String from,
                                         @RequestParam(name = "to") String to,
                                         Model model) {
        try {
            List<TimetableItemDTO> dto = timetableItemService.getAllForTeacher(
                    LocalDate.parse(from),
                    LocalDate.parse(to),
                    teacherId
            );
            model.addAttribute("timetables", dto);

        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("timetable_item.not.found", new String[]{String.valueOf(teacherId)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_ALL_TIMETABLES;
    }

    @GetMapping("/group")
    public String getTimetableForGroup(@RequestParam(name = "id") Long groupId,
                                       @RequestParam(name = "from") String from,
                                       @RequestParam(name = "to") String to,
                                       Model model) {
        try {
            List<TimetableItemDTO> dto = timetableItemService.getAllForGroup(
                    LocalDate.parse(from),
                    LocalDate.parse(to),
                    groupId
            );
            model.addAttribute("timetables", dto);

        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("timetable_item.not.found", new String[]{String.valueOf(groupId)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_ALL_TIMETABLES;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        TimetableItemDTO itemDTO;
        if (id == null) {
            itemDTO = new TimetableItemDTO();
        } else {
            itemDTO = timetableItemService.getByIdTimeTableItemDTO(id);
        }
        model.addAttribute("timetable_item", itemDTO);
        model.addAttribute("auditoriums", auditoriumService.getAllNotDeletedAuditoriumsDTO());
        model.addAttribute("subjects", subjectService.getAllSubjectsDTO());
        model.addAttribute("teachers", teacherService.getAllNotDeletedDTOS());
        model.addAttribute("lectures", lectureService.getAllLecturesDTO());
        return VIEW_TIMETABLE_EDIT;
    }

    @GetMapping("/groups-edit")
    public String getEditSubjectsPage(Long id, Model model) {
        try {
            TimetableItemDTO dto = timetableItemService.getByIdTimeTableItemDTO(id);
            model.addAttribute("timetable_item", dto);
            Set<GroupDTO> attachedGroups = dto.getGroups();
            model.addAttribute("groups", groupService.findGroupDTOSNotInListAndNotDeleted(attachedGroups));
        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("timetable_item.not.found",
                    new String[]{String.valueOf(id)},
                    LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }
        return VIEW_TIMETABLE_GROUPS_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditTimeTableItem(@Valid @ModelAttribute("timetable_item") TimetableItemDTO dto,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        String message;
        String redirectURL = VIEW_TIMETABLE_EDIT;
        if (!bindingResult.hasErrors()) {
            try {
                if (dto.getId() != null) {
                    timetableItemService.update(dto);
                    message = createMessage("success.timetable_item.update.template", dto);
                    redirectURL = format(REDIRECT_TO_TIMETABLE, dto.getId());
                } else {
                    timetableItemService.create(dto);
                    message = createMessage("success.timetable_item.create.template", dto);
                    redirectURL = REDIRECT_TO_ALL_TIMETABLES;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_TEMPLATE, dto);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_TIMETABLES;
            }
        }

        model.addAttribute("timetables", dto);
        model.addAttribute("auditoriums", auditoriumService.getAllNotDeletedAuditoriumsDTO());
        model.addAttribute("subjects", subjectService.getAllSubjectsDTO());
        model.addAttribute("teachers", teacherService.getAllNotDeletedDTOS());
        model.addAttribute("lectures", lectureService.getAllLecturesDTO());
        return redirectURL;
    }

    @PostMapping("/add-group")
    public String addGroupToTimetableItem(@RequestParam(name = "id") String id, @RequestParam String group, RedirectAttributes redirectAttributes) {
        TimetableItemDTO timeTableItemDTO = new TimetableItemDTO();
        GroupDTO groupDTO = new GroupDTO();

        try {
            timeTableItemDTO = timetableItemService.getByIdTimeTableItemDTO(Long.valueOf(id));
            groupDTO = groupService.getByIdGroupDTO(Long.valueOf(group));
            timetableItemService.addGroupToTimetableItem(groupDTO, timeTableItemDTO);
            String message = createMessageAfterChangingGroup("success.timetable_item.add.group", groupDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, timeTableItemDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        } catch (LogicalException e) {
            String message = createLogicalExceptionMessageByAddingGroup(GROUP_IS_ALREADY_BUSY_TEMPLATE, timeTableItemDTO, groupDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }

        return format(REDIRECT_TO_TIMETABLE_GROUPS, id);
    }

    @PostMapping("/delete-group")
    public String deleteGroupFromTimetableItem(@RequestParam(name = "id") String id, @RequestParam String group, RedirectAttributes redirectAttributes) {
        TimetableItemDTO timeTableItemDTO = new TimetableItemDTO();
        GroupDTO groupDTO = new GroupDTO();

        try {
            timeTableItemDTO = timetableItemService.getByIdTimeTableItemDTO(Long.valueOf(id));
            groupDTO = groupService.getByIdGroupDTO(Long.valueOf(group));
            timetableItemService.deleteGroupFromTimetableItem(groupDTO, timeTableItemDTO);
            String message = createMessageAfterChangingGroup("success.timetable_item.delete.group", groupDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, timeTableItemDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return format(REDIRECT_TO_TIMETABLE_GROUPS, id);
    }

    @PostMapping("/delete")
    public String deleteTimetable(TimetableItemDTO timeTableItemDTO, Model model, RedirectAttributes redirectAttributes) {
        try {
            timetableItemService.markDeleted(timeTableItemDTO.getId(), true);
            String message = createMessage("success.timetable_item.delete.template", timeTableItemDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, timeTableItemDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        } catch (LogicalException e) {
            String message = messageSource.getMessage(GROUPS_NOT_EMPTY_TEMPLATE, null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_TIMETABLES;
    }

    @PostMapping("/restore")
    public String restoreTimetable(TimetableItemDTO timeTableItemDTO, RedirectAttributes redirectAttributes) {
        try {
            timetableItemService.markDeleted(timeTableItemDTO.getId(), false);
            String message = createMessage("success.timetable_item.restore.template", timeTableItemDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_TEMPLATE, timeTableItemDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_TIMETABLES;
    }

    @PostMapping("/filter")
    public String getAllFiltered(TimetableSearchingFilterDTO filter) {

        String redirect = format(REDIRECT_TO_FILTERED_TIMETABLE,
                filter.getDateFrom(),
                filter.getDateTo(),
                filter.getGroupId(),
                filter.getTeacherId()
        );
        return redirect;
    }

    @GetMapping("/filter")
    public String getTimetableWithFilter(@RequestParam(name = "from") String dateFrom,
                                         @RequestParam(name = "to") String dateTo,
                                         @RequestParam(name = "groupId") String groupId,
                                         @RequestParam(name = "teacherId") String teacherId,
                                         Model model) {

        if (dateFrom.equals("null")) {
            dateFrom = null;
        }

        if (dateTo.equals("null")) {
            dateTo = null;
        }

        if (groupId.equals("null")) {
            groupId = null;
        }

        if (teacherId.equals("null")) {
            teacherId = null;
        }

        List<TimetableItemDTO> dto = timetableItemService.getAllNotDeletedByDateAndGroupAndTeacherDTOS(
                dateFrom,
                dateTo,
                groupId,
                teacherId);
        model.addAttribute("timetables", dto);

        TimetableSearchingFilterDTO filterDTO = new TimetableSearchingFilterDTO();
        if (dateFrom != null) {
            filterDTO.setDateFrom(LocalDate.parse(dateFrom));
        }
        if (dateTo != null) {
            filterDTO.setDateTo(LocalDate.parse(dateTo));
        }
        if (groupId != null) {
            filterDTO.setGroupId(Long.valueOf(groupId));
        }
        if (teacherId != null) {
            filterDTO.setTeacherId(Long.valueOf(teacherId));
        }

        model.addAttribute("filter", filterDTO);
        model.addAttribute("teachers", teacherService.getAllNotDeletedDTOS());
        model.addAttribute("groups", groupService.getAllNotDeletedDTOS());

        return VIEW_ALL_TIMETABLES;
    }

    private String createMessage(String template, TimetableItemDTO tableItemDTO) {
        String title = buildTitle(tableItemDTO);
        return messageSource.getMessage(template, new String[]{title}, LocaleContextHolder.getLocale());
    }

    private String buildTitle(TimetableItemDTO timeTableItemDTO) {
        Lecture lecture = lectureService.getById(timeTableItemDTO.getLectureId());
        return format("№%s %s %s", lecture.getNum(), lecture.getName(), timeTableItemDTO.getDate());
    }

    private String createMessageAfterChangingGroup(String template, GroupDTO groupDTO) {
        return messageSource.getMessage(template, new String[]{groupDTO.getName()}, LocaleContextHolder.getLocale());
    }

    private String createLogicalExceptionMessageByAddingGroup(String template, TimetableItemDTO timeTableItemDTO, GroupDTO groupDTO) {
        return messageSource.getMessage(template,
                new String[]{groupDTO.getName(), buildTitle(timeTableItemDTO)},
                LocaleContextHolder.getLocale());
    }
}
