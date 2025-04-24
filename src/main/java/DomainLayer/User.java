package DomainLayer;
import java.util.UUID;

import DomainLayer.Roles.RegisteredUser;
import ServiceLayer.UserService;

public abstract class User {
    protected String id = UUID.randomUUID().toString();
    protected ShoppingCart shoppingCart = new ShoppingCart((id));
    protected String myToken;

    public User() {
    }

    public void addProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.addProduct(store , product);
    }

    public void removeProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.removeProduct(store, product);
    }

    public String getToken() {
        return myToken;
    }
    public String getID() {
        return this.id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

}