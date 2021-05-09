package com.foxminded.university.controllers;

import java.util.List;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.GroupService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@TestInstance(Lifecycle.PER_CLASS)
public class GroupControllerTest {
    private static final Long NONEXISTENT_GROUP_ID = 0L;
    private static final Long EXISTENT_GROUP_ID = 1L;
    private static final String ERROR_MESSAGE = format("Group with id = %s not found!", NONEXISTENT_GROUP_ID);
    private static final String URL_DELETE = "/groups/delete";
    private static final String URL_RESTORE = "/groups/restore";
    private static final String URL_CREATE_NEW_GROUP = "/groups/edit";
    private static final String URL_EDIT_GROUP = "/groups/edit/?id={id}";
    private static final String URL_GET_ALL_GROUPS = "/groups";
    private static final String REDIRECT_TO_ALL_GROUPS = "redirect:/groups";

    private ExpectedControllerStorage expectedStorage = new ExpectedControllerStorage();
    private MockMvc mockMvc;

    @Mock
    private GroupService mockGroupService;

    @Mock
    private MessageSource mockMessageSource;

    @InjectMocks
    private GroupController groupController;

    @BeforeAll
    public void setUpBeforeClass() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void shouldCallViewAndModelAllGroupsAndExpectedValues() throws Exception {
        List<GroupDTO> expectedGroupDTO = expectedStorage.getExpectedGroupDTOList();
        when(mockGroupService.getAllGroupsDTO()).thenReturn(expectedGroupDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL_GET_ALL_GROUPS);
        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.view().name("group/groups"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"))
                .andExpect(MockMvcResultMatchers.model().attribute("groups", expectedGroupDTO));
    }

    @Test
    void shouldCallViewAndModelOfSingleGroupAndExpectedValue() throws Exception {
        GroupDTO expectedGroupDTO = expectedStorage.getExpectedGroupDTO();
        when(mockGroupService.getByIdGroupDTO(EXISTENT_GROUP_ID)).thenReturn(expectedGroupDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/groups/" + EXISTENT_GROUP_ID);
        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.view().name("group/group"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("group"))
                .andExpect(MockMvcResultMatchers.model().attribute("group", expectedGroupDTO));
    }

    @Test
    void shouldCallViewErrorIfEntityNotFoundException() throws Exception {
        when(mockGroupService.getByIdGroupDTO(NONEXISTENT_GROUP_ID)).thenThrow(EntityNotFoundException.class);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(ERROR_MESSAGE);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/groups/" + NONEXISTENT_GROUP_ID);
        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.view().name("group/group"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ERROR_MESSAGE));
    }

    @Test
    void whenMarkDeleteSuccessShouldSendSuccessMessageAndRedirect() throws Exception {
        GroupDTO groupDTO = expectedStorage.getExpectedGroupDTO();
        doNothing().when(mockGroupService).markDeleted(groupDTO.getId(), true);
        String message = format("Group %s deleted.", groupDTO.getName());
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_DELETE)
                .param("id", String.valueOf(groupDTO.getId()))
                .param("name", groupDTO.getName())
        );

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_GROUPS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }

    @Test
    void whenUnmarkDeleteShouldSendSuccessMessageAndRedirect() throws Exception {
        GroupDTO groupDTO = expectedStorage.getExpectedGroupDTO();

        doNothing().when(mockGroupService).markDeleted(groupDTO.getId(), false);
        String message = format("Group %s restored.", groupDTO.getName());
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_RESTORE)
                .param("id", String.valueOf(groupDTO.getId()))
                .param("name", groupDTO.getName()));

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_GROUPS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }

    @Test
    void whenMarkDeleteThrowsExceptionShouldSendErrorMessageAndRedirect() throws Exception {
        GroupDTO groupDTO = expectedStorage.getExpectedGroupDTO();

        doThrow(QueryNotExecuteException.class).when(mockGroupService).markDeleted(groupDTO.getId(), true);
        String message = format("Operation for group %s failed.", groupDTO.getName());
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_DELETE)
                .param("id", String.valueOf(groupDTO.getId()))
                .param("name", groupDTO.getName()));

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_GROUPS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMessage"));
    }


    @Test
    void whenCallGetForEditPageForCreatingNewGroupShouldSendEmptyGroupDTO() throws Exception {
        GroupDTO emptyGroupDTO = new GroupDTO();

        ResultActions getResult = mockMvc.perform(get(URL_CREATE_NEW_GROUP));

        getResult.andExpect(MockMvcResultMatchers.view().name("group/group-edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("group"))
                .andExpect(MockMvcResultMatchers.model().attribute("group", emptyGroupDTO));
    }

    @Test
    void whenCallGetForEditPageForUpdatingShouldSendGroupDTO() throws Exception {
        GroupDTO groupDTO = expectedStorage.getExpectedGroupDTO();

        when(mockGroupService.getByIdGroupDTO(groupDTO.getId())).thenReturn(groupDTO);

        ResultActions getResult = mockMvc.perform(get(URL_EDIT_GROUP, groupDTO.getId()));

        getResult.andExpect(MockMvcResultMatchers.view().name("group/group-edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("group"))
                .andExpect(MockMvcResultMatchers.model().attribute("group", groupDTO));
    }

    @Test
    void whenCallPostForUpdatingGroupAndThrowsExceptionThenShouldSendErrorMessageAndRedirect() throws Exception {
        String sourceExceptionMessage = "Source exception message";
        GroupDTO groupDTO = expectedStorage.getExpectedGroupDTO();

        doThrow(new QueryNotExecuteException(sourceExceptionMessage)).when(mockGroupService).update(groupDTO);

        String message = format("Operation for group %s failed.", groupDTO.getName());
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResultUpdate = mockMvc.perform(post(URL_CREATE_NEW_GROUP)
                .param("id", String.valueOf(groupDTO.getId()))
                .param("name", groupDTO.getName())
        );

        postResultUpdate.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_GROUPS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("errorMessage", message));
    }

    @Test
    void whenCallPostForCreatingGroupAndThrowsExceptionThenShouldSendErrorMessageAndRedirect() throws Exception {
        String sourceExceptionMessage = "Source exception message";
        GroupDTO groupDTO = expectedStorage.getExpectedGroupDTO();

        GroupDTO newGroupDTO = expectedStorage.getNewGroupDTO();
        doThrow(new QueryNotExecuteException(sourceExceptionMessage)).when(mockGroupService).create(newGroupDTO);

        String message = format("Operation for group %s failed.", groupDTO.getName());
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResultCreate = mockMvc.perform(post(URL_CREATE_NEW_GROUP)
                .param("name", newGroupDTO.getName())
        );

        postResultCreate.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_GROUPS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("errorMessage", message));
    }

    @Test
    void whenCallPostForSaveNewGroupShouldSendSuccessMessageAndRedirect() throws Exception {
        GroupDTO groupDTO = expectedStorage.getNewGroupDTO();
        doNothing().when(mockGroupService).create(groupDTO);
        String message = format("Group %s updated.", groupDTO.getName());
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_CREATE_NEW_GROUP)
                .param("id", "")
                .param("name", groupDTO.getName())
        );

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_GROUPS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }

    @Test
    void whenCallPostForUpdatingGroupShouldSendSuccessMessageAndRedirect() throws Exception {
        GroupDTO groupDTO = expectedStorage.getExpectedGroupDTO();
        doNothing().when(mockGroupService).update(groupDTO);
        String message = format("Group %s updated.", groupDTO.getName());
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_CREATE_NEW_GROUP)
                .param("id", String.valueOf(groupDTO.getId()))
                .param("name", groupDTO.getName())
        );

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_GROUPS + "/" + groupDTO.getId()))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }
}
