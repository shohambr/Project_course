package DomainLayer.domainServices;
import ServiceLayer.EventLogger;

import com.fasterxml.jackson.databind.ObjectMapper;

import DomainLayer.IToken;
import DomainLayer.Product;
import DomainLayer.ShoppingCart;
import DomainLayer.Store;
import DomainLayer.Roles.RegisteredUser;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UserCart {
    private IToken Tokener;
    private ObjectMapper objectMapper = new ObjectMapper();

    public UserCart(IToken Tokener) {
        this.Tokener = Tokener;
    }
    
    public String removeFromCart(String token , String userJson , String storeId , String productId , int amount) throws JsonProcessingException {
        if (token == null || userJson == null || storeId == null || productId == null || amount <= 0) {
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token, user, store and product cannot be null");
        }
        Tokener.validateToken(token);
        //generate user
        RegisteredUser user = objectMapper.readValue(userJson, RegisteredUser.class);
        user.removeProduct(storeId, productId , amount);
        EventLogger.logEvent(user.getUsername(), "REMOVE_FROM_CART_SUCCESS");
        //return user json
        String userJsonString = objectMapper.writeValueAsString(user);
        return userJsonString;
    }

    // public void cartAvaliability(String token , RegisteredUser user, ShoppingCart cart) {
    //     if (token == null || user == null || cart == null) {
    //         EventLogger.logEvent(user.getID(), "CART_AVAILABILITY_FAILED - NULL");
    //         throw new IllegalArgumentException("Token, user and cart cannot be null");
    //     }
    //     Tokener.validateToken(token);
    //     if (cart.isEmpty()) {
    //         EventLogger.logEvent(user.getUsername(), "CART_AVAILABILITY_EMPTY");
    //         throw new IllegalStateException("Cart is empty");
    //     }
    //     for (Product product : cart.getProducts()) {
    //         if (!product.isAvailable()) {
    //             EventLogger.logEvent(user.getUsername(), "CART_AVAILABILITY_PRODUCT_UNAVAILABLE");
    //             throw new IllegalStateException("Product is unavailable");
    //         }
    //     }
    //     EventLogger.logEvent(user.getUsername(), "CART_AVAILABILITY_SUCCESS");
    // }


    public void addToCart(String token , RegisteredUser user,Store store , Product product) {
        if (token == null || user == null || store == null || product == null) {
            EventLogger.logEvent(user.getID(), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token, user, store and product cannot be null");
        }
        Tokener.validateToken(token);
        user.addProduct(store, product);
        EventLogger.logEvent(user.getUsername(), "ADD_TO_CART_SUCCESS");
    }
}
