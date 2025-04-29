package DomainLayer.domainServices;
import ServiceLayer.EventLogger;
import DomainLayer.IToken;
import DomainLayer.Product;
import DomainLayer.Store;
import DomainLayer.Roles.RegisteredUser;

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

}
