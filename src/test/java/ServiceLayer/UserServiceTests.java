package ServiceLayer;

import DomainLayer.*;
import DomainLayer.Roles.RegisteredUser;
import io.micrometer.observation.Observation.Event;
import utils.ProductKeyModule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTests {

    /* ------------- mocked collaborators ------------- */
    @Mock private IUserRepository    userRepo;
    @Mock private IStoreRepository   storeRepo;
    @Mock private IProductRepository productRepo;
    @Mock private IJobRepository     jobRepo;

    /* ------------- real helpers & SUT --------------- */
    private TokenService   tokenService;
    private ProductService productService;
    private StoreService   storeService;
    private JobService     jobService;
    private UserService    userService;

    /* ------------- shared fixtures ------------------ */
    private final ObjectMapper mapper    = new ObjectMapper();
    private final String       PLAIN_PW  = "password";
    private final String       HASHED_PW = BCrypt.hashpw(PLAIN_PW, BCrypt.gensalt());
    private RegisteredUser     testUser;
    private String             validToken;
    private Product            product;
    private Store              store;

    /* ------------- convenience IDs ------------------ */
    private String userId;
    private String storeId;
    private String productId;

    /* ================================================= */
    @BeforeEach
    void setUp() throws Exception {
        tokenService   = new TokenService();
        productService = new ProductService(productRepo);
        storeService   = new StoreService(storeRepo, productService);
        jobService     = new JobService(jobRepo, storeService);
        userService    = new UserService(userRepo, tokenService, jobService, productService , storeRepo , productRepo);
        mapper.registerModule(new ProductKeyModule());

        when(userRepo.getUserPass("yaniv")).thenReturn(null);

        doAnswer(inv -> {
            when(userRepo.getUserPass("yaniv")).thenReturn(HASHED_PW);
            when(userRepo.getUser("yaniv")).thenReturn(validUserJson());
            return null;
        }).when(userRepo).addUser(eq("yaniv"), anyString(), anyString() ,anyString());


        userService.signUp("yaniv", PLAIN_PW);

        testUser   = mapper.readValue(validUserJson(), RegisteredUser.class);
        validToken = tokenService.generateToken("yaniv");

        product = new Product("1", "store1", "product1", "description", 10, 5, 4.5, "");
        store   = new Store();

        userId    = testUser.getID();
        storeId   = store.getId();
        productId = product.getId();
    }

    private String validUserJson() throws JsonProcessingException {
        ObjectNode root = mapper.createObjectNode();
        root.put("id", java.util.UUID.randomUUID().toString());
        root.putPOJO("shoppingCart", new DomainLayer.ShoppingCart());
        root.putArray("jobs");
        root.put("name", "yaniv");
        root.putNull("token");
        return mapper.writeValueAsString(root);
    }

    /* =============== sign-up tests =================== */
    @Test
    void signup_UserAlreadyExists() throws Exception {
        doThrow(new IllegalArgumentException("duplicate"))
            .when(userRepo).addUser(eq("yaniv"), any(), any() , any());
        when(userRepo.isUserExist("yaniv")).thenReturn(true);
        assertThrows(Exception.class, () -> userService.signUp("yaniv", PLAIN_PW));
    }

    @Test void signup_UsernameIsNull()  { assertThrows(Exception.class,
            () -> userService.signUp(null, PLAIN_PW)); }

    @Test void signup_PasswordIsNull()  { assertThrows(Exception.class,
            () -> userService.signUp("yaniv", null)); }

    @Test void signup_UsernameIsEmpty() { assertThrows(Exception.class,
            () -> userService.signUp("", PLAIN_PW)); }

    @Test void signup_PasswordIsEmpty() { assertThrows(Exception.class,
            () -> userService.signUp("yaniv", "")); }

    /* =============== login tests ===================== */
    @Test
    void login_Right_params() throws Exception {
        when(userRepo.getUserPass("yaniv")).thenReturn(HASHED_PW);
        when(userRepo.getUser("yaniv")).thenReturn(validUserJson());
        when(userRepo.isUserExist("yaniv")).thenReturn(true);
        String token = userService.login("yaniv", PLAIN_PW);
        assertNotNull(token);
        assertEquals(validToken, token);
    }

    @Test void login_UserDoesNotExist()  { assertThrows(Exception.class,
            () -> userService.login("ghost", PLAIN_PW)); }

    @Test void login_IncorrectPassword() { assertThrows(Exception.class,
            () -> userService.login("yaniv", "wrong")); }

    @Test void login_UsernameIsNull()    { assertThrows(Exception.class,
            () -> userService.login(null, PLAIN_PW)); }

    @Test void login_PasswordIsNull()    { assertThrows(Exception.class,
            () -> userService.login("yaniv", null)); }

    @Test void login_UsernameIsEmpty()   { assertThrows(Exception.class,
            () -> userService.login("", PLAIN_PW)); }

    @Test void login_PasswordIsEmpty()   { assertThrows(Exception.class,
            () -> userService.login("yaniv", "")); }

    /* =============== logout tests ==================== */
    @Test
    void logoutRegistered_Right_params() throws Exception {
        when(userRepo.getUserPass("yaniv")).thenReturn(HASHED_PW);
        when(userRepo.getUser("yaniv")).thenReturn(validUserJson());
        when(userRepo.isUserExist("yaniv")).thenReturn(true);
        userService.logoutRegistered(validToken);
        assertThrows(Exception.class, () -> userService.logoutRegistered(validToken));
    }

    @Test
    void logoutRegistered_UserNotLoggedIn() throws Exception {
        userService.logoutRegistered(validToken);
        assertThrows(Exception.class, () -> userService.logoutRegistered(validToken));
    }

    @Test
    void logoutGuest_ShouldFail() {
        String guestToken = tokenService.generateToken("Guest");
        assertThrows(Exception.class, () -> userService.logoutRegistered(guestToken));
    }

    /* =============== remove-from-cart tests =========== */
    @Test
    void removeFromCart_Right_params() throws Exception {
        testUser.addProduct(storeId, productId , 1);
        when(userRepo.getUser(userId)).thenReturn(validUserJson());
        when(storeRepo.getStore(storeId)).thenReturn(store);
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        userService.removeFromCart(validToken, userId, storeId, productId , 1);

    }

    @Test
    void removeFromCart_UserNotLoggedIn() throws Exception {
        userService.logoutRegistered(validToken);
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, userId, storeId, productId , 1));
    }

    @Test
    void removeFromCart_UserNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, null, storeId, productId , 1));
    }

    @Test
    void removeFromCart_StoreNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, userId, null, productId , 1));
    }

    @Test
    void removeFromCart_ProductNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, userId, storeId, null , 1));
    }

    @Test
    void removeFromCart_TokenNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(null, userId, storeId, productId , 1));
    }

    @Test
    void removeFromCart_TokenIsEmpty() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart("", userId, storeId, productId , 1));
    }

    /* =============== add-to-cart tests ================ */
    @Test
    void addToCart_Right_params() throws Exception {
        when(userRepo.getUser(userId)).thenReturn(validUserJson());
        when(storeRepo.getStore(storeId)).thenReturn(store);
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        userService.addToCart(validToken, userId, storeId, product.getId() , 1);
        //assertFalse(testUser.getShoppingCart().getShoppingBags().isEmpty());
    }

    @Test
    void addToCart_UserNotLoggedIn() throws Exception {
        userService.logoutRegistered(validToken);
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, userId, storeId, productId , 1));
    }

    @Test
    void addToCart_UserNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, null, storeId, productId , 1));
    }

    @Test
    void addToCart_StoreNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, userId, null, productId , 1));
    }

    @Test
    void addToCart_ProductNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, userId, storeId, null , 1));
    }

    @Test
    void addToCart_TokenNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(null, userId, storeId, productId , 1));
    }
}