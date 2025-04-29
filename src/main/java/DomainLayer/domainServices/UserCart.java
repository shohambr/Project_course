package DomainLayer.domainServices;
import ServiceLayer.EventLogger;
import DomainLayer.IProductRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.Product;
import DomainLayer.ShoppingBag;
import DomainLayer.ShoppingCart;
import DomainLayer.Store;
import DomainLayer.Roles.RegisteredUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;

public class UserCart {
    private IToken Tokener;
    private ObjectMapper mapper = new ObjectMapper();
    private IStoreRepository storeRepository;
    private IUserRepository userRepository;
    private IProductRepository productRepository;

    public UserCart(IToken Tokener , IUserRepository userRepository, IStoreRepository storeRepository , IProductRepository productRepository) {
        this.Tokener = Tokener;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }
    
    public void removeFromCart(String token , String userId , String storeId , String productId , Integer quantity) throws JsonProcessingException {
        if (token == null || userId == null || storeId == null || productId == null || quantity == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token, userId, storeId, productId and quantity cannot be null");
        }
        RegisteredUser user = mapper.readValue(userRepository.getUser(userId), RegisteredUser.class);
        Tokener.validateToken(token);
        user.removeProduct(storeId, productId , quantity);
        EventLogger.logEvent(user.getUsername(), "REMOVE_FROM_CART_SUCCESS");
    }

    public void addToCart(String token , String userId, String storeId , String productId , Integer quantity) throws JsonProcessingException {
        if (token == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token cannot be null");
        }
        if (userId == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (storeId == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("StoreId cannot be null");
        }
        if (productId == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("ProductId cannot be null");
        }
        if (quantity == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (quantity <= 0) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - INVALID_QUANTITY");
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        RegisteredUser user = mapper.readValue(userRepository.getUser(userId), RegisteredUser.class);
        Tokener.validateToken(token);
        user.addProduct(storeId, productId , quantity);
        userRepository.update(userId, mapper.writeValueAsString(user));
        EventLogger.logEvent(user.getUsername(), "ADD_TO_CART_SUCCESS");
    }

    public Double purchaseCart(String userId) throws JsonProcessingException {
       double totalPrice = 0;
       RegisteredUser user = mapper.readValue(userRepository.getUser(userId), RegisteredUser.class);
       ShoppingCart cart = user.getShoppingCart();
       for (ShoppingBag bag : cart.getShoppingBags()) {
            Store store = storeRepository.getStore(bag.getStoreId());
            if (store == null) {
                EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - STORE_NOT_FOUND");
                throw new IllegalArgumentException("Store not found");
            }
            for (Map.Entry<String, Integer> productEntry : bag.getProducts().entrySet()) {
                //get product from repository if exists if not throw exception
                Product product = productRepository.findById(productEntry.getKey()).orElse(null);
                if (product == null) {
                    EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - PRODUCT_NOT_FOUND");
                    throw new IllegalArgumentException("Product not found");
                }
                if (!store.availableProduct(product, productEntry.getValue())) {
                    EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - PRODUCT_UNAVAILABLE");
                    throw new IllegalArgumentException("Product unavailable");
                }
                if (productEntry.getValue() <= 0) {
                    EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - INVALID_QUANTITY");
                    throw new IllegalArgumentException("Invalid quantity");
                }
                totalPrice += productEntry.getValue() * product.getPrice();
            }
        }

        if (totalPrice <= 0) {
            throw new IllegalArgumentException("Total price must be greater than 0");
        }

        return totalPrice;
    }
        
}   //I AM HERE! I ADD IT
