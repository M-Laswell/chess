package service;


import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

// Register, Login, Logout, Clear

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {


    AuthDAO authDAO = new MemoryAuthDAO();
    AuthService authService = new AuthService(authDAO);
    UserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(userDAO, authService);

    @Test
    @Order(1)
    @DisplayName("Register - Success")
    public void testRegisterSuccess() throws DataAccessException {
        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        userService.register(newUser);

        Assertions.assertNotNull(userDAO.getUser("Hermoine"), "Make sure Hermoine is registered");
    }

    @Test
    @Order(2)
    @DisplayName("Register - Failure")
    public void testRegisterFailure() throws DataAccessException {
        UserData newUser = new UserData(null, "Alohamora", "wizKid27@myspace.com");

        Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUser));

    }

    @Test
    @Order(3)
    @DisplayName("Login - Success")
    public void testLoginSuccess() throws DataAccessException {
        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        userService.register(newUser);
        AuthData token = userService.login(newUser);

        Assertions.assertNotNull(token, "Hermoine has a login token");

    }

    @Test
    @Order(4)
    @DisplayName("Login - Failure")
    public void testLoginFailure() throws DataAccessException {
        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        userService.register(newUser);
        UserData badUser = new UserData("Hermoine", "Alohamour", null);

        Assertions.assertThrows(DataAccessException.class, () -> userService.login(badUser), "Null");
    }

    @Test
    @Order(5)
    @DisplayName("Logout - Success")
    public void testLogoutSuccess() throws DataAccessException {

        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        AuthData token = userService.register(newUser);
        userService.logout(token.getAuthToken());

        Assertions.assertNull(authDAO.getAuth(token.getAuthToken()), "Hermoine doesnt have a login token");

    }

    @Test
    @Order(6)
    @DisplayName("Logout - Failure")
    public void testLogoutFailure() throws DataAccessException {

        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        AuthData token = userService.register(newUser);

        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(null));

    }

    @Test
    @Order(7)
    @DisplayName("Clear - Success")
    public void testClearSuccess() throws DataAccessException {
        UserData newUser = new UserData("Hermoine", "Alohamora", "wizKid27@myspace.com");
        AuthData token = userService.register(newUser);
        userService.clear();

        Assertions.assertNull(userDAO.getUser("Hermoine"), "Should be null");

    }

}
