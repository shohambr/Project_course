package ServiceLayer;

import DomainLayer.*;
import DomainLayer.Roles.RegisteredUser;
import infrastructureLayer.UserRepository;
import infrastructureLayer.StoreRepository;
import infrastructureLayer.ProductRepository;
import infrastructureLayer.OrderRepository;
import infrastructureLayer.NotificationRepository;
import DomainLayer.IUserRepository;
import DomainLayer.DomainServices.UserCart;
import DomainLayer.DomainServices.UserConnectivity;
import DomainLayer.IStoreRepository;
import DomainLayer.IProductRepository;
import DomainLayer.IOrderRepository;
import DomainLayer.INotificationRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTests {

    /* ------------- mocked collaborators ------------- */
    private IUserRepository    userRepo;
    private IStoreRepository   storeRepo;
    private IProductRepository productRepo;
    @Mock private IOrderRepository   orderRepo;
    @Mock private IJobRepository     jobRepo;
    private INotificationRepository notificationRepo;
    @Mock private IPayment payment;
    @Mock private IShipping shipping;

    /* ------------- real helpers & SUT --------------- */
    private IToken   tokenService;
    private ProductService productService;
    private StoreService   storeService;
    private JobService     jobService;
    private UserService    userService;
    private RegisteredService registeredService;
    private UserCart       userCart;
    private UserConnectivity userConnectivity;

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
        //storeService   = new StoreService(storeRepo, productService);
        //jobService     = new JobService(jobRepo, storeService);
        userRepo       = new UserRepository();
        storeRepo      = new StoreRepository();
        productRepo    = new ProductRepository();
        notificationRepo = new NotificationRepository();
        userCart       = new UserCart(tokenService, userRepo , storeRepo, productRepo , payment , orderRepo , shipping);
        userConnectivity = new UserConnectivity(tokenService, userRepo);
        userService    = new UserService(userRepo, tokenService, productService , storeRepo , productRepo , payment , orderRepo , userConnectivity , userCart);
        registeredService = new RegisteredService(userRepo, tokenService , storeRepo, productRepo , orderRepo , notificationRepo);
        mapper.registerModule(new ProductKeyModule());



        userService.signUp("yaniv", PLAIN_PW);

        testUser   = mapper.readValue(userRepo.getUser("yaniv"), RegisteredUser.class);
        validToken = tokenService.generateToken("yaniv");

        product = new Product("1", "store1", "product1", "description", 10, 10, 4.5, "");
        store   = new Store();
        Order order = new Order("1", "yaniv", 10.0);

        storeId   = store.getId();
        productId = product.getId();
        store.addNewProduct(productId , product.getQuantity());
        storeRepo.addStore(storeId, mapper.writeValueAsString(store));
        productRepo.save(product);
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

    
}