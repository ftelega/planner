package ft.projects.planner;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public interface Constants {

    String SESSION_COOKIE_NAME = "JSESSIONID";
    String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    int PORT = 7777;
    String TEST_USERNAME = "username";
    String TEST_PASSWORD = "password";
    String TEST_CONTENT = "content";

    static String getCsrfToken() {
        return when()
                .post("/")
                .then()
                .cookie(CSRF_COOKIE_NAME)
                .extract()
                .cookie(CSRF_COOKIE_NAME);
    }

    static String getAuthorizedSessionId(String csrfToken, String username, String password) {
        return given()
                .cookie(CSRF_COOKIE_NAME, csrfToken)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(200)
                .cookie(SESSION_COOKIE_NAME)
                .extract()
                .cookie(SESSION_COOKIE_NAME);
    }
}
