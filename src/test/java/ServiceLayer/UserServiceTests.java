package ServiceLayer;

import DomainLayer.IJobRepository;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.IUserRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.IProductRepository;
import infrastructureLayer.JobRepository;
import infrastructureLayer.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class UserServiceTest {

    private IUserRepository userRepo;
    private IStoreRepository storeRepo;
    private IProductRepository productRepo;
    private IJobRepository jobRepo;
    private TokenService tokenService;
    private UserService userService;
    private StoreService storeService;
    private ProductService productService;
    private JobService jobService;

    @BeforeEach
    void setUp() {
        userRepo = new UserRepository();
        storeRepo = Mockito.mock(IStoreRepository.class);
        productRepo = Mockito.mock(IProductRepository.class);
        jobRepo = Mockito.mock(IJobRepository.class);
        tokenService = new TokenService();
        productService = new ProductService(productRepo);
        storeService = new StoreService(storeRepo, productService);
        jobService = new JobService(jobRepo, storeService);
        userService = new UserService(userRepo, tokenService, storeService, jobService);
    }

    @Test
    void signup_Right_params() throws Exception {
        String username = "yaniv";
        String password = "password";
        RegisteredUser u = userService.signUp(username, password);
        assertNotNull(u);
    }

    @Test
    void signup_UserAlreadyExists() throws Exception {
        String username = "yaniv";
        String password = "password";
        userService.signUp(username, password);
        RegisteredUser u = userService.signUp(username, password);
        assertNull(u);
    }

    @Test
    void signup_UsernameIsNull() throws Exception {
        String username = null;
        String password = "password";
        RegisteredUser u = userService.signUp(username, password);
        assertNull(u);
    }

    @Test
    void signup_PasswordIsNull() throws Exception {
        String username = "yaniv";
        String password = null;
        RegisteredUser u = userService.signUp(username, password);
        assertNull(u);
    }

    @Test
    void signup_UsernameIsEmpty() throws Exception {
        String username = "";
        String password = "password";
        RegisteredUser u = userService.signUp(username, password);
        assertNull(u);
    }

    @Test
    void signup_PasswordIsEmpty() throws Exception {
        String username = "yaniv";
        String password = "";
        RegisteredUser u = userService.signUp(username, password);
        assertNull(u);
    }

    @Test
    void login_Right_params() throws Exception {
        String username = "yaniv";
        String password = "password";
        userService.signUp(username, password);
        RegisteredUser u = userService.login(username, password);
        assertNotNull(u);
    }

    @Test
    void login_UserDoesNotExist() throws Exception {
        String username = "yaniv";
        String password = "password";
        RegisteredUser u = userService.login(username, password);
        assertNull(u);
    }

    @Test
    void login_IncorrectPassword() throws Exception {
        String username = "yaniv";
        String password = "password";
        userService.signUp(username, password);
        RegisteredUser u = userService.login(username, "wrongpassword");
        assertNull(u);
    }

    @Test
    void login_UsernameIsNull() throws Exception {
        String username = null;
        String password = "password";
        RegisteredUser u = userService.login(username, password);
        assertNull(u);
    }
    @Test
    void login_PasswordIsNull() throws Exception {
        String username = "yaniv";
        String password = null;
        RegisteredUser u = userService.login(username, password);
        assertNull(u);
    }
    @Test
    void login_UsernameIsEmpty() throws Exception {
        String username = "";
        String password = "password";
        RegisteredUser u = userService.login(username, password);
        assertNull(u);
    }
    @Test
    void login_PasswordIsEmpty() throws Exception {
        String username = "yaniv";
        String password = "";
        RegisteredUser u = userService.login(username, password);
        assertNull(u);
    }

//
//    @Test
//    void login_ShouldReturnNull_WhenPasswordIncorrect()  throws JsonProcessingException {
//        String username = "yaniv";
//        String password = "password";
//        String wrongHash = BCrypt.hashpw("wrongpassword", BCrypt.gensalt());
//
//        when(userRepo.isUserExist(username)).thenReturn(Boolean.valueOf(true));
//        when(userRepo.getUserPass(username)).thenReturn(wrongHash);
//
//        String result = userService.login(username, password);
//
//        assertEquals("incorrect password", result);
//    }
//
//    @Test
//    void login_ShouldReturnMessage_WhenUserDoesNotExist() throws JsonProcessingException {
//        when(userRepo.isUserExist("yaniv")).thenReturn(Boolean.valueOf(false));
//
//        String result = userService.login("yaniv", "password");
//
//        assertEquals("username does not exist", result);
//    }
//
//    @Test
//    void signUpTest() throws JsonProcessingException {
//        String username = "newUser";
//        String password = "password";
//        when(userRepo.isUserExist(username)).thenReturn(Boolean.valueOf(false));
//        userService.signUp(username, password);
//
//
//    }
//
//    @Test
//    void signUp_ShouldReturnMessage_WhenUserExists() throws JsonProcessingException {
//        when(userRepo.isUserExist("existing")).thenReturn(Boolean.valueOf(true));
//
//        String result = userService.signUp("existing", "password");
//
//        assertEquals("username already exists", result);
//        verify(userRepo, never()).addUser(any(), any());
//    }
//
//    @Test
//    void logoutRegistered_ShouldCallUpdate() throws JsonProcessingException {
//        RegisteredUser mockUser = mock(RegisteredUser.class);
//        when(mockUser.getID()).thenReturn(42);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String userJson = mapper.writeValueAsString(mockUser);
//
//        UserService spyService = spy(userService);
//        doReturn(mockUser).when(spyService).deserializeUser(userJson);
//
//        spyService.logoutRegistered("mockToken", userJson);
//
//        verify(userRepo).update("42", userJson);
//        verify(tokenService).invalidateToken("mockToken");
//    }
//
//    @Test
//    void login_ShouldStillWork_AfterLogout() throws JsonProcessingException {
//        String username = "yaniv";
//        String password = "password123";
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//        RegisteredUser user = new RegisteredUser();
//        ObjectMapper mapper = new ObjectMapper();
//        String expectedJson = mapper.writeValueAsString(user);
//
//        when(userRepo.isUserExist(username)).thenReturn(true);
//        when(userRepo.getUserPass(username)).thenReturn(hashedPassword);
//        when(userRepo.getUser(username)).thenReturn(mapper.writeValueAsString(user));
//
//        String result = userService.login(username, password);
//
//        assertEquals(expectedJson, result);
//    }
//
//    @Test
//    void rateItem_ShouldSucceed_WhenTokenIsValid() {
//        when(tokenService.validateToken("token")).thenReturn(true);
//        String result = userService.rateItem("Laptop", 5, "token");
//        assertEquals("Rated item 'Laptop' with 5 stars.", result);
//    }
//
//    @Test
//    void rateItem_ShouldFail_WhenTokenInvalid() {
//        when(tokenService.validateToken("token")).thenReturn(false);
//        String result = userService.rateItem("Laptop", 5, "token");
//        assertEquals("Invalid or expired token", result);
//    }
//
//    @Test
//    void createStore_ShouldSucceed_WhenTokenValid() {
//        when(tokenService.validateToken("token")).thenReturn(true);
//        String result = userService.createStore("CoolStore", 101, "token");
//        assertEquals("Store 'CoolStore' created by user ID: 101", result);
//    }
//
//    @Test
//    void createStore_ShouldFail_WhenTokenInvalid() {
//        when(tokenService.validateToken("token")).thenReturn(false);
//        String result = userService.createStore("CoolStore", 101, "token");
//        assertEquals("Invalid or expired token", result);
//    }
}