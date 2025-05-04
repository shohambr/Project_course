package ServiceLayer;

import DomainLayer.*;
import DomainLayer.Roles.RegisteredUser;
import infrastructureLayer.UserRepository;
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
    private IUserRepository    userRepo;
    @Mock private IStoreRepository   storeRepo;
    @Mock private IProductRepository productRepo;
    @Mock private IJobRepository     jobRepo;
    @Mock private IPayment payment;

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
    private String storeId;
    private String productId;

    /* ================================================= */
    @BeforeEach
    void setUp() throws Exception {
        tokenService   = new TokenService();
        productService = new ProductService(productRepo);
        storeService   = new StoreService(storeRepo, productService);
        jobService     = new JobService(jobRepo, storeService);
        userRepo       = new UserRepository();
        userService    = new UserService(userRepo, tokenService, jobService, productService , storeRepo , productRepo , payment);
        mapper.registerModule(new ProductKeyModule());



        userService.signUp("yaniv", PLAIN_PW);

        testUser   = mapper.readValue(userRepo.getUser("yaniv"), RegisteredUser.class);
        validToken = tokenService.generateToken("yaniv");

        product = new Product("1", "store1", "product1", "description", 10, 10, 4.5, "");
        store   = new Store();

        storeId   = store.getId();
        productId = product.getId();
        store.addNewProduct(productId , product.getQuantity());
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
         String token = userService.login("yaniv", PLAIN_PW);
         assertNotNull(token);
         assertDoesNotThrow(() -> tokenService.validateToken(token));
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


     /* =============== remove-from-cart tests =========== */
     @Test
     void removeFromCart_Right_params() throws Exception {
         testUser.addProduct(storeId, productId, 1);
         userRepo.update("yaniv" , mapper.writeValueAsString(testUser));
         assertEquals(mapper.writeValueAsString(testUser), userRepo.getUser("yaniv"));
         assertFalse(testUser.getShoppingCart().getShoppingBags().isEmpty());
         when(storeRepo.getStore(storeId)).thenReturn(store);
         when(productRepo.findById(productId)).thenReturn(Optional.of(product));
         userService.removeFromCart(validToken, storeId, productId , 1);
         testUser = mapper.readValue(userRepo.getUser("yaniv"), RegisteredUser.class);
         assertTrue(testUser.getShoppingCart().getShoppingBags().isEmpty());
     }


     @Test
     void removeFromCart_UserNotExist() {
         assertThrows(Exception.class,
                 () -> userService.removeFromCart("fdfd", storeId, productId , 1));
     }

     @Test
     void removeFromCart_StoreNotExist() {
         assertThrows(Exception.class,
                 () -> userService.removeFromCart(validToken, null, productId , 1));
     }

     @Test
     void removeFromCart_ProductNotExist() {
         assertThrows(Exception.class,
                 () -> userService.removeFromCart(validToken, storeId, null , 1));
     }

     @Test
     void removeFromCart_TokenNotExist() {
         assertThrows(Exception.class,
                 () -> userService.removeFromCart(null, storeId, productId , 1));
     }

     @Test
     void removeFromCart_TokenIsEmpty() {
         assertThrows(Exception.class,
                 () -> userService.removeFromCart("", storeId, productId , 1));
     }

     /* =============== add-to-cart tests ================ */
     @Test
     void addToCart_Right_params() throws Exception {
         testUser.addProduct(storeId, productId, 1);
         userRepo.update ("yaniv" ,mapper.writeValueAsString(testUser));
         assertEquals(mapper.writeValueAsString(testUser), userRepo.getUser("yaniv"));
         assertFalse(testUser.getShoppingCart().getShoppingBags().isEmpty());
         when(storeRepo.getStore(storeId)).thenReturn(store);
         when(productRepo.findById(productId)).thenReturn(Optional.of(product));
         userService.addToCart(validToken, storeId, productId , 1);
         testUser = mapper.readValue(userRepo.getUser("yaniv"), RegisteredUser.class);
         assertTrue(!testUser.getShoppingCart().getShoppingBags().isEmpty());
     }

     @Test
     void addToCart_UserNotExist() {
         assertThrows(Exception.class,
                 () -> userService.addToCart(tokenService.generateToken("dsds"),  storeId, productId , 1));
     }

     @Test
     void addToCart_StoreNotExist() {
         assertThrows(Exception.class,
                 () -> userService.addToCart(validToken, null, productId , 1));
     }

     @Test
     void addToCart_ProductNotExist() {
         assertThrows(Exception.class,
                 () -> userService.addToCart(validToken, storeId, null , 1));
     }

     @Test
     void addToCart_TokenNotExist() {
         assertThrows(Exception.class,
                 () -> userService.addToCart(null, storeId, productId , 1));
     }


    /* =============== purchase-cart tests ================ */

    @Test
    void purchaseCart_Right_params() throws Exception {
        testUser.addProduct(storeId, productId, 3);
        testUser.setToken(tokenService.generateToken(testUser.getUsername()));
        userRepo.update("yaniv", mapper.writeValueAsString(testUser));
        assertEquals(mapper.writeValueAsString(testUser), userRepo.getUser("yaniv"));
        assertFalse(testUser.getShoppingCart().getShoppingBags().isEmpty());
        when(storeRepo.getStore(storeId)).thenReturn(store);
        when(productRepo.getProduct(productId)).thenReturn(product);
        assertFalse(testUser.getShoppingCart().getShoppingBags().isEmpty());
        userService.purchaseCart(validToken, "", "", "", "");
        testUser = mapper.readValue(userRepo.getUser("yaniv"), RegisteredUser.class);
        assertTrue(testUser.getShoppingCart().getShoppingBags().isEmpty());
    }
}