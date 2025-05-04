package ServiceLayer;

import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.IOrderRepository;
import DomainLayer.IPayment;
import DomainLayer.IProductRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.Jobs.Job;
import DomainLayer.domainServices.UserCart;
import DomainLayer.domainServices.UserConnectivity;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.ShoppingCart;
import DomainLayer.ShoppingBag;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Optional;
import utils.ProductKeyModule;

import DomainLayer.Store;
import DomainLayer.User;

import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserService {

    private final IToken tokenService;
    private final IUserRepository userRepo;
    private final IStoreRepository storeRepo;
    private final IProductRepository productRepo;
    private final IOrderRepository orderRepo;
    private final IPayment payment;
    private final ObjectMapper mapper = new ObjectMapper();
    private final JobService jobService;
    private final ProductService productService;
    private final UserConnectivity userConnectivity;
    private final UserCart userCart;

    public UserService(IUserRepository repository, IToken tokenService, JobService jobService, ProductService productService, IStoreRepository storeRepo , IProductRepository productRepo , IPayment payment , IOrderRepository orderRepo) {
        this.orderRepo = orderRepo;
        this.payment = payment;
        this.storeRepo = storeRepo;
        this.productService = productService;
        this.userRepo = repository;
        this.productRepo = productRepo;
        this.tokenService = tokenService;
        this.userConnectivity = new UserConnectivity(tokenService , repository);
        this.jobService = jobService;
        this.mapper.registerModule(new ProductKeyModule());
        this.mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.userCart = new UserCart(tokenService , repository , storeRepo , productRepo , payment , orderRepo);
    }


    public String login(String username, String password) throws JsonProcessingException {
        try {
            EventLogger.logEvent(username , "LOGIN");
            return userConnectivity.login(username, password);
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "LOGIN_FAILED");
            throw new RuntimeException("Invalid username or password");
        }
    }

    public void signUp(String username, String password)  throws Exception {
        try {
            userConnectivity.signUp(username, password);
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "SIGNUP_FAILED");
            throw new RuntimeException("User already exists");
        }
    }


    public void removeFromCart(String token, String storeId, String productId, Integer quantity) {
        try{
            userCart.removeFromCart(token, storeId, productId, quantity);
            EventLogger.logEvent(tokenService.extractUsername(token), "REMOVE_FROM_CART");
        } catch (Exception e) {
            EventLogger.logEvent(tokenService.extractUsername(token), "REMOVE_FROM_CART_FAILED " + e.getMessage());
            throw new RuntimeException("Failed to remove product from cart");
        }
    }

    public String addToCart(String token, String storeId, String productId, Integer quantity) {
        try{
            userCart.addToCart(token, storeId, productId, quantity);
            EventLogger.logEvent(tokenService.extractUsername(token), "ADD_TO_CART");
            return "Product added to cart";
        } catch (Exception e) {
            EventLogger.logEvent(tokenService.extractUsername(token), "ADD_TO_CART_FAILED " + e.getMessage());
            throw new RuntimeException("Failed to add product to cart");
        }
    }

     public Double reserveCart(String token) {
         try{
            return userCart.reserveCart(token);
         } catch (Exception e) {
             EventLogger.logEvent(tokenService.extractUsername(token), "RESERVE_CART_FAILED");
             throw new RuntimeException("Failed to purchase cart");
         }
     }

    public void purchaseCart(String token , String paymentMethod , String cardNumber, String expirationDate, String cvv) {
        try{
            userCart.purchaseCart(token , reserveCart(token),cardNumber, expirationDate, cvv);
        } catch (Exception e) {
            EventLogger.logEvent(tokenService.extractUsername(token), "PURCHASE_CART_FAILED");
            throw new RuntimeException("Failed to purchase cart");
        }
    }

}
