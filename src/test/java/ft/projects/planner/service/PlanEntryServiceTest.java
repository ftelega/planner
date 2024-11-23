package ft.projects.planner.service;

import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.*;
import ft.projects.planner.repository.PlanEntryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static ft.projects.planner.Constants.TEST_USERNAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static ft.projects.planner.Constants.TEST_CONTENT;

class PlanEntryServiceTest {

    private final PlanEntryRepository planEntryRepository = mock(PlanEntryRepository.class);
    private final SecurityService securityService = mock(SecurityService.class);
    private final PlanEntryService planEntryService = new PlanEntryServiceImpl(planEntryRepository, securityService);

    @Test
    public void givenValidPlanEntryRequest_whenCreatePlanEntry_thenVerifyCalls() {
        var uuid = UUID.randomUUID();
        given(planEntryRepository.save(any())).willReturn(PlanEntry.builder().Uuid(uuid).build());
        CreatePlanEntryResponse res = planEntryService.createPlanEntry(new PlanEntryRequest(TEST_CONTENT));
        verify(securityService, times(1)).getCurrentUserFromAuthentication();
        verify(planEntryRepository, times(1)).save(any());
        assertNotNull(res);
        assertEquals(uuid.toString(), res.planEntryId());
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
    public void givenOneForUser_whenGetAllPlanEntries_thenOneResult() {
        var user = User.builder()
                .username(TEST_USERNAME)
                .build();
        given(securityService.getCurrentUserFromAuthentication()).willReturn(user);
        given(planEntryRepository.findAll()).willReturn(List.of(PlanEntry.builder().user(user).build()));
        List<PlanEntryResponse> res = planEntryService.getAllPlanEntries();
        verify(planEntryRepository, times(1)).findAll();
        assertNotNull(res);
        assertEquals(1, res.size());
    }

    @Test
    public void givenZeroForUser_whenGetAllPlanEntries_thenEmptyResult() {
        var user1 = User.builder()
                .username(TEST_USERNAME)
                .build();
        var user2 = User.builder()
                .username("")
                .build();
        given(securityService.getCurrentUserFromAuthentication()).willReturn(user1);
        given(planEntryRepository.findAll()).willReturn(List.of(PlanEntry.builder().user(user2).build()));
        List<PlanEntryResponse> res = planEntryService.getAllPlanEntries();
        verify(planEntryRepository, times(1)).findAll();
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

}