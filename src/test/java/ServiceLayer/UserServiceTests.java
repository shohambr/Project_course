package ServiceLayer;

import DomainLayer.IUserRepository;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.ShoppingCart;
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
    void login_ShouldReturnToken_WhenCredentialsMatch() {
        String username = "yaniv";
        String password = "password";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(hashedPassword);
        when(tokenService.generateToken(username)).thenReturn("mockedToken");

        String result = userService.login(username, password);

        assertEquals("mockedToken", result);
    }

    @Test
    void login_ShouldReturnNull_WhenPasswordIncorrect() {
        String username = "yaniv";
        String password = "password";
        String wrongHash = BCrypt.hashpw("wrongpassword", BCrypt.gensalt());

        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(wrongHash);

        String result = userService.login(username, password);

        assertEquals("incorrect password", result);
    }

    @Test
    void login_ShouldReturnMessage_WhenUserDoesNotExist() {
        when(userRepo.isUserExist("yaniv")).thenReturn(false);

        String result = userService.login("yaniv", "password");

        assertEquals("username does not exist", result);
    }

    @Test
    void signUp_ShouldReturnToken_WhenUserIsNew() {
        when(userRepo.isUserExist("newuser")).thenReturn(false);
        when(tokenService.generateToken("newuser")).thenReturn("token123");

        String result = userService.signUp("newuser", "securepassword");

        assertEquals("token123", result);
        verify(userRepo).addUser(eq("newuser"), anyString());
    }

    @Test
    void signUp_ShouldReturnMessage_WhenUserExists() {
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

        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(hashedPassword);
        when(tokenService.generateToken(username)).thenReturn("tokenAfterLogout");

        String token = userService.login(username, password);

        assertEquals("tokenAfterLogout", token);
    }

    @Test
    void guestLoginLogoutThenPurchase_ShouldFailDueToInvalidToken() throws JsonProcessingException {
        String username = "guestUser";
        String password = "guestPass";
        String token = "validTokenBeforeLogout";
        int userId = 100;

        when(userRepo.isUserExist(username)).thenReturn(false);
        when(tokenService.generateToken(username)).thenReturn(token);

        String returnedToken = userService.signUp(username, password);
        assertEquals(token, returnedToken);

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        when(userRepo.isUserExist(username)).thenReturn(true);
        when(userRepo.getUserPass(username)).thenReturn(hashedPassword);
        when(tokenService.generateToken(username)).thenReturn(token);

        String loginToken = userService.login(username, password);
        assertEquals(token, loginToken);

        RegisteredUser mockUser = mock(RegisteredUser.class);
        when(mockUser.getID()).thenReturn(userId);
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(mockUser);

        UserService spyService = spy(userService);
        doReturn(mockUser).when(spyService).deserializeUser(userJson);

        spyService.logoutRegistered(token, userJson);

        ShoppingCart mockCart = mock(ShoppingCart.class);
        when(mockCart.purchaseCart()).thenReturn(100.0);

        when(tokenService.validateToken(token)).thenReturn(false);

        String purchaseResult = spyService.purchaseCart(userId, token, mockCart);

        assertEquals("Invalid or expired token", purchaseResult);
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
