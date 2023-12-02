package az.ingress.mapstruct.controller;


import az.ingress.mapstruct.dto.request.UserRequest;
import az.ingress.mapstruct.dto.response.UserResponse;
import az.ingress.mapstruct.mapper.UserMapper;
import az.ingress.mapstruct.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
class UserControllerTest {

    private static final String USER_PATH = "/user";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void saveUser_Success() throws Exception {
        //Arrange
        var request = new UserRequest();
        request.setName("Kamran");
        request.setSurname("Babayev");
        request.setAge(24);
        var user = UserMapper.INSTANCE.mapUserRequestToUserEntity(request);
        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(24);
        when(userService.create(any())).thenReturn(expected);

        //Act
        mockMvc.perform(post(USER_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(objAsJson(request))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kamran"))
                .andExpect(jsonPath("$.surname").value("Babayev"))
                .andExpect(jsonPath("$.age").value("24"));

        //Assert
        verify(userService, times(1)).create(request);
    }

    @Test
    void saveUser_WhenAgeLessThan_BadRequest() throws Exception {
        //Arrange
        var request = new UserRequest();
        request.setName("Kamran");
        request.setSurname("Babayev");
        request.setAge(9);
        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(9);
        when(userService.create(request)).thenReturn(expected);

        //Act
        mockMvc.perform(post(USER_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(objAsJson(request))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //Assert
        verifyNoInteractions(userService);
    }

    @Test
    void saveUser_WhenNameBlank_BadRequest() throws Exception {
        //Arrange
        var request = new UserRequest();
        request.setName("");
        request.setSurname("Babayev");
        request.setAge(24);
        var expected = new UserResponse();
        expected.setName("");
        expected.setSurname("Babayev");
        expected.setAge(24);
        when(userService.create(request)).thenReturn(expected);

        //Act
        mockMvc.perform(post(USER_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(objAsJson(request))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //Assert
        verifyNoInteractions(userService);
    }

    @Test
    void saveUser_WhenSurnameBlank_BadRequest() throws Exception {
        //Arrange
        var request = new UserRequest();
        request.setName("Kamran");
        request.setSurname("");
        request.setAge(24);
        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("");
        expected.setAge(24);
        when(userService.create(request)).thenReturn(expected);

        //Act
        mockMvc.perform(post(USER_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(objAsJson(request))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //Assert
        verifyNoInteractions(userService);
    }


    @Test
    void getAllUsers_Success() throws Exception {

        //Arrange
        List<UserResponse> response = new ArrayList<>();
        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(24);
        response.add(expected);

        when(userService.getAllUsers()).thenReturn(response);

        //Act
        mockMvc.perform(get(USER_PATH + "/all")
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].age").value("24"))
                .andExpect(jsonPath("$[0].name").value("Kamran"))
                .andExpect(jsonPath("$[0].surname").value("Babayev"))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(status().isOk());

        //Assert
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUser_Success() throws Exception {

        //Arrange
        var id = 1L;

        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(24);

        when(userService.getById(id)).thenReturn(expected);

        //Act
        mockMvc.perform(get(USER_PATH + "/{id}", id)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Kamran"))
                .andExpect(jsonPath("$.surname").value("Babayev"))
                .andExpect(jsonPath("$.age").value("24"))
                .andExpect(status().isOk());

        //Assert
        verify(userService, times(1)).getById(id);
    }


    @Test
    void updateUser_Success() throws Exception {

        //Arrange
        var id = 1L;

        var request = new UserRequest();
        request.setName("Kamran");
        request.setSurname("Babayev");
        request.setAge(24);

        var expected = new UserResponse();
        expected.setName("Kamran");
        expected.setSurname("Babayev");
        expected.setAge(24);

        when(userService.updateUser(id, request)).thenReturn(expected);
        //Act
        mockMvc.perform(put(USER_PATH + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objAsJson(request))
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Kamran"))
                .andExpect(jsonPath("$.surname").value("Babayev"))
                .andExpect(jsonPath("$.age").value("24"))
                .andExpect(status().isOk());

        //Assert
        verify(userService, times(1)).updateUser(id, request);
    }

    @Test
    void deleteUser_Success() throws Exception {

        //Arrange
        var id = 1L;

        doNothing().when(userService).deleteUserById(id);

        //Act
        mockMvc.perform(delete(USER_PATH + "/{id}", id))
                .andExpect(status().isOk());

        //Assert
        verify(userService, times(1)).deleteUserById(id);
    }

    private String objAsJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}