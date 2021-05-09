package com.foxminded.university.controllers;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.DepartmentService;
import com.foxminded.university.service.GroupService;
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
@RequestMapping("/groups")
public class GroupController {
    private static final String REDIRECT_TO_ALL_GROUPS = "redirect:/groups";
    private static final String REDIRECT_TO_GROUP = "redirect:/groups/%s";
    private static final String VIEW_GROUP_EDIT = "group/group-edit";
    private static final String VIEW_A_GROUP = "group/group";
    private static final String VIEW_ALL_GROUPS = "group/groups";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String FAIL_ATTRIBUTE = "fail.group.operation";

    private MessageSource messageSource;
    private GroupService groupService;
    private DepartmentService departmentService;

    @Autowired
    public GroupController(MessageSource messageSource, GroupService groupService, DepartmentService departmentService) {
        this.messageSource = messageSource;
        this.groupService = groupService;
        this.departmentService = departmentService;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<GroupDTO> groups = groupService.getAllGroupsDTO();
        model.addAttribute("groups", groups);

        return VIEW_ALL_GROUPS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        try {
            GroupDTO group = groupService.getByIdGroupDTO(id);
            model.addAttribute("group", group);
        } catch (EntityNotFoundException e) {
            String message = messageSource.getMessage("group.not.found", new String[]{String.valueOf(id)}, LocaleContextHolder.getLocale());
            model.addAttribute(ERROR_ATTRIBUTE, message);
        }

        return VIEW_A_GROUP;
    }

    @GetMapping("/edit")
    public String getEditPage(Long id, Model model) {
        GroupDTO groupDTO;
        if (id == null) {
            groupDTO = new GroupDTO();
        } else {
            groupDTO = groupService.getByIdGroupDTO(id);
        }
        model.addAttribute("group", groupDTO);
        model.addAttribute("departments", departmentService.getAllNotDeletedDepartmentDTO());

        return VIEW_GROUP_EDIT;
    }

    @PostMapping("/edit")
    public String createOrEditGroup(@Valid @ModelAttribute("group") GroupDTO groupDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        String redirectURL = VIEW_GROUP_EDIT;
        String message;

        if (!bindingResult.hasErrors()) {
            try {
                if (groupDTO.getId() != null) {
                    groupService.update(groupDTO);
                    message = createMessage("success.group.update.template", groupDTO);
                    redirectURL = format(REDIRECT_TO_GROUP, groupDTO.getId());
                } else {
                    groupService.create(groupDTO);
                    message = createMessage("success.group.create.template", groupDTO);
                    redirectURL = REDIRECT_TO_ALL_GROUPS;
                }
                redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
            } catch (QueryNotExecuteException e) {
                message = createMessage(FAIL_ATTRIBUTE, groupDTO);
                redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
                redirectURL = REDIRECT_TO_ALL_GROUPS;
            }
        }

        return redirectURL;
    }

    @PostMapping("/delete")
    public String deleteGroup(GroupDTO groupDTO, RedirectAttributes redirectAttributes) {
        String message;

        try {
            groupService.markDeleted(groupDTO.getId(), true);
            message = createMessage("success.group.delete.template", groupDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            message = createMessage(FAIL_ATTRIBUTE, groupDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_GROUPS;
    }

    @PostMapping("/restore")
    public String restoreGroup(GroupDTO groupDTO, RedirectAttributes redirectAttributes) {
        try {
            groupService.markDeleted(groupDTO.getId(), false);
            String message = createMessage("success.group.restore.template", groupDTO);
            redirectAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, message);
        } catch (QueryNotExecuteException e) {
            String message = createMessage(FAIL_ATTRIBUTE, groupDTO);
            redirectAttributes.addFlashAttribute(ERROR_ATTRIBUTE, message);
        }
        return REDIRECT_TO_ALL_GROUPS;

    }

    private String createMessage(String template, GroupDTO groupDTO) {
        return messageSource.getMessage(template, new String[]{buildGroupTitle(groupDTO)}, LocaleContextHolder.getLocale());
    }

    private String buildGroupTitle(GroupDTO groupDTO) {
        return groupDTO.getName();
    }
}
