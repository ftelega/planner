package ft.projects.planner.service;

import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.*;
import ft.projects.planner.repository.PlanEntryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static ft.projects.planner.Constants.TEST_CONTENT;

class PlanEntryServiceTest {

    private final PlanEntryRepository planEntryRepository = mock(PlanEntryRepository.class);
    private final SecurityService securityService = mock(SecurityService.class);
    private final PlanEntryService planEntryService = new PlanEntryServiceImpl(planEntryRepository, securityService);

    @Test
    public void givenValidPlanEntryRequest_whenCreatePlanEntry_thenVerifyCalls() {
        given(planEntryRepository.save(any())).willReturn(PlanEntry.builder().Uuid(UUID.randomUUID()).build());
        var res = planEntryService.createPlanEntry(new PlanEntryRequest(TEST_CONTENT));
        verify(securityService, times(1)).getCurrentUserFromAuthentication();
        verify(planEntryRepository, times(1)).save(any());
        assertNotNull(res);
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
    public void whenGetUserPlanEntries_thenVerifyCalls() {
        var user = User.builder()
                .Uuid(UUID.randomUUID())
                .build();
        var planEntry = PlanEntry.builder()
                .Uuid(UUID.randomUUID())
                .user(user)
                .build();
        given(securityService.getCurrentUserFromAuthentication()).willReturn(user);
        given(planEntryRepository.findAll()).willReturn(List.of(planEntry));
        var res = planEntryService.getUserPlanEntries();
        verify(securityService, times(1)).getCurrentUserFromAuthentication();
        verify(planEntryRepository, times(1)).findAll();
        assertNotNull(res);
        assertEquals(1, res.size());
    }
}