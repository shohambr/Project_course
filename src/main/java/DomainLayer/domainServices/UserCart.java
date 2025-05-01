package DomainLayer.domainServices;
import DomainLayer.*;
import ServiceLayer.EventLogger;
import DomainLayer.Roles.RegisteredUser;

import java.util.HashMap;
import java.util.Map;

public class UserCart {
    private IToken Tokener;

    public UserCart(IToken Tokener) {
        this.Tokener = Tokener;
    }
    
    public void removeFromCart(String token , RegisteredUser user,Store store , Product product) {
        if (token == null || user == null || store == null || product == null) {
            EventLogger.logEvent(user.getID(), "REMOVE_FROM_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token, user, store and product cannot be null");
        }
        Tokener.validateToken(token);
        user.removeProduct(store, product);
        EventLogger.logEvent(user.getUsername(), "REMOVE_FROM_CART_SUCCESS");
    }

    public void addToCart(String token , RegisteredUser user,Store store , Product product) {
        if (token == null || user == null || store == null || product == null) {
            EventLogger.logEvent(user.getID(), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token, user, store and product cannot be null");
        }
        Tokener.validateToken(token);
        user.addProduct(store, product);
        EventLogger.logEvent(user.getUsername(), "ADD_TO_CART_SUCCESS");
    }

    public void purchaseCart(User user, String token) {
        if (token == null || user == null) {
            EventLogger.logEvent(user.getID(), "PURCHASE_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token and use crannot be null");
        }
        Tokener.validateToken(token);
        user.getShoppingCart().sold();
        EventLogger.logEvent(user.getID(), "PURCHASE_CART_SUCCESS");
    }

}
