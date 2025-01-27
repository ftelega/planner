package ft.projects.planner.repository;

import ft.projects.planner.AbstractIntegrationTest;
import ft.projects.planner.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static ft.projects.planner.Constants.TEST_USERNAME;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends AbstractIntegrationTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void before() {
        clearUsers();
    }

    @AfterEach
    public void after() {
        clearUsers();
    }

    private void clearUsers() {
        userRepository.deleteAll();
    }

    @Test
    public void givenUserExists_whenFindByUsername_thenOptionalPresent() {
        userRepository.save(User.builder()
                .username(TEST_USERNAME)
                .build()
        );
        var res = userRepository.findByUsername(TEST_USERNAME);
        assertTrue(res.isPresent());
    }

    @Test
    public void givenUserNotExist_whenFindByUsername_thenOptionalEmpty() {
        var res = userRepository.findByUsername(TEST_USERNAME);
        assertFalse(res.isPresent());
    }
}