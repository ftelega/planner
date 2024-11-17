package ft.projects.planner.controller;

import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.exception.PlannerExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlannerExceptionHandler {

    @ExceptionHandler(value = PlannerException.class)
    public ResponseEntity<PlannerExceptionResponse> handlePlannerException(PlannerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new PlannerExceptionResponse(e.getMessage())
        );
    }
}
