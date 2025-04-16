package ServiceLayer;

import DomainLayer.IUserRepository;
import DomainLayer.Product;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.ShoppingCart;
import DomainLayer.Store;
import DomainLayer.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

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
    }

    @Test
    void login_ShouldReturnJson_WhenCredentialsMatch() throws JsonProcessingException {
        String username = "yaniv";
        String password = "password";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        RegisteredUser user = new RegisteredUser();
        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = mapper.writeValueAsString(user);

        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(hashedPassword);
        when(userRepo.getUser(username)).thenReturn(mapper.writeValueAsString(user));

        String result = userService.login(username, password);

        assertEquals(expectedJson, result);
    }

    @Test
    void login_ShouldReturnNull_WhenPasswordIncorrect()  throws JsonProcessingException {
        String username = "yaniv";
        String password = "password";
        String wrongHash = BCrypt.hashpw("wrongpassword", BCrypt.gensalt());

        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(wrongHash);

        String result = userService.login(username, password);

        assertEquals("incorrect password", result);
    }

    @Test
    void login_ShouldReturnMessage_WhenUserDoesNotExist() throws JsonProcessingException {
        when(userRepo.isUserExist("yaniv")).thenReturn(false);

        String result = userService.login("yaniv", "password");

        assertEquals("username does not exist", result);
    }

    @Test
    void signUp_ShouldReturnJson_WhenUserIsNew() throws JsonProcessingException {
        String username = "newUser";
        String password = "password";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        RegisteredUser user = new RegisteredUser();
        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = mapper.writeValueAsString(user);

        when(userRepo.isUserExist(username)).thenReturn(false);
        when(userRepo.getUser(username)).thenReturn(mapper.writeValueAsString(user));
        when(userRepo.addUser(username, hashedPassword)).thenReturn(true);

        String result = userService.signUp(username, password);

        assertEquals(expectedJson, result);
    }

    @Test
    void signUp_ShouldReturnMessage_WhenUserExists() throws JsonProcessingException {
        when(userRepo.isUserExist("existing")).thenReturn(true);

        String result = userService.signUp("existing", "password");

        assertEquals("username already exists", result);
        verify(userRepo, never()).addUser(any(), any());
    }

    @Test
    void logoutRegistered_ShouldCallUpdate() throws JsonProcessingException {
        RegisteredUser mockUser = mock(RegisteredUser.class);
        when(mockUser.getID()).thenReturn(42);

        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(mockUser);

        UserService spyService = spy(userService);
        doReturn(mockUser).when(spyService).deserializeUser(userJson);

        spyService.logoutRegistered("mockToken", userJson);

        verify(userRepo).update("42", userJson);
        verify(tokenService).invalidateToken("mockToken");
    }

    @Test
    void login_ShouldStillWork_AfterLogout() throws JsonProcessingException {
        String username = "yaniv";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        RegisteredUser user = new RegisteredUser();
        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = mapper.writeValueAsString(user);

        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(hashedPassword);
        when(userRepo.getUser(username)).thenReturn(mapper.writeValueAsString(user));

        String result = userService.login(username, password);

        assertEquals(expectedJson, result);
    }

    @Test
    void rateItem_ShouldSucceed_WhenTokenIsValid() {
        when(tokenService.validateToken("token")).thenReturn(true);
        String result = userService.rateItem("Laptop", 5, "token");
        assertEquals("Rated item 'Laptop' with 5 stars.", result);
    }

    @Test
    void rateItem_ShouldFail_WhenTokenInvalid() {
        when(tokenService.validateToken("token")).thenReturn(false);
        String result = userService.rateItem("Laptop", 5, "token");
        assertEquals("Invalid or expired token", result);
    }

    @Test
    void createStore_ShouldSucceed_WhenTokenValid() {
        when(tokenService.validateToken("token")).thenReturn(true);
        String result = userService.createStore("CoolStore", 101, "token");
        assertEquals("Store 'CoolStore' created by user ID: 101", result);
    }

    @Test
    void createStore_ShouldFail_WhenTokenInvalid() {
        when(tokenService.validateToken("token")).thenReturn(false);
        String result = userService.createStore("CoolStore", 101, "token");
        assertEquals("Invalid or expired token", result);
    }
} 