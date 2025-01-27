package ft.projects.planner;

import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.User;
import ft.projects.planner.repository.PlanEntryRepository;
import ft.projects.planner.repository.UserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static ft.projects.planner.Constants.*;
import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PlanEntryFlowTest extends AbstractIntegrationTest {

    private static String sessionId;
    private static String csrfToken;

    @BeforeAll
    public static void saveAuthUserAndGenerateCookies(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        port = PORT;
        userRepository.save(User.builder()
                .username(TEST_USERNAME)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build()
        );
        csrfToken = getCsrfToken();
        sessionId = getAuthorizedSessionId(csrfToken, TEST_USERNAME, TEST_PASSWORD);
    }

    @AfterAll
    public static void clear(@Autowired PlanEntryRepository planEntryRepository, @Autowired UserRepository userRepository) {
        planEntryRepository.deleteAll();
        userRepository.deleteAll();
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
    public void givenValidSession_whenGetUserPlanEntries_thenStatusOk() {
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .when()
                .get("/api/plan-entries")
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidSession_whenGetUserPlanEntries_thenStatusUnauthorized() {
        given()
                .cookie(SESSION_COOKIE_NAME, "invalid")
                .when()
                .get("/api/plan-entries")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenNoSession_whenGetUserPlanEntries_thenStatusUnauthorized() {
        when()
                .get("/api/plan-entries")
                .then()
                .statusCode(401);
    }
}
