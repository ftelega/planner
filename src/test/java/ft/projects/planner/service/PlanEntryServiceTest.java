package ft.projects.planner.service;

import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.PlanEntry;
import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.PlanEntryResponse;
import ft.projects.planner.repository.PlanEntryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static ft.projects.planner.Constants.TEST_CONTENT;

class PlanEntryServiceTest {

    private final PlanEntryRepository planEntryRepository = mock(PlanEntryRepository.class);
    private final SecurityService securityService = mock(SecurityService.class);
    private final PlanEntryService planEntryService = new PlanEntryServiceImpl(planEntryRepository, securityService);

    @Test
    public void givenValidPlanEntryRequest_whenCreatePlanEntry_thenVerifyCalls() {
        given(planEntryRepository.save(any())).willAnswer(a -> a.getArgument(0));
        planEntryService.createPlanEntry(new PlanEntryRequest(TEST_CONTENT));
        verify(securityService, times(1)).getCurrentUserFromAuthentication();
        verify(planEntryRepository, times(1)).save(any());
    }

    @Test
    public void givenInvalidContent_whenCreatePlanEntry_thenThrow() {
        assertThrows(PlannerException.class, () -> {
            planEntryService.createPlanEntry(new PlanEntryRequest(null));
        });
    }

    @Test
    public void givenInvalidContent_whenCreatePlanEntry_thenThrow2() {
        assertThrows(PlannerException.class, () -> {
            planEntryService.createPlanEntry(new PlanEntryRequest(""));
        });
    }

    @Test
    public void whenGetAllPlanEntries_thenVerifyCalls() {
        given(planEntryRepository.findAll()).willReturn(List.of(new PlanEntry()));
        var res = planEntryService.getAllPlanEntries();
        verify(planEntryRepository, times(1)).findAll();
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(PlanEntryResponse.class, res.get(0).getClass());
    }

}