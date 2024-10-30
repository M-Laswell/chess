package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTests {

    MySqlUserDAO userDAO = new MySqlUserDAO();


    @Test
    @Order(1)
    @DisplayName("Create - Success")
    public void testCreateSuccess() throws DataAccessException {
        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        userDAO.createUser(newUser);

        Assertions.assertNotNull(userDAO.getUser("Hermoine"), "Make sure Hermoine is registered");
    }

    @Test
    @Order(2)
    @DisplayName("Create - Failure")
    public void testCreateFailure() throws DataAccessException {
        UserData newUser = new UserData(null, "Alohamora", "wizKid27@myspace.com");

        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(newUser));

    }

    @Test
    @Order(3)
    @DisplayName("Get - Success")
    public void testGetSuccess() throws DataAccessException {
        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        userDAO.createUser(newUser);
        UserData user = userDAO.getUser(newUser.getUsername());

        Assertions.assertNotNull(user, "Hermoines token was got");

    }

    @Test
    @Order(4)
    @DisplayName("Get - Failure")
    public void testGetFailure() throws DataAccessException {
        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        userDAO.createUser(newUser);


        Assertions.assertThrows(DataAccessException.class, () -> userDAO.getUser(null), "Null");
    }


    @Test
    @BeforeEach
    @DisplayName("Clear - Success")
    public void testClearSuccess() throws DataAccessException {

        userDAO.clear();

        Assertions.assertNull(userDAO.getUser("Hermoine"), "Should be null");

    }

}
