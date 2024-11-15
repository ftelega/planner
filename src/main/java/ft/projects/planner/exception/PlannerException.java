package ft.projects.planner.exception;

public class PlannerException extends RuntimeException {

    public PlannerException(Exceptions message) {
        super(message.name());
    }
}
