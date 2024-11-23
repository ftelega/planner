package ft.projects.planner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ft.projects.planner.exception.Exceptions;
import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.UserRequest;
import ft.projects.planner.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void givenServiceNotThrows_whenRegister_thenStatusCreated() throws Exception {
        var res = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserRequest(null, null))));
        res.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenServiceThrows_whenRegister_thenStatusBadRequest() throws Exception {
        given(userService.register(any())).willThrow(new PlannerException(Exceptions.INVALID_PASSWORD));
        var res = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserRequest(null, null))));
        res.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}