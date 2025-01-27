package ft.projects.planner.service;

import ft.projects.planner.exception.Exceptions;
import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.PlanEntry;
import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.PlanEntryResponse;
import ft.projects.planner.repository.PlanEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanEntryServiceImpl implements PlanEntryService {

    private final PlanEntryRepository planEntryRepository;
    private final SecurityService securityService;

    @Override
    public PlanEntryResponse createPlanEntry(PlanEntryRequest planEntryRequest) {
        var content = planEntryRequest.content();
        validateContent(content);
        var planEntry = planEntryRepository.save(
                PlanEntry.builder()
                        .content(content)
                        .date(LocalDate.now())
                        .user(securityService.getCurrentUserFromAuthentication())
                        .build()
        );
        return new PlanEntryResponse(planEntry.getUuid().toString(), planEntry.getContent(), planEntry.getDate());
    }

    @Override
    public List<PlanEntryResponse> getUserPlanEntries() {
        var user = securityService.getCurrentUserFromAuthentication();
        return planEntryRepository.findAll()
                .stream()
                .filter(p -> p.getUser().getUuid().equals(user.getUuid()))
                .map(p -> new PlanEntryResponse(p.getUuid().toString(), p.getContent(), p.getDate()))
                .toList();
    }

    private void validateContent(String content) {
        if(content == null || content.length() < 5) {
            throw new PlannerException(Exceptions.INVALID_CONTENT);
        }
    }
}
