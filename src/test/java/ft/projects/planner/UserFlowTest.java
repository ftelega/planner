package ft.projects.planner;

import ft.projects.planner.model.User;
import ft.projects.planner.model.UserRequest;
import ft.projects.planner.repository.UserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserFlowTest(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    public void before() {
        port = PORT;
        clearUsers();
    }

    @AfterEach
    public void after() {
        clearUsers();
    }

    private void clearUsers() {
        userRepository.deleteAll();
    }

    private void saveTestUser() {
        userRepository.save(User.builder()
                .username(TEST_USERNAME)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build()
        );
    }

    @Test
    public void givenValidCsrf_whenRegister_thenStatusCreated() {
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new UserRequest(TEST_USERNAME, TEST_PASSWORD))
                .log().all()
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(201);
    }

    @Test
    public void givenInvalidCsrf_whenRegister_thenStatusForbidden() {
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, "invalid")
                .contentType(ContentType.JSON)
                .body(new UserRequest(TEST_USERNAME, TEST_PASSWORD))
                .log().all()
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenNoCsrf_whenRegister_thenStatusForbidden() {
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .contentType(ContentType.JSON)
                .body(new UserRequest(TEST_USERNAME, TEST_PASSWORD))
                .log().all()
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenValidAuthAndCsrf_whenLogin_thenStatusOk() {
        saveTestUser();
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((TEST_USERNAME + ":" + TEST_PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .log().all()
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(200);
    }

    @Test
    public void givenInvalidAuth_whenLogin_thenStatusUnauthorized() {
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(("invalid").getBytes(StandardCharsets.UTF_8)))
                .log().all()
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenNoAuth_whenLogin_thenStatusUnauthorized() {
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .log().all()
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenInvalidCsrf_whenLogin_thenStatusForbidden() {
        saveTestUser();
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, "invalid")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((TEST_USERNAME + ":" + TEST_PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .log().all()
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenNoCsrf_whenLogin_thenStatusForbidden() {
        saveTestUser();
        var csrfToken = getCsrfToken();
        given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((TEST_USERNAME + ":" + TEST_PASSWORD).getBytes(StandardCharsets.UTF_8)))
                .log().all()
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(403);
    }

    @Test
    public void givenValidSessionAndCsrf_whenLogout_thenStatusNoContentAndInvalidateSession() {
        saveTestUser();
        var csrfToken = getCsrfToken();
        final String sessionId = getAuthorizedSessionId(csrfToken, TEST_USERNAME, TEST_PASSWORD);
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .log().all()
                .when()
                .post("/api/users/logout")
                .then()
                .statusCode(200);

        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .when()
                .get("/")
                .then()
                .statusCode(401);
    }

    @Test
    public void givenInvalidCsrf_whenLogout_thenStatusForbidden() {
        saveTestUser();
        var csrfToken = getCsrfToken();
        final String sessionId = getAuthorizedSessionId(csrfToken, TEST_USERNAME, TEST_PASSWORD);
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
        saveTestUser();
        var csrfToken = getCsrfToken();
        final String sessionId = getAuthorizedSessionId(csrfToken, TEST_USERNAME, TEST_PASSWORD);
        given()
                .cookie(SESSION_COOKIE_NAME, sessionId)
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .when()
                .post("/api/users/logout")
                .then()
                .statusCode(403);
    }
}
