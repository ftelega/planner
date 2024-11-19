package ft.projects.planner;

import ft.projects.planner.model.User;
import ft.projects.planner.model.UserRequest;
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
import java.util.Base64;

import static ft.projects.planner.Constants.*;
import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class UserFlowTest extends AbstractIntegrationTest {

    private static String csrfToken;

    @BeforeAll
    public static void saveAuthUserAndGenerateCsrfToken(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        RestAssured.port = PORT;
        userRepository.save(
                User.builder()
                        .username(AUTH_USERNAME)
                        .password(passwordEncoder.encode(AUTH_PASSWORD))
                        .build()
        );
        csrfToken = getCsrfToken();
    }

    @AfterAll
    public static void clear(@Autowired UserRepository userRepository, @Autowired PlanEntryRepository planEntryRepository) {
        userRepository.deleteAll();
        planEntryRepository.deleteAll();
    }

    @Test
    public void givenValidCsrf_whenRegister_thenStatusCreated() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new UserRequest(TEST_USERNAME, TEST_PASSWORD))
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(201);
    }

    @Test
    public void givenInvalidCsrf_whenRegister_thenStatusForbidden() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, "invalid")
                .contentType(ContentType.JSON)
                .body(new UserRequest(TEST_USERNAME, TEST_PASSWORD))
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenNoCsrf_whenRegister_thenStatusForbidden() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new UserRequest(TEST_USERNAME, TEST_PASSWORD))
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenValidAuthAndCsrf_whenLogin_thenStatusOk() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((AUTH_USERNAME + ":" + AUTH_PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidAuth_whenLogin_thenStatusUnauthorized() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(("invalid").getBytes(StandardCharsets.UTF_8)))
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenNoAuth_whenLogin_thenStatusUnauthorized() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenInvalidCsrf_whenLogin_thenStatusForbidden() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, "invalid")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((AUTH_USERNAME + ":" + AUTH_PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenNoCsrf_whenLogin_thenStatusForbidden() {
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((AUTH_USERNAME + ":" + AUTH_PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenValidSessionAndCsrf_whenLogout_thenStatusNoContentAndInvalidateSession() {
        final String sessionId = getAuthorizedSessionId(csrfToken);
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .when()
                .post("/api/users/logout")
                .then()
                .statusCode(204);
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .when()
                .post("/")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenInvalidCsrf_whenLogout_thenStatusForbidden() {
        final String sessionId = getAuthorizedSessionId(csrfToken);
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, "invalid")
                .when()
                .post("/api/users/logout")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenNoCsrf_whenLogout_thenStatusForbidden() {
        final String sessionId = getAuthorizedSessionId(csrfToken);
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .when()
                .post("/api/users/logout")
                .then()
                .statusCode(403);
    }
}
