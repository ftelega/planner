package ft.projects.planner;

import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.User;
import ft.projects.planner.repository.PlanEntryRepository;
import ft.projects.planner.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import static ft.projects.planner.Constants.*;
import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PlanEntryFlowTest extends AbstractIntegrationTest {

    private static String sessionId;
    private static String csrfToken;

    @BeforeAll
    public static void saveAuthUserAndGenerateCookies(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        RestAssured.port = PORT;
        userRepository.save(
                User.builder()
                        .username(AUTH_USERNAME)
                        .password(passwordEncoder.encode(AUTH_PASSWORD))
                        .planEntries(new ArrayList<>())
                        .build()
        );
        csrfToken = getCsrfToken();
        sessionId = getAuthorizedSessionId(csrfToken);
    }

    @AfterAll
    public static void clear(@Autowired UserRepository userRepository, @Autowired PlanEntryRepository planEntryRepository) {
        userRepository.deleteAll();
        planEntryRepository.deleteAll();
    }

    @Test
    public void givenValidSessionAndCsrf_whenCreatePlanEntry_thenStatusCreated() {
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new PlanEntryRequest(TEST_CONTENT))
                .when()
                .post("/api/plan-entries/create")
                .then()
                .statusCode(201);
    }

    @Test
    public void givenInvalidSession_whenCreatePlanEntry_thenStatusUnauthorized() {
        given()
                .cookie(SESSION_COOKIE_NAME, "invalid")
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new PlanEntryRequest(TEST_CONTENT))
                .when()
                .post("/api/plan-entries/create")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenNoSession_whenCreatePlanEntry_thenStatusUnauthorized() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new PlanEntryRequest(TEST_CONTENT))
                .when()
                .post("/api/plan-entries/create")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenInvalidCsrf_whenCreatePlanEntry_thenStatusForbidden() {
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, "invalid")
                .contentType(ContentType.JSON)
                .body(new PlanEntryRequest(TEST_CONTENT))
                .when()
                .post("/api/plan-entries/create")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenNoCsrf_whenCreatePlanEntry_thenStatusForbidden() {
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new PlanEntryRequest(TEST_CONTENT))
                .when()
                .post("/api/plan-entries/create")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenValidSession_whenGetAllPlanEntries_thenStatusOk() {
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .when()
                .get("/api/plan-entries")
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidSession_whenGetAllPlanEntries_thenStatusUnauthorized() {
        given()
                .cookie(SESSION_COOKIE_NAME, "invalid")
                .when()
                .get("/api/plan-entries")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenNoSession_whenGetAllPlanEntries_thenStatusUnauthorized() {
        when()
                .get("/api/plan-entries")
                .then()
                .statusCode(401);
    }
}
