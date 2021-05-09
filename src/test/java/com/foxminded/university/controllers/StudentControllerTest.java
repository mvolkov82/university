package com.foxminded.university.controllers;

import java.util.List;
import java.util.ResourceBundle;

import static java.lang.String.format;
import static java.util.Locale.US;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    public static final Long NONEXISTENT_STUDENT_ID = 0L;
    public static final String ERROR_MESSAGE = format("Student with id = %s not found!", NONEXISTENT_STUDENT_ID);
    private static final String URL_DELETE = "/students/delete";
    private static final String URL_RESTORE = "/students/restore";
    private static final String URL_CREATE_NEW_STUDENT = "/students/edit";
    private static final String URL_EDIT_STUDENT = "/students/edit/?id={id}";
    private static final String URL_GET_ALL_STUDENTS = "/students";
    private static final String REDIRECT_TO_ALL_STUDENTS = "redirect:/students";

    @Autowired
    private MockMvc mockMvc;

    private ExpectedControllerStorage expectedStorage = new ExpectedControllerStorage();

    @Mock
    private MessageSource mockMessageSource;

    @MockBean
    private StudentService studentService;

    @MockBean
    private GroupService mockGroupService;

//    @InjectMocks
//    private StudentController studentController;

//    @BeforeAll
//    public void setUpBeforeClass() throws Exception {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
//        mockMessageSource.
//    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(Locale.US);
//        return slr;
//    }


    @Test
    void whenCallPostForUpdatingStudentAndThrowsExceptionThenShouldSendErrorMessageAndRedirect() throws Exception {
        String sourceExceptionMessage = "Source exception message";
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        String studentTitle = buildStudentTitle(studentDTO);

        doThrow(new QueryNotExecuteException(sourceExceptionMessage)).when(studentService).update(any(StudentDTO.class));

        String message = format("Operation for student %s failed.", studentTitle);
//        String message = format("Операция для студента %s завершена с ошибкой.", studentTitle);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResultUpdate = mockMvc.perform(post(URL_CREATE_NEW_STUDENT)
//                .locale(ENGLISH)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, US.toLanguageTag())
                        .param("id", String.valueOf(studentDTO.getId()))
                        .param("name", studentTitle)
                        .param("firstName", "Ivan")
                        .param("lastName", "Ivanov")
                        .param("birthDay", "1980-01-01")
                        .param("address", "address address address")
                        .param("phone", "+123456789")
                        .param("email", "email@email.com")
                        .param("startDate", "2020-01-01")
                        .param("groupId", "1")
        );

        postResultUpdate.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_STUDENTS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("errorMessage", message));
    }

    @Test
    void shouldCallViewAndModelAllStudentsAndExpectedValues() throws Exception {
        List<StudentDTO> expectedList = expectedStorage.getExpectedStudentDTOList();
        when(studentService.getAllStudentsDTO()).thenReturn(expectedList);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL_GET_ALL_STUDENTS);
        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.view().name("student/students"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("students"))
                .andExpect(MockMvcResultMatchers.model().attribute("students", expectedList));
    }

    @Test
    void shouldCallViewAndModelOfSingleStudentAndExpectedValue() throws Exception {
        StudentDTO expectedStudent = expectedStorage.getExpectedStudentDTO();
        when(studentService.getByIdStudentDTO(1L)).thenReturn(expectedStudent);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/students/1");
        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.view().name("student/student"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("student"))
                .andExpect(MockMvcResultMatchers.model().attribute("student", expectedStudent));
    }

    @Test
    void shouldCallViewErrorIfEntityNotFoundException() throws Exception {

        when(studentService.getByIdStudentDTO(NONEXISTENT_STUDENT_ID)).thenThrow(EntityNotFoundException.class);
//        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(ERROR_MESSAGE);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn("Студент с id = 0 не найден!");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/students/" + NONEXISTENT_STUDENT_ID);
        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.view().name("student/student"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
//                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ERROR_MESSAGE));
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "Студент с id = 0 не найден!"));
    }

    @Test
    void whenMarkDeleteSuccessShouldSendSuccessMessageAndRedirect() throws Exception {
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        String studentTitle = buildStudentTitle(studentDTO);

        doNothing().when(studentService).markDeleted(studentDTO.getId(), true);
//        String message = format("Student %s deleted..", studentTitle);
        String message = format("Студент %s удален.", studentTitle);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_DELETE)
                .param("id", String.valueOf(studentDTO.getId()))
                .param("name", studentTitle)
                .param("firstName", "Ivan")
                .param("lastName", "Ivanov")
                .param("birthDay", "1980-01-01")
                .param("address", "address address address")
                .param("phone", "+123456789")
                .param("email", "email@email.com")
                .param("startDate", "2020-01-01")
                .param("groupId", "1")
        );

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_STUDENTS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }

    @Test
    void whenUnmarkDeleteShouldSendSuccessMessageAndRedirect() throws Exception {
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        String studentTitle = buildStudentTitle(studentDTO);

        doNothing().when(studentService).markDeleted(studentDTO.getId(), false);
//        String message = format("Student %s restored.", studentTitle);
        String message = format("Студент %s восстановлен.", studentTitle);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_RESTORE)
                .param("id", String.valueOf(studentDTO.getId()))
                .param("name", studentTitle)
                .param("firstName", "Ivan")
                .param("lastName", "Ivanov")
                .param("birthDay", "1980-01-01")
                .param("address", "address address address")
                .param("phone", "+123456789")
                .param("email", "email@email.com")
                .param("startDate", "2020-01-01")
                .param("groupId", "1"));

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_STUDENTS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }

    @Test
    void whenMarkDeleteThrowsExceptionShouldSendErrorMessageAndRedirect() throws Exception {
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        String studentTitle = buildStudentTitle(studentDTO);

        doThrow(QueryNotExecuteException.class).when(studentService).markDeleted(studentDTO.getId(), true);
        String message = format("Operation for student %s failed.", studentTitle);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_DELETE)
                .param("id", String.valueOf(studentDTO.getId()))
                .param("name", studentTitle));

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_STUDENTS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMessage"));
    }


    @Test
    void whenCallGetForEditPageForCreatingNewStudentShouldSendEmptyStudentDTO() throws Exception {
        StudentDTO emptyStudentDTO = new StudentDTO();
        List<GroupDTO> groupDTOS = expectedStorage.getExpectedGroupDTOList();

        when(mockGroupService.getAllNotDeletedDTOS()).thenReturn(groupDTOS);

        ResultActions getResult = mockMvc.perform(get(URL_CREATE_NEW_STUDENT));

        getResult.andExpect(MockMvcResultMatchers.view().name("student/student-edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("student"))
                .andExpect(MockMvcResultMatchers.model().attribute("student", emptyStudentDTO));
    }

    @Test
    void whenCallGetForEditPageForUpdatingShouldSendStudentDTO() throws Exception {
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        List<GroupDTO> groupDTOS = expectedStorage.getExpectedGroupDTOList();

        when(mockGroupService.getAllNotDeletedDTOS()).thenReturn(groupDTOS);
        when(studentService.getByIdStudentDTO(studentDTO.getId())).thenReturn(studentDTO);

        ResultActions getResult = mockMvc.perform(get(URL_EDIT_STUDENT, studentDTO.getId()));

        getResult.andExpect(MockMvcResultMatchers.view().name("student/student-edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("student"))
                .andExpect(MockMvcResultMatchers.model().attribute("student", studentDTO));
    }

    @Test
    void whenCallPostForCreatingStudentAndThrowsExceptionThenShouldSendErrorMessageAndRedirect() throws Exception {
        String sourceExceptionMessage = "Source exception message";
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        String studentTitle = buildStudentTitle(studentDTO);

        doThrow(new QueryNotExecuteException(sourceExceptionMessage)).when(studentService).create(any(StudentDTO.class));

//        String message = format("Operation for student %s failed.", studentTitle);
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        String messageTemplate = bundle.getString("fail.student.operation");
        String message = format(messageTemplate, studentTitle);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);
        when(studentService.countByGroupId(any())).thenReturn(1);
        when(studentService.existsStudentByIdAndGroupId(studentDTO.getId(), null)).thenReturn(false);

        ResultActions postResultCreate = mockMvc.perform(post(URL_CREATE_NEW_STUDENT)
                .param("name", studentTitle)
                .param("firstName", "Ivan")
                .param("lastName", "Ivanov")
                .param("birthDay", "1980-01-01")
                .param("address", "address address address")
                .param("phone", "+123456789")
                .param("email", "email@email.com")
                .param("startDate", "2020-01-01")
                .param("groupId", "1")
        );

        postResultCreate.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_STUDENTS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("errorMessage", message));
    }

    @Test
    void whenCallPostForSaveNewStudentShouldSendSuccessMessageAndRedirect() throws Exception {
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        String studentTitle = buildStudentTitle(studentDTO);

        doNothing().when(studentService).create(any(StudentDTO.class));
//        String message = format("Student %s updated.", studentTitle);
        String message = format("Студент %s создан.", studentTitle);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_CREATE_NEW_STUDENT)
                .param("id", "")
                .param("name", studentTitle)
                .param("firstName", "Ivan")
                .param("lastName", "Ivanov")
                .param("birthDay", "1980-01-01")
                .param("address", "address address address")
                .param("phone", "+123456789")
                .param("email", "email@email.com")
                .param("startDate", "2020-01-01")
                .param("groupId", "1")
        );

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_STUDENTS))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }

    @Test
    void whenCallPostForUpdatingStudentShouldSendSuccessMessageAndRedirect() throws Exception {
        StudentDTO studentDTO = expectedStorage.getExpectedStudentDTO();
        String studentTitle = buildStudentTitle(studentDTO);

        doNothing().when(studentService).update(any(StudentDTO.class));
//        String message = format("Student %s updated.", studentTitle);
        String message = format("Студент %s обновлен.", studentTitle);
        when(mockMessageSource.getMessage(anyString(), any(), any())).thenReturn(message);

        ResultActions postResult = mockMvc.perform(post(URL_CREATE_NEW_STUDENT)
                .param("id", String.valueOf(studentDTO.getId()))
                .param("firstName", "Ivan")
                .param("lastName", "Ivanov")
                .param("birthDay", "1980-01-01")
                .param("address", "address address address")
                .param("phone", "+123456789")
                .param("email", "email@email.com")
                .param("startDate", "2020-01-01")
                .param("groupId", "1")

        );

        postResult.andExpect(MockMvcResultMatchers.view().name(REDIRECT_TO_ALL_STUDENTS + "/" + studentDTO.getId()))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", message));
    }

    private String buildStudentTitle(StudentDTO studentDTO) {
        return format("%s %s", studentDTO.getFirstName(), studentDTO.getLastName());
    }
}
