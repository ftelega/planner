package ft.projects.planner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ft.projects.planner.exception.Exceptions;
import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.service.PlanEntryService;
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

@WebMvcTest(PlanEntryController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlanEntryControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private PlanEntryService planEntryService;

    @Autowired
    public PlanEntryControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void givenValidRequest_whenCreatePlanEntry_thenStatusCreated() throws Exception {
        var res = mockMvc.perform(MockMvcRequestBuilders.post("/api/plan-entries/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PlanEntryRequest(null))));
        res.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenInvalidRequest_whenCreatePlanEntry_thenStatusBadRequest() throws Exception {
        given(planEntryService.createPlanEntry(any())).willThrow(new PlannerException(Exceptions.INVALID_CONTENT));
        var res = mockMvc.perform(MockMvcRequestBuilders.post("/api/plan-entries/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PlanEntryRequest(null))));
        res.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenValidRequest_whenGetAllPlanEntries_thenStatusOk() throws Exception {
        var res = mockMvc.perform(MockMvcRequestBuilders.get("/api/plan-entries"));
        res.andExpect(MockMvcResultMatchers.status().isOk());
    }
}