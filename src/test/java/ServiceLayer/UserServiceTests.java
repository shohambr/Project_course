package ServiceLayer;

import DomainLayer.*;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.RegisteredUser;
import Mocks.MockPayment;
import Mocks.MockShipping;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pure-unit tests: the only concrete class under test is UserService.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)   // suppress UnnecessaryStubbingException
class UserServiceTests {

    /* ---------------- mocked collaborators ---------------- */
    @Mock private IUserRepository    userRepo;
    @Mock private IStoreRepository   storeRepo;
    @Mock private IProductRepository productRepo;
    @Mock private IJobRepository     jobRepo;

    /* ---------------- real support objects ---------------- */
    private TokenService   tokenService;     // real blacklist logic
    private ProductService productService;
    private StoreService   storeService;
    private JobService     jobService;
    private UserService    userService;

    /* ---------------- shared fixtures --------------------- */
    private final ObjectMapper mapper    = new ObjectMapper();
    private final String       PLAIN_PW  = "password";
    private final String       HASHED_PW = BCrypt.hashpw(PLAIN_PW, BCrypt.gensalt());
    private RegisteredUser     testUser;
    private String             validToken;
    private Product           product;
    private Store             store;

    /* ====================================================== */
    @BeforeEach
    void setUp() throws Exception {

        /* ---- real lightweight services wired with mocks ---- */
        tokenService   = new TokenService();
        productService = new ProductService(productRepo);
        storeService   = new StoreService(storeRepo, productService);
        jobService     = new JobService(jobRepo, storeService);
        userService    = new UserService(userRepo, tokenService, jobService, productService, new PaymentService(new MockPayment()), new ShippingService(new MockShipping()));
        this.mapper.registerModule(new ProductKeyModule());

        /* ---- default mock behaviour ---- */
        when(userRepo.getUserPass("yaniv")).thenReturn(null);   // user doesn't exist yet

        // simulate repo state mutation when signUp() adds the user
        doAnswer(inv -> {
            when(userRepo.getUserPass("yaniv")).thenReturn(HASHED_PW);
            when(userRepo.getUser("yaniv")).thenReturn(validUserJson());
            return null;
        }).when(userRepo).addUser(eq("yaniv"), anyString(), anyString());

        /* ---- perform initial successful sign-up ---- */
        userService.signUp("yaniv", PLAIN_PW);

        testUser   = mapper.readValue(validUserJson(), RegisteredUser.class);
        validToken = tokenService.generateToken("yaniv");
        product = new Product("1", "store1", "product1", "description", 10, 5, 4.5, "category");
        store = new Store();

    }

    /* ---------------- helper to build matching JSON -------- */
    private String validUserJson() throws JsonProcessingException {
        ObjectNode root = mapper.createObjectNode();
        root.put("id",            java.util.UUID.randomUUID().toString());
        root.putPOJO("shoppingCart",
                     new DomainLayer.ShoppingCart()); // or whatever minimal cart object
        root.putArray("jobs");                        // empty jobs
        root.put("name", "yaniv");                    // <-- RegisteredUser knows 'name'
        root.putNull("token");
        return mapper.writeValueAsString(root);
    }

    /* ===================== sign-up tests =================== */
    @Test
    void signup_UserAlreadyExists() throws Exception {
        doThrow(new IllegalArgumentException("duplicate"))
            .when(userRepo).addUser(eq("yaniv"), any(), any());

        assertThrows(Exception.class, () -> userService.signUp("yaniv", PLAIN_PW));
    }

    @Test void signup_UsernameIsNull()   { assertThrows(Exception.class,
            () -> userService.signUp(null, PLAIN_PW)); }

    @Test void signup_PasswordIsNull()   { assertThrows(Exception.class,
            () -> userService.signUp("yaniv", null)); }

    @Test void signup_UsernameIsEmpty()  { assertThrows(Exception.class,
            () -> userService.signUp("", PLAIN_PW)); }

    @Test void signup_PasswordIsEmpty()  { assertThrows(Exception.class,
            () -> userService.signUp("yaniv", "")); }

    /* ===================== login tests ===================== */
    @Test
    void login_Right_params() throws Exception {
        RegisteredUser user = userService.login("yaniv", PLAIN_PW);
        assertNotNull(user);
        assertEquals("yaniv", user.getName());
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

    /* ===================== logout tests ==================== */
    @Test
    void logoutRegistered_Right_params() throws Exception {
        userService.logoutRegistered(validToken, testUser);
        assertThrows(Exception.class,
                () -> tokenService.validateToken(validToken));
        assertThrows(Exception.class,
                () -> userService.logoutRegistered(validToken, testUser));
    }

    @Test
    void logoutRegistered_UserNotLoggedIn() throws Exception {
        userService.logoutRegistered(validToken, testUser); // first logout
        assertThrows(Exception.class,
                () -> userService.logoutRegistered(validToken, testUser));
    }

    @Test
    void logoutRegistered_UserNotExist() {
        assertThrows(Exception.class,
                () -> userService.logoutRegistered(validToken, null));
    }

    /* ===================== remove from cart tests ==================== */
    @Test
    void removeFromCart_Right_params() throws Exception {
        testUser.addProduct(store, product);
        userService.removeFromCart(validToken, testUser, store, product);
        assertTrue(testUser.getShoppingCart().getShoppingBags().isEmpty());
    }
    @Test
    void removeFromCart_UserNotLoggedIn() throws Exception {
        userService.logoutRegistered(validToken, testUser); // first logout
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, testUser, store, product));
    }
    @Test
    void removeFromCart_UserNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, null, store, product));
    }
    @Test
    void removeFromCart_StoreNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, testUser, null, product));
    }
    @Test
    void removeFromCart_ProductNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(validToken, testUser, store, null));
    }
    @Test
    void removeFromCart_TokenNotExist() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart(null, testUser, store, product));
    }
    @Test
    void removeFromCart_TokenIsEmpty() {
        assertThrows(Exception.class,
                () -> userService.removeFromCart("", testUser, store, product));
    }


    /* ===================== add to cart tests ==================== */
    @Test
    void addToCart_Right_params() throws Exception {
        userService.addToCart(validToken, testUser, store, product);
        assertFalse(testUser.getShoppingCart().getShoppingBags().isEmpty());
    }
    @Test
    void addToCart_UserNotLoggedIn() throws Exception {
        userService.logoutRegistered(validToken, testUser); // first logout
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, testUser, store, product));
    }
    @Test
    void addToCart_UserNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, null, store, product));
    }
    @Test
    void addToCart_StoreNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, testUser, null, product));
    }
    @Test
    void addToCart_ProductNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(validToken, testUser, store, null));
    }
    @Test
    void addToCart_TokenNotExist() {
        assertThrows(Exception.class,
                () -> userService.addToCart(null, testUser, store, product));
    }

    /* ===================== purchase cart tests ==================== */

    @Test
    void PurchaseCart_Successful() {
        User user = new Guest();
        Store store = new Store();
        Product product1 = new Product("1", store.getId(), "bgdfbf", "bdfgbfgds", 321, 3, 1.0, "1223r");
        store.increaseProduct(product1, 3);
        user.addProduct(store, product1);
        userService.purchaseCart(user, validToken, "csda", "5555555555554444", "10/26", "395", "Israel", "Be'er Sheva", "Even Gvirol", "12");
        assertTrue(user.getShoppingCart().getShoppingBags().isEmpty() & !store.availableProduct(product1, 3));
    }

    @Test
    void PurchaseCart_NotEnoughStock() {
        User user = new RegisteredUser();
        Store store = new Store();
        Product product1 = new Product("1", store.getId(), "bgdfbf", "bdfgbfgds", 321, 3, 1.0, "1223r");
        store.addNewProduct(product1, 1);
        user.addProduct(store, product1);
        user.addProduct(store, product1);
        assertThrows(Exception.class, () -> userService.purchaseCart(user, validToken, "csda", "5555555555554444", "10/26", "395", "Israel", "Be'er Sheva", "Even Gvirol", "12"));
        assertTrue(store.availableProduct(product1, 1));
    }
    @Test
    void PurchaseCart_PaymentServiceFailure() {
        User user = new RegisteredUser();
        Store store = new Store();
        Product product1 = new Product("1", store.getId(), "bgdfbf", "bdfgbfgds", 321, 3, 1.0, "1223r");
        store.addNewProduct(product1, 1);
        user.addProduct(store, product1);
        assertThrows(Exception.class, () -> userService.purchaseCart(user, validToken, "csda", "5555553555554444", "10/26", "395", "Israel", "Be'er Sheva", "Even Gvirol", "12"));
        assertTrue(store.availableProduct(product1, 1));
    }
    @Test
    void PurchaseCart_ShippingFailure() {
        User user = new RegisteredUser();
        Store store = new Store();
        Product product1 = new Product("1", store.getId(), "bgdfbf", "bdfgbfgds", 321, 3, 1.0, "1223r");
        store.addNewProduct(product1, 1);
        user.addProduct(store, product1);
        assertThrows(Exception.class, () -> userService.purchaseCart(user, validToken, "csda", "5555553555554444", "10/26", "395", "Israel", "Be'er Sheva", "Even Gvirol", ""));
        assertTrue(store.availableProduct(product1, 1));    }

}