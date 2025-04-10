package ServiceLayer;

import DomainLayer.IUserRepository;
import DomainLayer.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private IUserRepository userRepo;
    private TokenService tokenService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepo = mock(IUserRepository.class);
        tokenService = mock(TokenService.class);
        userService = new UserService(userRepo, tokenService);

        // Hacky workaround for BCrypt since it's static
        userService = spy(userService);
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsMatch() {
        when(userRepo.isUserExist("yaniv")).thenReturn(true);
        when(userRepo.getUserPass("yaniv")).thenReturn(BCrypt.hashpw("password", BCrypt.gensalt()));
        when(userRepo.getUser("yaniv")).thenReturn("YanivUserObject");

        UserService actualService = new UserService(userRepo, tokenService);
        String result = actualService.login("yaniv", "password");

        assertEquals("YanivUserObject", result);
    }

    @Test
    void login_ShouldReturnNull_WhenPasswordIncorrect() {
        when(userRepo.isUserExist("yaniv")).thenReturn(true);
        when(userRepo.getUserPass("yaniv")).thenReturn(BCrypt.hashpw("wrongpass", BCrypt.gensalt()));

        UserService actualService = new UserService(userRepo, tokenService);
        String result = actualService.login("yaniv", "password");

        assertNull(result);
    }

    @Test
    void login_ShouldReturnMessage_WhenUserDoesNotExist() {
        when(userRepo.isUserExist("yaniv")).thenReturn(false);

        String result = userService.login("yaniv", "password");

        assertEquals("username already exists", result);
    }

    @Test
    void signUp_ShouldReturnToken_WhenUserIsNew() {
        when(userRepo.isUserExist("newuser")).thenReturn(false);
        when(tokenService.generateToken("newuser")).thenReturn("token123");

        UserService actualService = new UserService(userRepo, tokenService);
        String result = actualService.signUp("newuser", "securepassword");

        assertEquals("token123", result);
        verify(userRepo, times(1)).addUser(eq("newuser"), anyString());
    }

    @Test
    void signUp_ShouldReturnMessage_WhenUserExists() {
        when(userRepo.isUserExist("existing")).thenReturn(true);

        String result = userService.signUp("existing", "password");

        assertEquals("username already exists", result);
        verify(userRepo, never()).addUser(any(), any());
    }

    @Test
    void logoutRegistered_ShouldCallUpdate() {
        userService.logoutRegistered("user123", "{\"status\":\"logged out\"}");

        verify(userRepo).update("user123", "{\"status\":\"logged out\"}");
    }


    @Test
    void login_ShouldStillWork_AfterLogout() {
        String username = "yaniv";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Setup mocks
        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(hashedPassword);
        when(userRepo.getUser(username)).thenReturn("YanivUserObject");

        // Simulate logout
        userService.logoutRegistered("yaniv", "{\"status\":\"logged out\"}");
        verify(userRepo).update("yaniv", "{\"status\":\"logged out\"}");

        // Now try to log in again
        String result = userService.login(username, password);

        assertEquals("YanivUserObject", result);
    }
}